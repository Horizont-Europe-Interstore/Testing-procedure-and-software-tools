FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV OPENSSL_1_1_VERSION=1.1.0l

# Install packages with retry logic
RUN apt-get update || apt-get update && \
    apt-get install -y --no-install-recommends \
    build-essential \
    openssl \
    libssl3 \
    libssl-dev \
    iproute2 \
    net-tools \
    iputils-ping \
    bash \
    ca-certificates \
    git \
    wget \
    make \
    gcc \
    perl \
    zlib1g-dev \
    && rm -rf /var/lib/apt/lists/*

# Install OpenSSL 1.1.0 for building
WORKDIR /tmp
RUN wget https://www.openssl.org/source/openssl-$OPENSSL_1_1_VERSION.tar.gz && \
    tar -xzf openssl-$OPENSSL_1_1_VERSION.tar.gz && \
    cd openssl-$OPENSSL_1_1_VERSION && \
    ./config --prefix=/usr/local/openssl-1.1.0 --openssldir=/usr/local/openssl-1.1.0 shared && \
    make -j$(nproc) && \
    make install && \
    rm -rf /tmp/openssl-*

# Set build environment to use OpenSSL 1.1.0
ENV PKG_CONFIG_PATH=/usr/local/openssl-1.1.0/lib/pkgconfig:$PKG_CONFIG_PATH
ENV CPPFLAGS="-I/usr/local/openssl-1.1.0/include"
ENV LDFLAGS="-L/usr/local/openssl-1.1.0/lib"
ENV LD_LIBRARY_PATH=/usr/local/openssl-1.1.0/lib:$LD_LIBRARY_PATH

# Copy local IEEE-2030.5-Client folder (with custom certs and scripts)
WORKDIR /app
COPY . .

# Build with OpenSSL 1.1.0
RUN sed -i 's/\r$//' ./build.sh && \
    chmod +x ./build.sh && \
    ./build.sh

# Debug: Check versions and linking
RUN echo "System OpenSSL:" && openssl version
RUN echo "OpenSSL 1.1.0 installed at:" && ls -la /usr/local/openssl-1.1.0/lib/
RUN echo "Binary linking:" && ldd /app/build/client_test

RUN sed -i 's/\r$//' /app/run_client.sh /app/persistent_client.sh /app/send_command.sh && \
    chmod +x /app/run_client.sh /app/persistent_client.sh /app/send_command.sh

# Set runtime library path
ENV LD_LIBRARY_PATH=/usr/local/openssl-1.1.0/lib:/usr/lib/x86_64-linux-gnu:$LD_LIBRARY_PATH

ENTRYPOINT ["/app/persistent_client.sh"]
