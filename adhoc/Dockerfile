FROM ubuntu:22.04

RUN apt-get update && apt-get install -y \
    g++ \
    libssl-dev \
    libcurl4-openssl-dev \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY adhoc/proxy_server.cpp .
RUN g++ -o ieee2030_proxy proxy_server.cpp -lssl -lcrypto -lcurl

EXPOSE 443

CMD ["./ieee2030_proxy", "443", "/app/certs/certs/cert_key.pem"]