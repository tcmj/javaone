package com.tcmj.common.crypto;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import com.tcmj.common.lang.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tcmj.common.crypto.AES.Defaults.DEFAULT_ITERATIONS;
import static com.tcmj.common.crypto.AES.Defaults.DEFAULT_SALT_SIZE;
import static com.tcmj.common.crypto.AES.Defaults.KEY_SIZE_128_BITS;

/**
 * Implementation of the <b>A</b>dvanced <b>E</b>ncryption <b>S</b>tandard (AES), also known as Rijndael.
 * AES is a symmetric-key algorithm, meaning the same key is used for both, encrypting and decrypting data.
 * Symmetric means that for the same input, it will always generate the same output.
 * AES was published by NIST (National Institute of Standards and Technology) as FIPS PUB 197 in November 2001.
 * AES is known as a successor of the DES algorithm.
 * <ul><li>KeySize: 128, 192 or 256 Bit</li>
 * <li>BlockSize: 128 Bit fixed</li>
 * <li>Structure: Substitution-permutation network</li></ul>
 * <a href="https://en.wikipedia.org/wiki/Advanced_Encryption_Standard">AES on Wikipedia</a>
 * <p/>
 * <b>Summary</b>
 * <ul>
 * <li>Encoding can be applied to any couple of bytes (e.g. text or file).</li>
 * <li>First you have to choose a key size (128/192/256 bit) and the amount of iterations.and pass it to the constructor. </li>
 * <li>Encrypting with 192 or 256 bits needs the installation of 'JCE unlimited Strength Policy Files'</li>
 * <li>The empty constructor defaults to a key size of 128 bits and a default amount of password iterations</li>
 * <li>There's a unofficial way to get over the key size limitation by using {@link Crypto#removeCryptographyRestrictions()}</li>
 * <li>The key will be created using a password and a salt and a predefined number of iterations</li>
 * <li>Encryption also needs a so called initialisation vector (IV)</li>
 * <li>The IV will be created randomly or you can set it as a parameter on the encrypt method</li>
 * <li>The result of encryption is a byte array consisting of the salt, iv and the cipherbytes</li>
 * <li>The recipient needs to know the password, the salt, the amount of iterations, the IV and of course the encrypted bytes</li>
 * <li>The recipient generates the key in exactly the same way using the same password, salt and iteration amount</li>
 * </ul>
 * Have a look on the JUnit test for detailed examples to use this class.
 * @author tcmj - Thomas Deutsch
 * @since 2.15.08
 */
public class AES {

    /** Default values which can be used to change AES encryption. */
    static class Defaults {
        /** Amount of iterations used for key generation. */
        public static final int DEFAULT_ITERATIONS = 65536;
        /** Length of the Salt to be used in bytes. 16 bytes = 128 bit */
        public static final int DEFAULT_SALT_SIZE = 16;
        /** Constat value for a 16 byte - 128 bit AES key. */
        public static final int KEY_SIZE_128_BITS = 128;
        /** Constat value for a 24 byte - 192 bit AES key. */
        public static final int KEY_SIZE_192_BITS = 192;
        /** Constat value for a 32 byte - 256 bit AES key. */
        public static final int KEY_SIZE_256_BITS = 256;
    }

    /** slf4j Logging Framework. */
    private static final Logger LOG = LoggerFactory.getLogger(AES.class);

    /** Cipher transformation : AES */
    private static final String CIPHER_TRANSFORM_AES = "AES";

    /** AES specification: Cipher Block Chaining Mode (CBC), Padding needed beause of fixed block length */
    private static final String CIPHER_SPEC = "AES/CBC/PKCS5Padding";

    /** Key derivation specification - changing will break existing streams! */
    private static final String KEYGEN_SPEC = "PBKDF2WithHmacSHA512";

    /** Random number algorithm used by the generate Salt and IV methods. */
    private static final String RND_NUM_ALGORITHM = "SHA1PRNG";

    /** Length of the Initialization Vector in bytes. 16 bytes = 128 bit. FIXED for AES! */
    private static final int FIXED_AES_BLOCK_SIZE = 16;


    /** Key factory used to create key specification objects. */
    private final SecretKeyFactory factory;

    /** Main object used for en- and decoding. */
    private final Cipher cipher;

    /** Must be 128, 192 or 256 Bit! */
    private final int keySize;

    /** We need the salt size for our concatenations! */
    private final int saltSize;

