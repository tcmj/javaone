package com.tcmj.common.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import com.tcmj.common.text.RandomStrings;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Tests and Usage of class {@link com.tcmj.common.crypto.AES }
 * <p>There's a extended debugging mode which can be activated using jvm parameter: 'java.security.debug'</p>
 */
public class AESTest {

    /** Simple example to encrypt and decrypt some data. */
    @Test
    public void normalUsage() throws Exception {
        //we want to encrypt some data....
        byte[] data2Encrypt = "dear diary, yesterday i killed a fly!".getBytes("UTF-8");
        char[] password = "aG§s5Srt234!MyPassword!!".toCharArray(); //passwords should be held in character arrays, not String's

        //start encryption by creating an AES object...
        AES encryptor = new AES();
        //first we need a salt... we choose to generate a new random one (but we also can use any existing)
        byte[] salt = encryptor.generateSalt();
        //next we create the AES key by using the password and salt (this step is the same for en- and decrpytion)
        SecretKey key = encryptor.generateKey(password, salt);

        //Now we can apply encryption ... by creating a new IV (initialisation vector)
        byte[] encryptedA = encryptor.encrypt(key, salt, data2Encrypt); //keep in mind that we can also choose an existing IV

        //the simplest way to decrypt our data is:
        AES decryptor = new AES(); //(even we could use the same AES object for encryption and decryption)
        byte[] decrypted = decryptor.decrypt(password, encryptedA);
        //we only have to convert our byte array back to String:
        System.out.println(new String(decrypted, "UTF-8"));

        //and heres the proof:
        assertThat(decrypted, equalTo(data2Encrypt));
    }


    /** Extended example to encrypt and decrypt with customized values. */
    @Test
    public void extendedUsage() throws Exception {

        //We need the JCE installation to unlock AES 256 !
        Crypto.removeCryptographyRestrictions();

        byte[] data2Encrypt = ("Much much much much much much much much much much much much much much much much " +
                "much much much much much much much much much much much much much much much much much much much much " +
                "much much much much much much much much much much much much more data this Time!").getBytes("UTF-8");
        char[] password = "Ultra3%§Long325PASSWORD§$%%$&&%$&$%&jfakldijkvjw343243-32j4k32j4j.43j!!!!!!!!".toCharArray();

        int iterations = 100;

        AES encryptor = new AES(AES.Defaults.KEY_SIZE_256_BITS, AES.Defaults.DEFAULT_SALT_SIZE * 2, iterations);
        System.out.println("New encryptor object created: " + encryptor);

        byte[] salt = new byte[AES.Defaults.DEFAULT_SALT_SIZE * 2];
        System.arraycopy(encryptor.generateSalt(), 0, salt, 0, AES.Defaults.DEFAULT_SALT_SIZE);
        System.arraycopy(encryptor.generateSalt(), 0, salt, AES.Defaults.DEFAULT_SALT_SIZE, AES.Defaults.DEFAULT_SALT_SIZE);
        System.out.println("Salt           : " + Crypto.toHex(salt) + " length: " + salt.length + " byte");

        SecretKey key = encryptor.generateKey(password, salt);
        System.out.println("Key            : " + Crypto.toHex(key.getEncoded()) + " Algo: " + key.getAlgorithm());

        byte[] encrypted = encryptor.encrypt(key, salt, data2Encrypt);
        System.out.println("Encryption     : " + Crypto.toHex(encrypted));

        //Now we start again to simulate another jvm at another time in another world
        AES decryptor = new AES(AES.Defaults.KEY_SIZE_256_BITS, AES.Defaults.DEFAULT_SALT_SIZE * 2, iterations);
        byte[] theSalt = decryptor.extractSalt(encrypted);
        byte[] theIV = decryptor.extractIV(encrypted);
        byte[] theCipherText = decryptor.extractEncryptedData(encrypted);
        System.out.println("Salt           : " + Crypto.toHex(theSalt));
        System.out.println("IV             : " + Crypto.toHex(theIV));
        System.out.println("CipherText     : " + Crypto.toHex(theCipherText));

        System.out.println("New decryptor object created: " + decryptor);
        byte[] decrypted = decryptor.decrypt(password, theSalt, theIV, theCipherText);
        System.out.println("DeCryption: " + Crypto.toHex(decrypted));
        System.out.println("DeCryption length: " + decrypted.length);

        //we only have to convert our byte array back to String:
        System.out.println(new String(decrypted, "UTF-8"));

        //and heres the proof:
        assertThat(decrypted, equalTo(data2Encrypt));
    }


