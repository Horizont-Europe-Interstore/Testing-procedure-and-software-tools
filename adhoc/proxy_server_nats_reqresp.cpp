#include <iostream>
#include <cstring>
#include <unistd.h>
#include <arpa/inet.h>
#include <openssl/ssl.h>
#include <openssl/err.h>
#include <openssl/evp.h>
#include <openssl/pem.h>
#include <openssl/x509.h>
#include <openssl/sha.h>
#include <string>
#include <sstream>
#include <iomanip>
#include <thread>
#include <algorithm>
#include <chrono>
#include <mutex>
#include <condition_variable>
#include <map>
#include <nats/nats.h>

// Global mutex for thread-safe operations
std::mutex response_mutex;

// Map to store pending responses: correlation_id -> response_data
std::map<std::string, std::string> pending_responses;
std::map<std::string, std::condition_variable*> response_cvs;

// Check digit calculation for SFDI
int check_digit(uint64_t x) {
    int sum = 0;
    while(x) {
        sum += x % 10;
        x /= 10;
    }
    return(10 - (sum % 10)) % 10;
}

// SFDI generation from LFDI
uint64_t sfdi_gen(uint8_t *lfdi) {
    int i = 0;
    uint64_t sfdi = 0;
    while(i < 5) sfdi = (sfdi << 8) + lfdi[i++];
    sfdi >>= 4;
    sfdi = sfdi * 10 + check_digit(sfdi);
    return sfdi;
}

// Calculate LFDI from certificate
std::string calculate_lfdi_from_cert(const char *cert_file) {
    FILE *fp = fopen(cert_file, "r");
    if (!fp) return "";

    X509 *cert = PEM_read_X509(fp, NULL, NULL, NULL);
    fclose(fp);
    if (!cert) return "";

    unsigned char *der = NULL;
    int der_len = i2d_X509(cert, &der);
    if (der_len <= 0) {
        X509_free(cert);
        return "";
    }

    unsigned char hash[SHA256_DIGEST_LENGTH];
    SHA256_CTX sha256;
    SHA256_Init(&sha256);
    SHA256_Update(&sha256, der, der_len);
    SHA256_Final(hash, &sha256);

    std::stringstream lfdi_hex;
    for (int i = 0; i < 20; i++) {
        lfdi_hex << std::hex << std::setfill('0') << std::setw(2) << (unsigned int)hash[i];
    }

    OPENSSL_free(der);
    X509_free(cert);
    return lfdi_hex.str();
}

// Calculate SFDI from LFDI
std::string calculate_sfdi_from_lfdi(const std::string& lfdi) {
    if (lfdi.length() != 40) return "";
    uint8_t lfdi_bytes[20];
    for (size_t i = 0; i < 20; i++) {
        std::string hex_byte = lfdi.substr(i * 2, 2);
        char* end;
        unsigned long byte_val = std::strtoul(hex_byte.c_str(), &end, 16);
        if (*end != '\0' || byte_val > 255) return "";
        lfdi_bytes[i] = static_cast<uint8_t>(byte_val);
    }
    return std::to_string(sfdi_gen(lfdi_bytes));
}

// Extract Content-Type from headers
std::string extract_content_type(const std::string& headers) {
    std::istringstream stream(headers);
    std::string line;
    while (std::getline(stream, line)) {
        std::string lower_line = line;
        std::transform(lower_line.begin(), lower_line.end(), lower_line.begin(), ::tolower);

        if (lower_line.find("content-type:") == 0) {
            size_t colon_pos = line.find(':');
            if (colon_pos != std::string::npos) {
                std::string content_type = line.substr(colon_pos + 1);
                content_type.erase(0, content_type.find_first_not_of(" \t\r\n"));
                content_type.erase(content_type.find_last_not_of(" \t\r\n") + 1);
                return content_type;
            }
        }
    }
    return "";
}

// HTTP Request structure
struct HttpRequest {
    std::string method;
    std::string path;
    std::string version;
    std::string headers;
    std::string body;
    std::string full_request;
};

