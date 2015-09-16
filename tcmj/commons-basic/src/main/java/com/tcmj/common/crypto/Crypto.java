package com.tcmj.common.crypto;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Map;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crypto {


    /** slf4j Logging framework. */
    private static final Logger LOG = LoggerFactory.getLogger(Crypto.class);

    /**
     * Unofficial hack to bypass the installation of the 'JCE unlimited Strength Policy Files'. Prevents: java.security.InvalidKeyException: Illegal key size or default parameters Do the following, but with reflection to bypass access checks:
     * </p>
     * JceSecurity.isRestricted = false; JceSecurity.defaultPolicy.perms.clear(); JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
     */
    public static final void removeCryptographyRestrictions() {
        LOG.trace("Applying 'JCE unlimited Strenght Policy File' installation fix...");
        try {
            Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
            Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
            Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

            Field isRestricted = jceSecurity.getDeclaredField("isRestricted");
            isRestricted.setAccessible(true);
            isRestricted.set(null, Boolean.FALSE);

            Field defaultPolicy = jceSecurity.getDeclaredField("defaultPolicy");
            defaultPolicy.setAccessible(true);
            PermissionCollection defPolicy = (PermissionCollection) defaultPolicy.get(null);
            Field perms = cryptoPermissions.getDeclaredField("perms");
            perms.setAccessible(true);
            ((Map<?, ?>) perms.get(defPolicy)).clear();

            Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            defPolicy.add((Permission) instance.get(null));
            LOG.debug("Successfully applied 'JCE unlimited Strenght Policy File' installation fix!");
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            LOG.warn("Cannot apply JCE unlimited Strength Policy Files fix: {}", ex.getMessage());
        }
    }


    /**
     * MD5 (Message-Digest algorithm 5) is a widely used cryptographic hash
     * function with a 128-bit hash value, specified in RFC 1321, MD5 is one
     * in a series of message digest algorithms designed by Professor Ronald Rivest
     * of MIT (Rivest, 1994). MD5 has been employed in a wide variety of security
     * applications, and is also worldwide used to check the integrity of files.
     * @param input text utf-8
     * @return MD5 hash (128 hash bits) by delegating to the MD5 MessageDigest.
     */
    public static String toMD5(String input) {
        HashFunction md = Hashing.md5();
        return md.hashString(input, StandardCharsets.UTF_8).toString();
    }

    /**
     * SHA-1 is a cryptographic hash function designed by the United States National Security Agency.
     * @param input text utf-8
     * @return produces a 160-bit (20-byte) hash value known as a message digest
     */
    public static String toSHA1(String input) {
        HashFunction sha1 = Hashing.sha1();
        return sha1.hashString(input, StandardCharsets.UTF_8).toString();
    }

    /**
     * SHA-256.
     * Message Size (bits): 2^64
     * Block Size (bits): 512
     * Word Size (bits): 32
     * Message Digest Size (bits): 256
     * @param input text utf-8
     * @return generates an almost-unique, fixed size 256-bit (32-byte) hash.
     */
    public static String toSHA256(String input) {
        HashFunction sha1 = Hashing.sha256();
        return sha1.hashString(input, StandardCharsets.UTF_8).toString();
    }


    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String toHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    public static class Base64 {

        public static java.util.Base64.Decoder DECODER = java.util.Base64.getUrlDecoder();
        public static java.util.Base64.Encoder ENCODER = java.util.Base64.getUrlEncoder();
        private static Charset UTF8 = StandardCharsets.UTF_8;

        public static byte[] decode(byte[] src) {
            return DECODER.decode(src);
        }

        public static byte[] decode(String src) {
            return DECODER.decode(src.getBytes(UTF8));
        }

        public static String decode2String(byte[] src) {
            return new String(DECODER.decode(src), UTF8);
        }

        public static String decode2String(String src) {
            return new String(DECODER.decode(src.getBytes(UTF8)), UTF8);
        }

        public static byte[] encode(byte[] src) {
            return ENCODER.encode(src);
        }

        public static byte[] encode(String src) {
            return ENCODER.encode(src.getBytes(UTF8));
        }

        public static String encode2String(byte[] src) {
            return new String(ENCODER.encode(src), UTF8);
        }

        public static String encode2String(String src) {
            return new String(ENCODER.encode(src.getBytes(UTF8)), UTF8);
        }
    }
}
