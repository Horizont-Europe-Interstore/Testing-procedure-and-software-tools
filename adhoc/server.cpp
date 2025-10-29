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

int check_digit( uint64_t x )
{
    int sum = 0;
    while( x )
    {
        sum += x % 10;
        x /= 10;
    }
    return(10 - (sum % 10)) % 10;
}

uint64_t sfdi_gen( uint8_t *lfdi )
{
    int i = 0;
    uint64_t sfdi = 0;
    while( i < 5 ) sfdi = (sfdi << 8) + lfdi[i++];
    sfdi >>= 4;
    sfdi = sfdi * 10 + check_digit( sfdi );
    return sfdi;
}

std::string calculate_lfdi_from_cert(const char *cert_file) {
    FILE *fp = fopen(cert_file, "r");
    if (!fp) {
        std::cerr << "Unable to open certificate file: " << cert_file << std::endl;
        return "";
    }

    X509 *cert = PEM_read_X509(fp, NULL, NULL, NULL);
    fclose(fp);
    if (!cert) {
        std::cerr << "Unable to read certificate" << std::endl;
        return "";
    }

    unsigned char *der = NULL;
    int der_len = i2d_X509(cert, &der);
    if (der_len <= 0) {
        std::cerr << "Unable to convert certificate to DER format" << std::endl;
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
    if (lfdi.length() != 40) {
        std::cerr << "Error: LFDI string must be exactly 40 characters long, got " << lfdi.length() << std::endl;
        return "";
    }
    uint8_t lfdi_bytes[20];
    for (size_t i = 0; i < 20; i++) {
        std::string hex_byte = lfdi.substr(i * 2, 2);
        char* end;
        unsigned long byte_val = std::strtoul(hex_byte.c_str(), &end, 16);
        if (*end != '\0' || byte_val > 255) {
            std::cerr << "Error: Invalid hexadecimal character in LFDI at position " << (i * 2) << std::endl;
            return "";
        }
        lfdi_bytes[i] = static_cast<uint8_t>(byte_val);
    }
    return std::to_string(sfdi_gen(lfdi_bytes));
}

void initialize_openssl() {
    SSL_load_error_strings();
    OpenSSL_add_ssl_algorithms();
}

void cleanup_openssl() {
    EVP_cleanup();
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

void configure_context(SSL_CTX *ctx, const char *cert_key_file, const char *ca_file) {
    const char *cipher_list =
        "ECDHE-ECDSA-AES128-CCM8:"
        "ECDHE-ECDSA-AES256-CCM8:"
        "ECDHE-ECDSA-AES128-GCM-SHA256:"
        "ECDHE-ECDSA-AES256-GCM-SHA384:"
        "ECDHE-ECDSA-AES128-SHA256:"
        "ECDHE-ECDSA-AES256-SHA384:"
        "ECDHE-RSA-AES128-GCM-SHA256:"
        "ECDHE-RSA-AES256-GCM-SHA384:"
        "ECDHE-RSA-AES128-SHA256:"
        "ECDHE-RSA-AES256-SHA384:"
        "DHE-RSA-AES128-GCM-SHA256:"
        "DHE-RSA-AES256-GCM-SHA384";

    if (!SSL_CTX_set_cipher_list(ctx, cipher_list)) {
        std::cerr << "Error setting cipher list" << std::endl;
        ERR_print_errors_fp(stderr);
        exit(EXIT_FAILURE);
    }

    SSL_CTX_set_min_proto_version(ctx, TLS1_2_VERSION);
    SSL_CTX_set_options(ctx, SSL_OP_NO_SSLv2 | SSL_OP_NO_SSLv3 | SSL_OP_NO_TLSv1 | SSL_OP_NO_TLSv1_1);

    if (SSL_CTX_use_certificate_chain_file(ctx, cert_key_file) <= 0) {
        std::cerr << "Error loading certificate chain from: " << cert_key_file << std::endl;
        ERR_print_errors_fp(stderr);
        exit(EXIT_FAILURE);
    }

    if (SSL_CTX_use_PrivateKey_file(ctx, cert_key_file, SSL_FILETYPE_PEM) <= 0) {
        std::cerr << "Error loading private key from: " << cert_key_file << std::endl;
        ERR_print_errors_fp(stderr);
        exit(EXIT_FAILURE);
    }

    if (!SSL_CTX_check_private_key(ctx)) {
        std::cerr << "Private key does not match the certificate public key" << std::endl;
        exit(EXIT_FAILURE);
    }

    SSL_CTX_set_verify(ctx, SSL_VERIFY_NONE, nullptr);
    SSL_CTX_set_verify_depth(ctx, 4);
}

std::string parse_http_path(const std::string& request) {
    std::istringstream stream(request);
    std::string method, path, version;
    stream >> method >> path >> version;
    return path;
}

std::string generate_dcap_response(const std::string& lfdi) {
    std::string dcap_xml =
        "<DeviceCapability xsi:schemaLocation=\"urn:ieee:std:2030.5:ns sep.xsd\" "
        "xmlns=\"urn:ieee:std:2030.5:ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
        "href=\"/dcap\">\n"
        "  <TimeLink href=\"/dcap/tm\"/>\n"
        "  <EndDeviceListLink all=\"1\" href=\"/edev\"/>\n"
        "</DeviceCapability>";
    std::ostringstream response;
    response << "HTTP/1.1 200 OK\r\n"
             << "Content-Type: application/sep+xml\r\n"
             << "Content-Length: " << dcap_xml.length() << "\r\n"
             << "X-Server-LFDI: " << lfdi << "\r\n"
             << "X-Server-SFDI: " << calculate_sfdi_from_lfdi(lfdi) << "\r\n"
             << "\r\n"
             << dcap_xml;
    return response.str();
}

std::string generate_edev_response(const std::string& lfdi) {
    std::string sfdi = calculate_sfdi_from_lfdi(lfdi);
    if (sfdi.empty()) {
        sfdi = "000000000000";
    }

    std::string edev_xml =
        "<EndDeviceList xsi:schemaLocation=\"urn:ieee:std:2030.5:ns sep.xsd\" "
        "xmlns=\"urn:ieee:std:2030.5:ns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
        "href=\"/edev\" subscribable=\"0\" all=\"1\" results=\"1\">\n"
        "  <EndDevice href=\"/edev/edev0\" subscribable=\"0\">\n"
        "    <deviceCategory>00</deviceCategory>\n"
        "    <lFDI>" + lfdi + "</lFDI>\n"
        "    <sFDI>" + sfdi + "</sFDI>\n"
        "    <changedTime>1757478285</changedTime>\n"
        "    <FunctionSetAssignmentsListLink all=\"1\" href=\"/fsa\"/>\n"
        "    <RegistrationLink href=\"/edev/edev0/rg\"/>\n"
        "  </EndDevice>\n"
        "</EndDeviceList>";

    std::ostringstream response;
    response << "HTTP/1.1 200 OK\r\n"
             << "Content-Type: application/sep+xml\r\n"
             << "Content-Length: " << edev_xml.length() << "\r\n"
             << "\r\n"
             << edev_xml;
    return response.str();
}

std::string handle_ieee2030_5_request(const std::string& request, const std::string& lfdi) {
    std::string path = parse_http_path(request);
    std::cout << "Requested path: " << path << std::endl;

    if (path == "/dcap" || path == "/dcap/") {
        return generate_dcap_response(lfdi);
    } else if (path == "/edev" || path == "/edev/") {
        return generate_edev_response(lfdi);
    } else if (path == "/" || path.empty()) {
        return generate_dcap_response(lfdi);
    }
    return "HTTP/1.1 404 Not Found\r\nContent-Length: 0\r\n\r\n";
}

int main(int argc, char **argv) {
    if (argc != 4) {
        std::cerr << "Usage: " << argv[0] << " <port> <cert_key_file> <ca_cert_file>" << std::endl;
        return EXIT_FAILURE;
    }

    int port = std::stoi(argv[1]);
    const char *cert_key_file = argv[2];
    const char *ca_file = argv[3];
    
    std::string lfdi = calculate_lfdi_from_cert(cert_key_file);
    if (lfdi.empty()) {
        std::cerr << "Failed to calculate LFDI from certificate" << std::endl;
        return EXIT_FAILURE;
    }

    initialize_openssl();
    SSL_CTX *ctx = create_context();
    configure_context(ctx, cert_key_file, ca_file);

    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd < 0) {
        perror("Unable to create socket");
        return EXIT_FAILURE;
    }

    int opt = 1;
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt)) < 0) {
        perror("setsockopt SO_REUSEADDR failed");
        close(server_fd);
        return EXIT_FAILURE;
    }

    sockaddr_in addr{};
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = INADDR_ANY;
    addr.sin_port = htons(port);

    if (bind(server_fd, (struct sockaddr *)&addr, sizeof(addr)) < 0) {
        perror("Bind failed");
        close(server_fd);
        return EXIT_FAILURE;
    }

    if (listen(server_fd, 5) < 0) {
        perror("Listen failed");
        close(server_fd);
        return EXIT_FAILURE;
    }

    std::cout << "IEEE 2030.5 Server listening on port " << port << std::endl;
    std::cout << "Certificate: " << cert_key_file << std::endl;
    std::cout << "LFDI: " << lfdi << std::endl;
    std::cout << "SFDI: " << calculate_sfdi_from_lfdi(lfdi) << std::endl;

    while (true) {
        sockaddr_in client_addr{};
        socklen_t len = sizeof(client_addr);
        int client_fd = accept(server_fd, (struct sockaddr *)&client_addr, &len);
        if (client_fd < 0) {
            perror("Accept failed");
            continue;
        }

        SSL *ssl = SSL_new(ctx);
        SSL_set_fd(ssl, client_fd);

        if (SSL_accept(ssl) <= 0) {
            std::cerr << "SSL handshake failed" << std::endl;
            ERR_print_errors_fp(stderr);
        } else {
            // Handle multiple requests on same connection
            while (true) {
                char buffer[4096];
                int bytes_read = SSL_read(ssl, buffer, sizeof(buffer) - 1);
                if (bytes_read <= 0) {
                    break;
                }
                
                buffer[bytes_read] = '\0';
                std::string request(buffer);
                std::cout << "Received request:\n" << request << std::endl;

                std::string response = handle_ieee2030_5_request(request, lfdi);
                
                int bytes_written = SSL_write(ssl, response.c_str(), response.length());
                if (bytes_written <= 0) {
                    break;
                }
                
                if (request.find("Connection: close") != std::string::npos) {
                    break;
                }
            }
        }

        SSL_shutdown(ssl);
        SSL_free(ssl);
        close(client_fd);
    }

    close(server_fd);
    SSL_CTX_free(ctx);
    cleanup_openssl();
    return EXIT_SUCCESS;
}