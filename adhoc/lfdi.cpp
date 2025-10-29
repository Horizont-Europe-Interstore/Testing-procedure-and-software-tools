#include <stdio.h>
#include <stdlib.h>
#include <openssl/pem.h>
#include <cinttypes>
#include <openssl/x509.h>
#include <openssl/evp.h>
#include <openssl/sha.h>

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

// Calculate SFDI from LFDI
uint64_t sfdi_gen( uint8_t *lfdi )
{
    int i = 0;
    uint64_t sfdi = 0;
    while( i < 5 ) sfdi = (sfdi << 8) + lfdi[i++];
    sfdi >>= 4;
    sfdi = sfdi * 10 + check_digit( sfdi );
    return sfdi;
}

// Function: Calculate LFDI
void calculate_lfdi(const char *pem_file) {
    FILE *fp = fopen(pem_file, "r");
    if (!fp) {
        perror("Unable to open file");
        return;
    }

    uint64_t sfdi = 0;
    uint8_t lfdi[20];

    // Read certificate from PEM file
    X509 *cert = PEM_read_X509(fp, NULL, NULL, NULL);
    fclose(fp);

    if (!cert) {
        fprintf(stderr, "Unable to read certificate\n");
        return;
    }

    // Convert certificate to DER format
    unsigned char *der = NULL;
    int der_len = i2d_X509(cert, &der);
    if (der_len <= 0) {
        fprintf(stderr, "Unable to convert certificate to DER format\n");
        X509_free(cert);
        return;
    }

    // Calculate SHA-256 hash
    unsigned char hash[SHA256_DIGEST_LENGTH];
    SHA256_CTX sha256;
    SHA256_Init(&sha256);
    SHA256_Update(&sha256, der, der_len);
    SHA256_Final(hash, &sha256);

    memcpy( lfdi, hash, 20);

    // Output LFDI (first 40 hexadecimal characters)
    printf("LFDI: ");
    for (int i = 0; i < 20; i++) { // 20 bytes = 40 hex digits
        printf("%02x", hash[i]);
    }
    printf("\n");

    // Free memory
    OPENSSL_free(der);
    X509_free(cert);

    sfdi = sfdi_gen( lfdi );
    printf( "SFDI: %" PRIu64 "\n", sfdi );
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <certificate.pem>\n", argv[0]);
        return 1;
    }

    // Initialize OpenSSL library
    OpenSSL_add_all_algorithms();

    // Calculate LFDI
    calculate_lfdi(argv[1]);

    // Cleanup OpenSSL library
    EVP_cleanup();
    return 0;
}