Place TLS keystore and truststore files in this directory for the Spring Boot server.

Recommended filenames (these names are referenced in application.properties examples):

- server-keystore.p12  (PKCS12 keystore containing the server private key and certificate)
- truststore.p12       (PKCS12 truststore containing CA certificates to trust)

Example (uncomment in application.properties):

server.ssl.enabled=true
server.ssl.key-store=classpath:certs/server-keystore.p12
server.ssl.key-store-password=changeit
server.ssl.key-store-type=PKCS12
server.ssl.trust-store=classpath:certs/truststore.p12
server.ssl.trust-store-password=changeit
server.ssl.trust-store-type=PKCS12
server.ssl.client-auth=want

Notes:
- You can generate a PKCS12 keystore from PEM files using openssl (see README below for quick commands).
- When running inside Docker, you may prefer to mount the host folder into the container and reference absolute paths such as /app/certs/server-keystore.p12.

Quick commands to create a PKCS12 keystore from PEM (example):

# Convert PEM key+cert to PKCS12
openssl pkcs12 -export -inkey server.key -in server.crt -certfile ca.crt -name server -out server-keystore.p12

# Create a truststore PKCS12 containing CA cert(s)
openssl pkcs12 -export -nokeys -in ca.crt -name ca -out truststore.p12

Replace passwords as appropriate and keep keystores secure.
