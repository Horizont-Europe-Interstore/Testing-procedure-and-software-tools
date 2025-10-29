package interstore.Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
// import java.util.Base64;

public final class SfdiUtil {
    private SfdiUtil() {}

    private static int checkDigit(long x) {
        int sum = 0;
        long v = x;
        while (v != 0) {
            sum += v % 10;
            v /= 10;
        }
        return (10 - (sum % 10)) % 10;
    }

    private static long sfdiGen(byte[] lfdi) {
        if (lfdi == null || lfdi.length < 5) throw new IllegalArgumentException("LFDI must be at least 5 bytes");
        long sfdi = 0L;
        for (int i = 0; i < 5; i++) {
            sfdi = (sfdi << 8) + (lfdi[i] & 0xFFL);
        }
        sfdi >>= 4;
        long base = sfdi;
        int cd = checkDigit(base);
        return base * 10 + cd;
    }

    // 1) Given a 40-char hex LFDI, return SFDI decimal string
    public static String calculateSfdiFromLfdiHex(String lfdiHex) {
        if (lfdiHex == null || lfdiHex.length() != 40) return "";
        byte[] lfdiBytes = new byte[20];
        try {
            for (int i = 0; i < 20; i++) {
                String hex = lfdiHex.substring(i * 2, i * 2 + 2);
                lfdiBytes[i] = (byte) Integer.parseInt(hex, 16);
            }
        } catch (NumberFormatException e) {
            return "";
        }
        return Long.toString(sfdiGen(lfdiBytes));
    }

    // 2) Given PEM (-----BEGIN CERTIFICATE-----...-----END CERTIFICATE-----) return LFDI hex
    public static String certificatePemToLfdiHex(String pem) throws Exception {
        if (pem == null) return null;
        // Remove header/footer and base64 decode, or let CertificateFactory parse the PEM bytes
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        byte[] pemBytes = pem.getBytes(StandardCharsets.UTF_8);
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pemBytes));
        byte[] der = cert.getEncoded();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(der);
        byte[] lfdi = Arrays.copyOf(hash, 20);
        // hex lower-case (match your existing Lfdi.java)
        StringBuilder sb = new StringBuilder(lfdi.length * 2);
        for (byte b : lfdi) sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }

    // convenience: full flow from PEM cert -> SFDI
    public static String certificatePemToSfdi(String pem) throws Exception {
        String lfdi = certificatePemToLfdiHex(pem);
        if (lfdi == null || lfdi.isEmpty()) return "";
        return calculateSfdiFromLfdiHex(lfdi);
    }
}