// Parse HTTP request
HttpRequest parse_http_request(const std::string& request) {
    HttpRequest req;
    req.full_request = request;

    std::istringstream stream(request);
    stream >> req.method >> req.path >> req.version;

    std::string line;
    std::getline(stream, line);

    std::ostringstream headers_stream;
    while (std::getline(stream, line) && line != "\r" && !line.empty()) {
        headers_stream << line << "\n";
    }
    req.headers = headers_stream.str();

    std::ostringstream body_stream;
    while (std::getline(stream, line)) {
        body_stream << line << "\n";
    }
    req.body = body_stream.str();

    return req;
}

// Escape XML special characters
std::string escape_xml(const std::string& str) {
    std::string escaped;
    escaped.reserve(str.length());

    for (char c : str) {
        switch (c) {
            case '&':  escaped += "&amp;"; break;
            case '<':  escaped += "&lt;"; break;
            case '>':  escaped += "&gt;"; break;
            case '"':  escaped += "&quot;"; break;
            case '\'': escaped += "&apos;"; break;
            default:   escaped += c; break;
        }
    }
    return escaped;
}

// Base64 encode function
std::string base64_encode(const std::string& input) {
    static const char base64_chars[] =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        "abcdefghijklmnopqrstuvwxyz"
        "0123456789+/";

    std::string encoded;
    int i = 0;
    int j = 0;
    unsigned char char_array_3[3];
    unsigned char char_array_4[4];
    size_t in_len = input.length();
    const unsigned char* bytes_to_encode = reinterpret_cast<const unsigned char*>(input.c_str());

    while (in_len--) {
        char_array_3[i++] = *(bytes_to_encode++);
        if (i == 3) {
            char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
            char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
            char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
            char_array_4[3] = char_array_3[2] & 0x3f;

            for(i = 0; i < 4; i++)
                encoded += base64_chars[char_array_4[i]];
            i = 0;
        }
    }

    if (i) {
        for(j = i; j < 3; j++)
            char_array_3[j] = '\0';

        char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
        char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
        char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);

        for (j = 0; j < i + 1; j++)
            encoded += base64_chars[char_array_4[j]];

        while(i++ < 3)
            encoded += '=';
    }

    return encoded;
}

// Generate unique correlation ID
std::string generate_correlation_id() {
    auto now = std::chrono::system_clock::now();
    auto timestamp = std::chrono::duration_cast<std::chrono::microseconds>(
        now.time_since_epoch()).count();

    std::hash<std::thread::id> hasher;
    size_t thread_id = hasher(std::this_thread::get_id());

    std::ostringstream oss;
    oss << timestamp << "-" << thread_id << "-" << (rand() % 10000);
    return oss.str();
}

// Create XML request payload for NATS
std::string create_request_xml(const HttpRequest& req, const std::string& lfdi,
                                const std::string& sfdi, const std::string& client_ip,
                                const std::string& content_type, const std::string& correlation_id) {
    std::ostringstream xml;
    auto now = std::chrono::system_clock::now();
    auto timestamp = std::chrono::duration_cast<std::chrono::milliseconds>(
        now.time_since_epoch()).count();

    xml << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    xml << "<DeviceRequest xmlns=\"urn:ieee:std:2030.5:ns\">\n";
    xml << "  <correlationId>" << escape_xml(correlation_id) << "</correlationId>\n";
    xml << "  <timestamp>" << timestamp << "</timestamp>\n";
    xml << "  <clientIP>" << escape_xml(client_ip) << "</clientIP>\n";
    xml << "  <lfdi>" << escape_xml(lfdi) << "</lfdi>\n";
    xml << "  <sfdi>" << escape_xml(sfdi) << "</sfdi>\n";
    xml << "  <httpRequest>\n";
    xml << "    <method>" << escape_xml(req.method) << "</method>\n";
    xml << "    <path>" << escape_xml(req.path) << "</path>\n";
    xml << "    <version>" << escape_xml(req.version) << "</version>\n";
    xml << "    <contentType>" << escape_xml(content_type) << "</contentType>\n";
    xml << "    <headers><![CDATA[" << req.headers << "]]></headers>\n";

    if (content_type.find("xml") != std::string::npos || content_type.find("sep+xml") != std::string::npos) {
        xml << "    <body encoding=\"xml\"><![CDATA[" << req.body << "]]></body>\n";
    } else {
        xml << "    <body encoding=\"base64\">" << base64_encode(req.body) << "</body>\n";
    }

    xml << "  </httpRequest>\n";
    xml << "</DeviceRequest>\n";

    return xml.str();
}

