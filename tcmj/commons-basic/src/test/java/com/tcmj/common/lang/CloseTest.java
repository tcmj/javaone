package com.tcmj.common.lang;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Test of com.tcmj.common.lang.Close.
 * @author tcmj - Thomas Deutsch
 */
public class CloseTest {

    @Test
    public void testInSilenceWithoutThrowingException() {
        StringReader reader = new StringReader("abc");
        Close.inSilence(reader);
        try {
            reader.ready();
            fail("The IOException has not been thrown! This happens only if closing fails!");
        } catch (IOException e) {
        }
    }

    @Test
    public void testInSilenceThrowingAException() {
        Close.inSilence(() -> {
            throw new IOException("We choose a checked one!");
        });
    }

    @Test
    public void testInSilenceNullCase() {
        Close.inSilence(null);
    }

    @Test
    public void testUncheckedNullCase() {
        Close.unchecked(null);
    }

    @Test
    public void testUncheckedWithoutThrowingException() {
        StringReader reader = new StringReader("abc");
        Close.unchecked(reader);
        try {
            reader.ready();
            fail("The IOException has not been thrown! This happens only if closing fails!");
        } catch (IOException e) {
        }
    }

    @Test(expected = IOException.class)
    public void testUncheckedThrowingACheckedException() {
        Close.unchecked(() -> {
            throw new IOException("We choose a checked one!");
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUncheckedThrowingARuntimeException() {
        Close.unchecked(() -> {
            throw new IllegalArgumentException("We choose a unchecked one!");
        });
    }

}
