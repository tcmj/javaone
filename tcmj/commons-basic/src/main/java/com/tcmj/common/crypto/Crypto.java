package com.tcmj.common.crypto;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;

public class Crypto {

    /**
     * MD5 (Message-Digest algorithm 5) is a widely used cryptographic hash
     * function with a 128-bit hash value, specified in RFC 1321, MD5 is one
     * in a series of message digest algorithms designed by Professor Ronald Rivest
     * of MIT (Rivest, 1994). MD5 has been employed in a wide variety of security
     * applications, and is also worldwide used to check the integrity of files.
     * @param input text utf-8
     * @return MD5 hash (128 hash bits) by delegating to the MD5 MessageDigest.
     */
    public static String getMD5(String input) {
        HashFunction md = Hashing.md5();
        return md.hashString(input, StandardCharsets.UTF_8).toString();
    }

    /**
     * SHA-1 is a cryptographic hash function designed by the United States National Security Agency.
     * @param input text utf-8
     * @return produces a 160-bit (20-byte) hash value known as a message digest
     */
    public static String getSHA1(String input) {
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
    public static String getSHA256(String input) {
        HashFunction sha1 = Hashing.sha256();
        return sha1.hashString(input, StandardCharsets.UTF_8).toString();
    }

}
