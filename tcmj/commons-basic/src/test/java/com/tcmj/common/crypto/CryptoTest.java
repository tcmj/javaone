package com.tcmj.common.crypto;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * com.tcmj.common.crypto.Crypto
 * @author tcmj
 */
public class CryptoTest {

    @Test
    public void shouldCreateMD5() throws Exception {
        assertThat("1.SingleChar", Crypto.toMD5("a"), equalTo("0cc175b9c0f1b6a831c399e269772661"));
        assertThat("2.SingleWord", Crypto.toMD5("tcmj"), equalTo("20e28419f3d9fa2ca38b1608d3dfc2bf"));
        assertThat("3.Sentence", Crypto.toMD5("Free md5 hash calculator implemented in Java"), equalTo("8ac46ccae69e3cb01c5175ed12c7f58e"));
        assertThat("4.MultiLine", Crypto.toMD5("lineone\r\nlinetwo\r\nlinethree"), equalTo("7a7f1c42f15f3b66cd893f49ca4be122"));
    }

    @Test
    public void shouldCreateSHA1() throws Exception {
        assertThat("1.SingleChar", Crypto.toSHA1("a"), equalTo("86F7E437FAA5A7FCE15D1DDCB9EAEAEA377667B8".toLowerCase()));
        assertThat("2.SingleWord", Crypto.toSHA1("tcmj"), equalTo("17B01260D3A3ACFB220C9999C4DDE39082A4B4A7".toLowerCase()));
        assertThat("3.Sentence", Crypto.toSHA1("Free sha1 hash calculator implemented in Java"), equalTo("C393361E98821E63212D9BB84EA7B95C71802445".toLowerCase()));
        assertThat("4.MultiLine", Crypto.toSHA1("lineone\r\nlinetwo\r\nlinethree"), equalTo("acae3e171a7eeb22d922d8c384f32e7fe8e2dfd8"));
    }

    @Test
    public void shouldCreateSHA256() throws Exception {
        assertThat("1.SingleChar", Crypto.toSHA256("a"), equalTo("ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb"));
        assertThat("2.SingleWord", Crypto.toSHA256("tcmj"), equalTo("1bbb42e4d13cdc0ba7db32cce9f7c5384603e817ccb4d5b7b2b18a84f5289275"));
        assertThat("3.Sentence", Crypto.toSHA256("Free sha256 hash calculator implemented in Java"), equalTo("ba3d062f71594a1b6cc969804ce5a89fe696b5638407b6e3ef459f988758f548"));
        assertThat("4.MultiLine", Crypto.toSHA256("lineone\r\nlinetwo\r\nlinethree"), equalTo("a984eaf52e1eaf5cec727203cb9092d8f02f0ab294383b26d6031a93a3af29eb"));
    }

}