    /** Checks for the JCE (JCE jurisdiction policy files). */
    @Test
    public void checkMaxAllowedKeySize() {
        //Standard Java installation (Standard JCE)
        assertThat("Check AES keysize 128", AES.isKeySizeAllowed(AES.Defaults.KEY_SIZE_128_BITS), is(true)); //this is the only allowed one by default
        assertThat("Check AES keysize 192", AES.isKeySizeAllowed(AES.Defaults.KEY_SIZE_192_BITS), is(false)); //valid but locked
        assertThat("Check AES keysize 256", AES.isKeySizeAllowed(AES.Defaults.KEY_SIZE_256_BITS), is(false)); //valid but locked

        //Extended Java installation (JCE jurisdiction policy files)
        Crypto.removeCryptographyRestrictions();

        //..now we can also use 192 and 256 bit key size for AES
        assertThat("Check AES keysize 128", AES.isKeySizeAllowed(128), is(true));
        assertThat("Check AES keysize 192", AES.isKeySizeAllowed(192), is(true));
        assertThat("Check AES keysize 256", AES.isKeySizeAllowed(256), is(true));
    }


    /** We want to test some special cases to ensure that we get exactly the same after decrypting our encrypted data. */
    @Test
    public void encryptDecrpyt() throws Exception {
        encryptDecrpyt("a");    //single lower case char should stay the same
        encryptDecrpyt("A");    //..same test with a single upper case char
        encryptDecrpyt("Just some random Text and Numbers 1 2 3 4");
        encryptDecrpyt("AVeryLongText " + StringUtils.repeat(new RandomStrings().randomWordCapitalized(500), 100));
        encryptDecrpyt("Special Characters: !\"§$%&/()=?`´ 28°C <> || ;,:.-_ *' ÖÜÄöüä~");
        encryptDecrpyt("multiline\r\nmultiline\r\nmultiline\nmultiline\n");
    }

    /** Helper method to test if input data is the same after encryption - decryption. */
    private static void encryptDecrpyt(String text) throws Exception {
        char[] password = "avzh5F3§v bfh%ASt35!".toCharArray();
        AES pbe = new AES();
        byte[] salt = pbe.generateSalt();
        String result = new String(pbe.decryptConcatedData(password, pbe.encrypt(pbe.generateKey(password, salt), salt, text.getBytes("UTF-8"))), "UTF-8");
        System.out.println(result);
        assertThat("Equality Problem for: " + text, result, equalTo(text));
    }

    @Test
    public void testDecrypt() throws Exception {
        String ciphertext = "FVAZTZoZelJ5muNhE96TCmiQZhwgFuZvkrwxwo8PqOtL8q79BSxTj5WGgXJ_p5Zqkkpu8vABrCrK_XKevuOynT7PkD-QQzxu2_tF7KSjeoa53XaW5Pw2GWKuP3-YZ9wid4hDHwzrVw_QmlyS5U6i_sjoFQIeETZkhC6t-vJQdn0="; //PBKDF2WithHmacSHA512
        char[] password = "avzh5F3§v bfh%ASt35!".toCharArray();
        byte[] cipherbytes = Crypto.Base64.decode(ciphertext);
        AES aes = new AES(256, 16, AES.Defaults.DEFAULT_ITERATIONS);
        byte[] bytes = aes.decryptConcatedData(password, cipherbytes);
        String decrypt = new String(bytes);
        System.out.println("Decrypted Text: " + decrypt);
        String wewant = "This is the text we want to encrypt! The more i write the larger the encryption will get!";
        assertThat("Equality!", decrypt, equalTo(wewant));
    }