// Response callback handler - called when cloud publishes response
void response_callback(natsConnection *nc, natsSubscription *sub, natsMsg *msg, void *closure) {
    const char* reply_data = natsMsg_GetData(msg);
    const char* subject = natsMsg_GetSubject(msg);

    // Extract correlation ID from subject (format: "ieee2030.responses.{correlation_id}")
    std::string subject_str(subject);
    size_t last_dot = subject_str.find_last_of('.');
    if (last_dot == std::string::npos) {
        natsMsg_Destroy(msg);
        return;
    }

    std::string correlation_id = subject_str.substr(last_dot + 1);
    std::string response_data(reply_data);

    std::cout << "Received response from cloud for correlation ID: " << correlation_id << std::endl;

    // Store response and notify waiting thread
    {
        std::lock_guard<std::mutex> lock(response_mutex);
        pending_responses[correlation_id] = response_data;

        // Notify the waiting thread
        if (response_cvs.find(correlation_id) != response_cvs.end()) {
            response_cvs[correlation_id]->notify_one();
        }
    }

    natsMsg_Destroy(msg);
}

// Publish request and wait for response from cloud
std::string request_response_via_nats(natsConnection* conn,
                                       const std::string& request_subject,
                                       const std::string& response_subject_prefix,
                                       const HttpRequest& req,
                                       const std::string& lfdi,
                                       const std::string& sfdi,
                                       const std::string& client_ip,
                                       const std::string& content_type,
                                       int timeout_ms) {

    // Generate unique correlation ID for this request
    std::string correlation_id = generate_correlation_id();
    std::string response_subject = response_subject_prefix + "." + correlation_id;

    std::cout << "\n[Request " << correlation_id << "] Publishing to NATS" << std::endl;
    std::cout << "  Request subject: " << request_subject << std::endl;
    std::cout << "  Response subject: " << response_subject << std::endl;

    // Create XML payload
    std::string payload = create_request_xml(req, lfdi, sfdi, client_ip, content_type, correlation_id);

    // Setup condition variable for this request
    std::condition_variable cv;
    {
        std::lock_guard<std::mutex> lock(response_mutex);
        response_cvs[correlation_id] = &cv;
    }

    // Subscribe to specific response subject
    natsSubscription *sub = NULL;
    natsStatus s = natsConnection_Subscribe(&sub, conn, response_subject.c_str(),
                                             response_callback, NULL);

    if (s != NATS_OK) {
        std::cerr << "Failed to subscribe to response subject: " << natsStatus_GetText(s) << std::endl;
        std::lock_guard<std::mutex> lock(response_mutex);
        response_cvs.erase(correlation_id);
        return "";
    }

    // Publish request to cloud
    s = natsConnection_PublishString(conn, request_subject.c_str(), payload.c_str());
    if (s != NATS_OK) {
        std::cerr << "Failed to publish request: " << natsStatus_GetText(s) << std::endl;
        natsSubscription_Destroy(sub);
        std::lock_guard<std::mutex> lock(response_mutex);
        response_cvs.erase(correlation_id);
        return "";
    }

    natsConnection_Flush(conn);
    std::cout << "[Request " << correlation_id << "] Published, waiting for cloud response..." << std::endl;

    // Wait for response from cloud with timeout
    std::string response_data;
    {
        std::unique_lock<std::mutex> lock(response_mutex);

        if (cv.wait_for(lock, std::chrono::milliseconds(timeout_ms),
            [&correlation_id]{ return pending_responses.find(correlation_id) != pending_responses.end(); })) {
            // Response received
            response_data = pending_responses[correlation_id];
            pending_responses.erase(correlation_id);
            std::cout << "[Request " << correlation_id << "] Response received from cloud ("
                      << response_data.length() << " bytes)" << std::endl;
        } else {
            // Timeout
            std::cerr << "[Request " << correlation_id << "] TIMEOUT waiting for cloud response" << std::endl;
        }

        response_cvs.erase(correlation_id);
    }

    // Cleanup subscription
    natsSubscription_Destroy(sub);

    return response_data;
}

