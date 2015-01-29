package com.tcmj.common.text;

import com.tcmj.common.text.CamelCase;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import org.hamcrest.Matcher;

/**
 * JUnit CamelCase Test.
 *
 * @author tcmj
 */
public class CamelCaseTest {

    /**
     * slf4j Logging framework.
     */
    private static final transient Logger LOG = LoggerFactory.getLogger(CamelCaseTest.class);

    
    /** Internal helper method because of signature problems. */
    private void assertThat(String message, CharSequence result, Matcher<String> expected) {
        org.junit.Assert.assertThat(message,(String)result,expected);
    }
    
    /**
     * Test of toCamelCase method, of class CamelCase.
     */
    @Test
    public void toCamelCase() {
        LOG.info("Testing method toCamelCase(String)...");

        assertThat("under_my_skin should be UnderMySkin", CamelCase.toCamelCase("under_my_skin"), equalTo("UnderMySkin"));
        assertThat("FRAME_WORK should be FrameWork", CamelCase.toCamelCase("FRAME_WORK"), equalTo("FrameWork"));

        assertThat("normalizedword should be ", CamelCase.toCamelCase("normalizedword"), equalTo("Normalizedword"));
        assertThat("normalizedWord should be NormalizedWord", CamelCase.toCamelCase("normalizedWord"), equalTo("NormalizedWord"));
        assertThat("a b c should be ABC", CamelCase.toCamelCase("a b c"), equalTo("ABC"));
        assertThat("a_b_c should be ABC", CamelCase.toCamelCase("a_b_c"), equalTo("ABC"));
        assertThat("_underscoreword should be Underscoreword", CamelCase.toCamelCase("_underscoreword"), equalTo("Underscoreword"));
        assertThat("_un_der_score_word should be UnDerScoreWord", CamelCase.toCamelCase("_un_der_score_word"), equalTo("UnDerScoreWord"));
        assertThat(" _ un _ d er_score_word should be UnDErScoreWord", CamelCase.toCamelCase(" _ un _ d er_score_word"), equalTo("UnDErScoreWord"));

        assertThat("'' should be <input>", CamelCase.toCamelCase(""), equalTo(""));
        assertThat("null should be <input>", CamelCase.toCamelCase(null), nullValue(String.class));
        assertThat("' ' should be ''", CamelCase.toCamelCase(" "), equalTo(""));
    }

    /**
     * Test of toGetter method, of class CamelCase.
     */
    @Test
    public void toGetter() {
        LOG.info("Testing method toGetter(String)...");

        assertThat("input should be getInput", CamelCase.toGetter("input"), equalTo("getInput"));
        assertThat("version_id should be getVersionId", CamelCase.toGetter("version_id"), equalTo("getVersionId"));
        assertThat("JAVA_HOME should be getJavaHome", CamelCase.toGetter("JAVA_HOME"), equalTo("getJavaHome"));
    }

    /**
     * Test of toSetter method, of class CamelCase.
     */
    @Test
    public void toSetter() {
        LOG.info("Testing method toSetter(String)...");

        assertThat("codenames should be setCodenames", CamelCase.toSetter("codenames"), equalTo("setCodenames"));
        assertThat("last_name should be setLastName", CamelCase.toSetter("last_name"), equalTo("setLastName"));
        assertThat("first-name should be setFirstName", CamelCase.toSetter("first-name"), equalTo("setFirstName"));
    }
    
    /**
     * Test with {@link CharSequence}
     */
    @Test
    public void charSequenceTest() {
        
        String string = "co-ff_ee", result = "CoFfEe";
        
        assertThat("String", CamelCase.toCamelCase(string), equalTo("CoFfEe"));
        
        assertThat("StringBuilder", CamelCase.toCamelCase(new StringBuilder(string)), equalTo(result));
        
        assertThat("StringBuffer", CamelCase.toCamelCase(new StringBuffer(string)), equalTo(result));

        assertThat("CharBuffer", CamelCase.toCamelCase(CharBuffer.wrap(string)), equalTo(result));
        
        //No-NullPointerException-CharSequence-Test:
        CharSequence sb = null;
        CamelCase.toCamelCase(sb);
    }
    

}