    @Test
    public void testLoop() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();
        char[] password = "a89))((zz66".toCharArray();
        RandomStrings rand = new RandomStrings();
        for (int i = 0; i <= 3; i++) {
            String word = rand.randomWordCapitalized(i);
            String base = AES.encrypt2Base64(password, word.getBytes());
            //System.out.println("Base64: " + base + " Plain: " + word);
        }
        watch.stop();
        System.out.println("Time: " + watch.toString());
    }

    @Test
    public void testPerformantEncrpytionAndDecryption() throws Exception {
        //encrypt mass data
        StopWatch watch = new StopWatch();
        RandomStrings rand = new RandomStrings();
        int loops = 1000;
        Map<String, String> datastore = new HashMap<>();
        //given:
        char[] password = "our!Pass3ord!!!".toCharArray();
        AES aes = new AES();

        System.out.println("Start encrypting " + loops + " pieces using the same key but different IVs");
        watch.start();
        byte[] salt = aes.generateSalt();
        SecretKey crypKey = aes.generateKey(password, salt);

        for (int i = 1; i <= loops; i++) {
            String word = rand.randomWordCapitalized(100);
            byte[] encrypt = aes.encrypt(crypKey, salt, word.getBytes());
            String hex = Crypto.toHex(encrypt);
            datastore.put(word, hex);
        }
        watch.stop();
        System.out.println("Encryption Time: " + watch.toString() + " (of " + loops + " loops)");

        System.out.println("Start decrypting " + loops + " pieces using the same key but individual IV");
        watch.reset();
        watch.start();
        AES aes256 = new AES();

        SecretKey key = null;

        for (Map.Entry<String, String> entry : datastore.entrySet()) {
            byte[] data = Crypto.hexStringToByteArray(entry.getValue());
            if (key == null) {
                key = aes256.generateKey(password, aes256.extractSalt(data));
            }

            byte[] bytes = aes256.decrypt(key, aes256.extractIV(data), aes256.extractEncryptedData(data));
            String result = new String(bytes, "UTF-8");
            assertThat(result, equalTo(entry.getKey()));

        }
        watch.stop();
        System.out.println("Decryption Time: " + watch.toString() + " (of " + loops + " loops)"); //03:30 - loop:1000

    }

    @Test
    public void testStreamEncryption() throws Exception {

        String toEncrypt = "This could also be read from an FileInputStream!";
        char[] password = "PassWoord!!".toCharArray();

        byte[] result = null;
        try (InputStream in = new ByteArrayInputStream(toEncrypt.getBytes("UTF-8"));
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            AES.encryptStream(password, in, out);
            result = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (InputStream inD = new ByteArrayInputStream(result);
             ByteArrayOutputStream outD = new ByteArrayOutputStream()) {
            AES.decryptStream(password, inD, outD);
            assertThat("En-De-Cryption failed!", new String(outD.toByteArray()), equalTo(toEncrypt));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSameSaltAndIvProducesSameCipherText() throws Exception {
        System.out.println("Default encoding: " + Charset.defaultCharset());
        char[] password = "super space containing password".toCharArray();
        String plain = "Claudia";
        AES aes = new AES();
        IvParameterSpec iv = AES.generateIV();
        byte[] salt = aes.generateSalt();
        SecretKey key = aes.generateKey(password, salt);
        System.out.println("Encrypting with same key: " + Crypto.toHex(key.getEncoded()) + " and salt: " + Crypto.toHex(salt) + " and iv: " + Crypto.toHex(iv.getIV()));
        for (int i = 1; i <= 5; i++) {
            byte[] encrypted = aes.encrypt(key, salt, iv, plain.getBytes());
            String plainAgain = new String(aes.decryptConcatedData(password, encrypted));
            System.out.println(Crypto.toHex(encrypted) + " Plain: " + plainAgain);
            assertThat(plainAgain, equalTo(plain));
        }
    }

    @Test
    public void testToString() throws NoSuchPaddingException, NoSuchAlgorithmException {
        AES aes = new AES();
        String summary = aes.toString();
        System.out.println(summary);
    }


}