// Initialize OpenSSL
void initialize_openssl() {
    SSL_load_error_strings();
    OpenSSL_add_ssl_algorithms();
}

// Create SSL context
SSL_CTX *create_context() {
    const SSL_METHOD *method = TLS_server_method();
    SSL_CTX *ctx = SSL_CTX_new(method);
    if (!ctx) {
        std::cerr << "Unable to create SSL context" << std::endl;
        ERR_print_errors_fp(stderr);
        exit(EXIT_FAILURE);
    }
    return ctx;
}

// Configure SSL context
void configure_context(SSL_CTX *ctx, const char *cert_key_file) {
    const char *cipher_list = "ECDHE-ECDSA-AES128-CCM8:ECDHE-ECDSA-AES256-CCM8:ECDHE-ECDSA-AES128-GCM-SHA256";
    SSL_CTX_set_cipher_list(ctx, cipher_list);
    SSL_CTX_set_min_proto_version(ctx, TLS1_2_VERSION);
    SSL_CTX_use_certificate_chain_file(ctx, cert_key_file);
    SSL_CTX_use_PrivateKey_file(ctx, cert_key_file, SSL_FILETYPE_PEM);
    SSL_CTX_set_verify(ctx, SSL_VERIFY_NONE, nullptr);
}

// Handle client connection - Send to cloud and wait for response
void handle_client(SSL *ssl, int client_fd, const std::string& lfdi,
                   const std::string& sfdi, const std::string& client_ip,
                   natsConnection* nats_conn, const std::string& request_subject,
                   const std::string& response_subject_prefix, int timeout_ms) {

    char buffer[8192];
    int bytes_read = SSL_read(ssl, buffer, sizeof(buffer) - 1);

    if (bytes_read > 0) {
        buffer[bytes_read] = '\0';
        std::string request(buffer);

        HttpRequest http_req = parse_http_request(request);
        std::string content_type = extract_content_type(http_req.headers);

        std::cout << "\n========== Request from " << client_ip << " ==========" << std::endl;
        std::cout << "Method: " << http_req.method << " | Path: " << http_req.path << std::endl;

        // Send to cloud via NATS and WAIT for response
        std::string cloud_response = request_response_via_nats(
            nats_conn, request_subject, response_subject_prefix,
            http_req, lfdi, sfdi, client_ip, content_type, timeout_ms
        );

        // Send cloud's response back to device
        if (!cloud_response.empty()) {
            // Cloud response is expected to be a complete HTTP response
            SSL_write(ssl, cloud_response.c_str(), cloud_response.length());
            std::cout << "Sent cloud response to device (" << cloud_response.length() << " bytes)" << std::endl;
        } else {
            // Timeout or error - send 504 Gateway Timeout
            std::string error_response =
                "HTTP/1.1 504 Gateway Timeout\r\n"
                "Content-Type: application/json\r\n"
                "Content-Length: 61\r\n"
                "\r\n"
                "{\"status\":\"error\",\"message\":\"Cloud processing timeout\"}";
            SSL_write(ssl, error_response.c_str(), error_response.length());
            std::cerr << "Sent 504 timeout response to device" << std::endl;
        }
    }

    SSL_shutdown(ssl);
    SSL_free(ssl);
    close(client_fd);
}

