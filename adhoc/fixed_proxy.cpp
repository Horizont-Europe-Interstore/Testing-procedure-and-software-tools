std::string forward_to_java(const std::string& method, const std::string& path, const std::string& body,
                            const std::string& content_type, const std::string& lfdi, const std::string& sfdi) {

    CURL *curl;
    CURLcode res;
    HttpResponse response;
    curl = curl_easy_init();

    if(curl) {
        // Fix: Use localhost instead of hardcoded IP
        std::string url = "http://localhost:8080" + path;
        
        std::cerr << "Forwarding to URL: " << url << " [Method: " << method << "]" << std::endl;

        if (!body.empty()) {
            std::cerr << "Forwarding body (" << body.length() << " bytes)" << std::endl;
        }

        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);

        // Set HTTP method
        if (method == "PUT") {
            curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "PUT");
        } else if (method == "POST") {
            curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "POST");
        } else if (method == "DELETE") {
            curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "DELETE");
        }

        // Set request body if present
        if (!body.empty()) {
            curl_easy_setopt(curl, CURLOPT_POSTFIELDS, body.c_str());
            curl_easy_setopt(curl, CURLOPT_POSTFIELDSIZE, body.length());
        }

        struct curl_slist *headers = NULL;
        std::string lfdi_header = "X-Server-LFDI: " + lfdi;
        std::string sfdi_header = "X-Server-SFDI: " + sfdi;
        headers = curl_slist_append(headers, lfdi_header.c_str());
        headers = curl_slist_append(headers, sfdi_header.c_str());

        // Forward Content-Type if present
        if (!content_type.empty()) {
            std::string content_type_header = "Content-Type: " + content_type;
            headers = curl_slist_append(headers, content_type_header.c_str());
        }

        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);
        res = curl_easy_perform(curl);

        if (res != CURLE_OK) {
            std::cerr << "CURL error: " << curl_easy_strerror(res) << std::endl;
        } else {
            std::cerr << "CURL request successful. Response size: " << response.data.size() << std::endl;
        }

        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    } else {
        std::cerr << "Failed to initialize CURL." << std::endl;
    }

    if (response.data.empty()) {
        return "HTTP/1.1 500 Internal Server Error\r\nContent-Length: 0\r\n\r\n";
    }

    std::ostringstream http_response;
    http_response << "HTTP/1.1 200 OK\r\n"
                  << "Content-Type: application/sep+xml\r\n"
                  << "Content-Length: " << response.data.length() << "\r\n";

    if (path == "/dcap" || path == "/dcap/" || path == "/") {
        http_response << "X-Server-LFDI: " << lfdi << "\r\n"
                      << "X-Server-SFDI: " << sfdi << "\r\n";
    }

    http_response << "\r\n" << response.data;
    return http_response.str();
}