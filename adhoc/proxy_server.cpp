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
#include <curl/curl.h>

int check_digit(uint64_t x) {
    int sum = 0;
    while(x) {
        sum += x % 10;
        x /= 10;
    }
    return(10 - (sum % 10)) % 10;
}

uint64_t sfdi_gen(uint8_t *lfdi) {
    int i = 0;
    uint64_t sfdi = 0;
    while(i < 5) sfdi = (sfdi << 8) + lfdi[i++];
    sfdi >>= 4;
    sfdi = sfdi * 10 + check_digit(sfdi);
    return sfdi;
}

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

struct HttpResponse {
    std::string data;
};

static size_t WriteCallback(void *contents, size_t size, size_t nmemb, HttpResponse *response) {
    size_t totalSize = size * nmemb;
    response->data.append((char*)contents, totalSize);
    return totalSize;
}

std::string forward_to_java(const std::string& path, const std::string& lfdi, const std::string& sfdi) {
    CURL *curl;
    CURLcode res;
    HttpResponse response;

    curl = curl_easy_init();
    if(curl) {
        std::string url = "http://java-backend:8080" + path;
        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);
        
        struct curl_slist *headers = NULL;
        std::string lfdi_header = "X-Server-LFDI: " + lfdi;
        std::string sfdi_header = "X-Server-SFDI: " + sfdi;
        headers = curl_slist_append(headers, lfdi_header.c_str());
        headers = curl_slist_append(headers, sfdi_header.c_str());
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
        
        res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            std::cerr << "curl_easy_perform() failed: " << curl_easy_strerror(res) << std::endl;
        }
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    }

    if (response.data.empty()) {
        return "HTTP/1.1 500 Internal Server Error\r\nContent-Length: 0\r\n\r\n";
    }

    // Java backend returns just XML, construct proper HTTP response
    std::ostringstream http_response;
    http_response << "HTTP/1.1 200 OK\r\n"
                  << "Content-Type: application/sep+xml\r\n"
                  << "Content-Length: " << response.data.length() << "\r\n";
    
    // Add LFDI/SFDI headers for /dcap responses
    if (path == "/dcap" || path == "/dcap/" || path == "/") {
        http_response << "X-Server-LFDI: " << lfdi << "\r\n"
                      << "X-Server-SFDI: " << sfdi << "\r\n";
    }
    
    http_response << "\r\n" << response.data;
    return http_response.str();
}

void initialize_openssl() {
    SSL_load_error_strings();
    OpenSSL_add_ssl_algorithms();
}

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

void configure_context(SSL_CTX *ctx, const char *cert_key_file) {
    const char *cipher_list = "ECDHE-ECDSA-AES128-CCM8:ECDHE-ECDSA-AES256-CCM8:ECDHE-ECDSA-AES128-GCM-SHA256";
    
    SSL_CTX_set_cipher_list(ctx, cipher_list);
    SSL_CTX_set_min_proto_version(ctx, TLS1_2_VERSION);
    SSL_CTX_use_certificate_chain_file(ctx, cert_key_file);
    SSL_CTX_use_PrivateKey_file(ctx, cert_key_file, SSL_FILETYPE_PEM);
    SSL_CTX_set_verify(ctx, SSL_VERIFY_NONE, nullptr);
}

std::string parse_http_path(const std::string& request) {
    std::istringstream stream(request);
    std::string method, path, version;
    stream >> method >> path >> version;
    return path;
}

int main(int argc, char **argv) {
    if (argc != 3) {
        std::cerr << "Usage: " << argv[0] << " <port> <cert_key_file>" << std::endl;
        return EXIT_FAILURE;
    }

    int port = std::stoi(argv[1]);
    const char *cert_key_file = argv[2];
    
    std::string lfdi = calculate_lfdi_from_cert(cert_key_file);
    std::string sfdi = calculate_sfdi_from_lfdi(lfdi);
    
    if (lfdi.empty()) {
        std::cerr << "Failed to calculate LFDI" << std::endl;
        return EXIT_FAILURE;
    }

    curl_global_init(CURL_GLOBAL_DEFAULT);
    initialize_openssl();
    SSL_CTX *ctx = create_context();
    configure_context(ctx, cert_key_file);

    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    int opt = 1;
    setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    sockaddr_in addr{};
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(port);

    bind(server_fd, (struct sockaddr *)&addr, sizeof(addr));
    listen(server_fd, 5);

    std::cout << "IEEE 2030.5 Proxy listening on port " << port << std::endl;
    std::cout << "LFDI: " << lfdi << std::endl;
    std::cout << "SFDI: " << sfdi << std::endl;

    while (true) {
        sockaddr_in client_addr{};
        socklen_t len = sizeof(client_addr);
        int client_fd = accept(server_fd, (struct sockaddr *)&client_addr, &len);

        SSL *ssl = SSL_new(ctx);
        SSL_set_fd(ssl, client_fd);

        // Log client connection
        char client_ip[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &client_addr.sin_addr, client_ip, INET_ADDRSTRLEN);
        std::cout << "[" << time(nullptr) << "] Connection from " << client_ip << ":" << ntohs(client_addr.sin_port) << std::endl;

        if (SSL_accept(ssl) > 0) {
            std::cout << "SSL handshake successful with " << client_ip << std::endl;
            while (true) {
                char buffer[4096];
                int bytes_read = SSL_read(ssl, buffer, sizeof(buffer) - 1);
                if (bytes_read <= 0) break;
                
                buffer[bytes_read] = '\0';
                std::string request(buffer);
                std::string path = parse_http_path(request);
                
                std::cout << "[" << client_ip << "] Request: " << path << std::endl;
                std::string response = forward_to_java(path, lfdi, sfdi);
                std::cout << "Response: " << response << std::endl;
                
                SSL_write(ssl, response.c_str(), response.length());
                
                if (request.find("Connection: close") != std::string::npos) break;
            }
        }

        SSL_shutdown(ssl);
        SSL_free(ssl);
        close(client_fd);
    }

    close(server_fd);
    SSL_CTX_free(ctx);
    curl_global_cleanup();
    return EXIT_SUCCESS;
}