int main(int argc, char **argv) {
    if (argc != 7) {
        std::cerr << "Usage: " << argv[0] << " <port> <cert_key_file> <nats_url> <request_subject> <response_subject_prefix> <timeout_ms>" << std::endl;
        std::cerr << "Example: " << argv[0] << " 443 /app/certs/cert_key.pem nats://nats:4222 ieee2030.requests ieee2030.responses 30000" << std::endl;
        return EXIT_FAILURE;
    }

    int port = std::stoi(argv[1]);
    const char *cert_key_file = argv[2];
    const char *nats_url = argv[3];
    const char *request_subject = argv[4];
    const char *response_subject_prefix = argv[5];
    int timeout_ms = std::stoi(argv[6]);

    std::string lfdi = calculate_lfdi_from_cert(cert_key_file);
    std::string sfdi = calculate_sfdi_from_lfdi(lfdi);

    if (lfdi.empty()) {
        std::cerr << "Failed to calculate LFDI" << std::endl;
        return EXIT_FAILURE;
    }

    // Initialize NATS connection
    natsConnection *nats_conn = NULL;
    natsOptions *opts = NULL;
    natsStatus s;

    s = natsOptions_Create(&opts);
    if (s == NATS_OK)
        s = natsOptions_SetURL(opts, nats_url);
    if (s == NATS_OK)
        s = natsOptions_SetTimeout(opts, 5000);
    if (s == NATS_OK)
        s = natsConnection_Connect(&nats_conn, opts);

    if (s != NATS_OK) {
        std::cerr << "Failed to connect to NATS: " << natsStatus_GetText(s) << std::endl;
        natsOptions_Destroy(opts);
        return EXIT_FAILURE;
    }

    std::cout << "\n===== IEEE 2030.5 NATS Request-Response Proxy =====" << std::endl;
    std::cout << "NATS Server: " << nats_url << std::endl;
    std::cout << "Request Subject: " << request_subject << std::endl;
    std::cout << "Response Subject Prefix: " << response_subject_prefix << std::endl;
    std::cout << "Response Timeout: " << timeout_ms << " ms" << std::endl;
    std::cout << "LFDI: " << lfdi << std::endl;
    std::cout << "SFDI: " << sfdi << std::endl;
    std::cout << "\nMode: REQUEST-RESPONSE - Device waits for cloud response" << std::endl;
    std::cout << "===================================================\n" << std::endl;

    // Initialize OpenSSL
    initialize_openssl();
    SSL_CTX *ctx = create_context();
    configure_context(ctx, cert_key_file);

    // Create server socket
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEPORT, &opt, sizeof(opt));

    sockaddr_in addr{};
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(port);

    bind(server_fd, (struct sockaddr *)&addr, sizeof(addr));
    listen(server_fd, 50);

    std::cout << "Listening on port " << port << " (Request-Response mode)" << std::endl;

    // Main server loop
    while (true) {
        sockaddr_in client_addr{};
        socklen_t len = sizeof(client_addr);
        int client_fd = accept(server_fd, (struct sockaddr *)&client_addr, &len);

        SSL *ssl = SSL_new(ctx);
        SSL_set_fd(ssl, client_fd);

        char client_ip[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &client_addr.sin_addr, client_ip, INET_ADDRSTRLEN);

        if (SSL_accept(ssl) > 0) {
            // Spawn thread to handle request-response
            std::thread(handle_client, ssl, client_fd, lfdi, sfdi, std::string(client_ip),
                       nats_conn, std::string(request_subject),
                       std::string(response_subject_prefix), timeout_ms).detach();
        } else {
            SSL_free(ssl);
            close(client_fd);
        }
    }

    // Cleanup
    close(server_fd);
    SSL_CTX_free(ctx);
    natsConnection_Destroy(nats_conn);
    natsOptions_Destroy(opts);
    nats_Close();

    return EXIT_SUCCESS;
}