    /** Iterations used to generate the key! */
    private int pwdIterations;


    /** The AES key size. Valid values are 128, 192 or 256 bits. */
    public int getKeySize() {
        return keySize;
    }
    /** Salt size in Bytes.*/
    public int getSaltSize() {
        return saltSize;
    }


    /**
     * AES with {@link Defaults#KEY_SIZE_128_BITS} bit key size
     * using {@link Defaults#DEFAULT_ITERATIONS} password iterations.
     */
    public AES() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this(KEY_SIZE_128_BITS, DEFAULT_SALT_SIZE, DEFAULT_ITERATIONS);
    }

    /**
     * Constructor with...
     * @param keySize must be {@link Defaults#KEY_SIZE_128_BITS}, {@link Defaults#KEY_SIZE_192_BITS} or {@link Defaults#KEY_SIZE_256_BITS}
     */
    public AES(final int keySize, final int saltSize, final int passIterations) throws NoSuchAlgorithmException, NoSuchPaddingException {
        Objects.ensure(keySize == 128 || keySize == 192 || keySize == 256, "KeySize must be 128, 192 or 256 (bits)");
        this.keySize = keySize;
        this.saltSize = saltSize;
        this.pwdIterations = Objects.notNull(passIterations, "Password iterations be one of {}", keySize);
        factory = SecretKeyFactory.getInstance(KEYGEN_SPEC);
        cipher = Cipher.getInstance(CIPHER_SPEC);
    }


    /** Check the actually allowed key size for AES depending on the installed jurisdiction policy files (JCE). */
    public static boolean isKeySizeAllowed(int sizeToCheck) {
        try {
            Objects.ensure(sizeToCheck == 128 || sizeToCheck == 192 || sizeToCheck == 256, "KeySize must be 128, 192 or 256 (bits)");
            int maxKeyLen = Cipher.getMaxAllowedKeyLength(CIPHER_TRANSFORM_AES);
            LOG.debug("The max allowed key length for {} is {}!", CIPHER_TRANSFORM_AES, maxKeyLen);
            return sizeToCheck <= maxKeyLen;
        } catch (IllegalStateException ise) {
            LOG.debug("Invalid keysize '{}'! {}", sizeToCheck, ise.getMessage());
            return false;
        } catch (NoSuchAlgorithmException e) {
            LOG.debug("Cannot determine max allowed key length because of missing provider for '{}'!", CIPHER_TRANSFORM_AES);
            return false;
        }
    }


    /**
     * Generate a new pseudorandom salt of the specified length (bytes) using SecureRandom and SHA1PRNG.
     * <p>The length of the salt is 16 bytes (128 bit) - size should be same as block size <p/>
     */
    public byte[] generateSalt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[getSaltSize()];
        SecureRandom.getInstance(RND_NUM_ALGORITHM).nextBytes(salt);
        return salt;
    }

    /**
     * Generate a new pseudorandom salt of the specified length (bytes) using SecureRandom and SHA1PRNG.
     * <p>The length of the salt is 16 bytes (128 bit) - size should be same as block size <p/>
     */
    public static byte[] generateSalt(int sizeInBytes) throws NoSuchAlgorithmException {
        byte[] salt = new byte[sizeInBytes];
        SecureRandom.getInstance(RND_NUM_ALGORITHM).nextBytes(salt);
        return salt;
    }

    /** Generate a new pseudorandom initialisation vector in block size length (16 bytes / 128 bits) using SecureRandom and SHA1PRNG. */
    public static IvParameterSpec generateIV() throws NoSuchAlgorithmException {
        byte[] iv = new byte[FIXED_AES_BLOCK_SIZE];
        SecureRandom.getInstance(RND_NUM_ALGORITHM).nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /** Generates a AES key in the wanted size, using a given password and salt and the default amount of password iterations. */
    public SecretKey generateKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        return generateKey(password, salt, getPwdIterations());
    }

    /**
     * Generates a AES key in the wanted size, using a given password and salt and the specified amount of iterations.
     * <p>Watch out because this method can become a bottleneck! Tunning can be done e.g. by decreasing iterations</p>
     */
    public SecretKey generateKey(char[] password, byte[] salt, int iterations) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {

        Objects.notNull(password, "Password not set for key generation!");
        Objects.notNull(salt, "Salt not set for key generation!");

        //Generating 128/192/256-bit key using 16-byte salt doing n iterations
        KeySpec spec = new PBEKeySpec(password, salt, iterations, getKeySize());

        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        SecretKey secretKey = new SecretKeySpec(keyBytes, CIPHER_TRANSFORM_AES);

        return secretKey;
    }

    public byte[] getInitialisationVector(Cipher cipher) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidParameterSpecException {
        AlgorithmParameters params = cipher.getParameters();
        return params.getParameterSpec(IvParameterSpec.class).getIV();
    }

    /* Decrypt the message, given derived key and initialization vector. */
    public byte[] decryptConcatedData(char[] password, byte[] concatedData) throws Exception {
        byte[] salt = extractSalt(concatedData);
        byte[] iv = extractIV(concatedData);
        byte[] data = extractEncryptedData(concatedData);
        return decrypt(password, salt, iv, data);
    }

    public byte[] decrypt(char[] password, byte[] salt, byte[] iv, byte[] ciphertext) throws Exception {
        SecretKey key = generateKey(password, salt);
        return decrypt(key, iv, ciphertext);
    }

    public byte[] decrypt(SecretKey key, byte[] iv, byte[] ciphertext) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(ciphertext);
        return decrypted;
    }


    /**
     * Encrypts data using the given key and salt.
     * <p/>
     * Fastest way to encrypt data!
     */
    public byte[] encrypt(SecretKey key, byte[] salt, byte[] plain) throws Exception {

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] iv = getInitialisationVector(cipher);

        byte[] encrypted = cipher.doFinal(plain);

        //put all things together
        byte[] concated = concat(salt, iv, encrypted);

        return concated;
    }

    public byte[] encrypt(SecretKey key, byte[] salt, IvParameterSpec iv, byte[] plain) throws Exception {

        //the iv byte array must be wrapped in an IvParameterSpec object: new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] encrypted = cipher.doFinal(plain);

        //put all things together
        byte[] concated = concat(salt, iv.getIV(), encrypted);

        return concated;
    }

    /**
     * Encrypts a stream of data. The encrypted stream consists of
     * [16byte salt] + [16 byte IV] + [n byte cipherdata]
     * a header followed by the raw AES data. The header is broken down as follows:<br/>
     * <ul>
     * <li><b>keyLength</b>: AES key length in bytes (valid for 16, 24, 32) (1 byte)</li>
     * <li><b>salt</b>: pseudorandom salt used to derive keys from password (16 bytes)</li>
     * <li><b>authentication key</b> (derived from password and salt, used to check validity of password upon decryption) (8 bytes)</li>
     * <li><b>IV</b>: pseudorandom AES initialization vector (16 bytes)</li>
     * </ul>
     * @param password password to use for encryption
     * @param input an arbitrary byte stream to encrypt
     * @param output stream to which encrypted data will be written
     * @todo keyLength key length to use for AES encryption (must be 128, 192, or 256)
     */
    public static void encryptStream(char[] password, InputStream input, OutputStream output) throws Exception {

        AES pbe = new AES(KEY_SIZE_128_BITS, DEFAULT_SALT_SIZE, DEFAULT_ITERATIONS);
        byte[] salt = pbe.generateSalt();
        SecretKey key = pbe.generateKey(password, salt);

        output.write(salt);

        pbe.cipher.init(Cipher.ENCRYPT_MODE, key); //we want a new IV
        output.write(pbe.getInitialisationVector(pbe.cipher)); //write IV

        // read data from input into buffer, encrypt and write to output
        byte[] buffer = new byte[1024];
        int numRead;
        byte[] encrypted = null;
        while ((numRead = input.read(buffer)) > 0) {
            encrypted = pbe.cipher.update(buffer, 0, numRead);
            if (encrypted != null) {
                output.write(encrypted);
            }
        }
        byte[] data = pbe.cipher.doFinal();
        if (data != null) {
            output.write(data);
        }


    }

    /**
     * ATM only 16 byte salts supported!
     * @param password
     * @param input
     * @param output
     * @throws Exception
     */
    public static void decryptStream(char[] password, InputStream input, OutputStream output) throws Exception {

        byte[] salt = new byte[DEFAULT_SALT_SIZE];
        input.read(salt);

        AES pbe = new AES(KEY_SIZE_128_BITS, DEFAULT_SALT_SIZE, DEFAULT_ITERATIONS);

        SecretKey key = pbe.generateKey(password, salt);

        byte[] iv = new byte[FIXED_AES_BLOCK_SIZE]; // 16-byte I.V. regardless of key size
        input.read(iv);

        pbe.cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        // read data from input into buffer, decrypt and write to output
        byte[] buffer = new byte[1024];
        int numRead;
        byte[] decrypted;
        while ((numRead = input.read(buffer)) > 0) {
            decrypted = pbe.cipher.update(buffer, 0, numRead);
            if (decrypted != null) {
                output.write(decrypted);
            }
        }

        decrypted = pbe.cipher.doFinal();

        if (decrypted != null) {
            output.write(decrypted);
        }


    }

    private byte[] concat(byte[] salt, byte[] iv, byte[] encrypted) {
        byte[] concated = new byte[salt.length + iv.length + encrypted.length];
        int pos = 0;
        System.arraycopy(salt, 0, concated, pos, salt.length); //copy the salt in the new array
        pos += salt.length;
        System.arraycopy(iv, 0, concated, pos, iv.length); //copy the iv in the new array
        pos += iv.length;
        System.arraycopy(encrypted, 0, concated, pos, encrypted.length); //copy the ciphertext in the new array
        return concated;
    }

    /**
     * AES-Encrypts the given bytes and produces a url compatible base 64 string.<br/>
     * The encryption will be performed with a new salt and a new iv.
     * @return base64 string containing salt, iv and encrypted data.
     */
    public static String encrypt2Base64(char[] password, byte[] toEncrpyt) throws Exception {
        AES pbe = new AES();
        byte[] salt = pbe.generateSalt();
        SecretKey crypKey = pbe.generateKey(password, salt);
        byte[] encrypted = pbe.encrypt(crypKey, salt, toEncrpyt);
        return Crypto.Base64.encode2String(encrypted);
    }

    /**
     * Decrypts a base64 encoded string containing a aes ciphertext
     * @param ciphertext salt + iv + encoded data
     * @return plain text as utf-8 string
     */
    public   String decryptBase64(char[] password, String ciphertext) throws Exception {
        byte[] cipherbytes = Crypto.Base64.decode(ciphertext);
        byte[] decryptData = decrypt(password, cipherbytes);
        return new String(decryptData, "UTF-8");
    }

    public byte[] decrypt(char[] password, byte[] cipherbytes) throws Exception {
        byte[] salt = extractSalt(cipherbytes);
        byte[] iv = extractIV(cipherbytes);
        byte[] encdata = extractEncryptedData(cipherbytes);
        return decrypt(password, salt, iv, encdata);
    }

    /** Extract the salt from an byte array. */
    public byte[] extractSalt(byte[] encrypted) {
        byte[] salt = new byte[getSaltSize()];
        System.arraycopy(encrypted, 0, salt, 0, getSaltSize());
        return salt;
    }

    /** Extract the initialization vector from an byte array. */
    public byte[] extractIV(byte[] encrypted) {
        byte[] iv = new byte[FIXED_AES_BLOCK_SIZE];
        System.arraycopy(encrypted, getSaltSize(), iv, 0, FIXED_AES_BLOCK_SIZE);
        return iv;
    }

    public byte[] extractEncryptedData(byte[] encrypted) {
        int length = encrypted.length - getSaltSize() - FIXED_AES_BLOCK_SIZE;
        byte[] data = new byte[encrypted.length - getSaltSize() - FIXED_AES_BLOCK_SIZE];
        System.arraycopy(encrypted, getSaltSize() + FIXED_AES_BLOCK_SIZE, data, 0, length);
        return data;
    }

    /** Getter for the amount of iterations used by {@link #generateKey(char[], byte[])} */
    public int getPwdIterations() {
        return pwdIterations;
    }

    /** Setter for the amount of iterations used by {@link #generateKey(char[], byte[])} */
    public void setPwdIterations(int pwdIterations) {
        this.pwdIterations = pwdIterations;
    }

    /**
     * [AES specification]+[KeySize]+[Key derivation specification ]+[Iterations]+[@hashCode]
     * @return AES/CBC/PKCS5Padding/128Bit/PBKDF2WithHmacSHA512/65536@45820e51
     */
    @Override
    public String toString() {
        return CIPHER_SPEC + "/" + keySize + "Bit/" + KEYGEN_SPEC + "/" + pwdIterations + "@" + Integer.toHexString(hashCode());
    }
}
