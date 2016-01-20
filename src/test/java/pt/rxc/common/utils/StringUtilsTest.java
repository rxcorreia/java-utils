package pt.rxc.common.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.rxc.commmon.utils.StringUtils;

/**
 * Test class for {@link StringUtils}.
 * 
 * @author ruben.correia
 * 
 */
public class StringUtilsTest {
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testReplaceExtendedCharacters() {
    Assert.assertEquals("cCAAAAEEEIIIOOOO", StringUtils.replaceExtendedCharacters("çÇÀÁÃÄÈÉËÌÍÏÒÓÕÖ", false));
    Assert.assertEquals("ABC abc __||!! CcAaAaAaAaAa !#$%&/)=?",
        StringUtils.replaceExtendedCharacters("ABC abc __||!! ÇçÃãÁáÀàÂâÄä !#$%&/)=?", false));
  }

  @Test
  public void testCleanSpecialCharacters() {
    Assert.assertEquals("ABCDEF", StringUtils.cleanSpecialCharacters("ABÇDEF"));
    Assert.assertEquals("cCAAAAEEEIIIOOOO", StringUtils.cleanSpecialCharacters("çÇÀÁÃÄÈÉËÌÍÏÒÓÕÖ"));

    Assert.assertEquals("A_A", StringUtils.cleanSpecialCharacters("A!´~A"));
    Assert.assertEquals("AA", StringUtils.cleanSpecialCharacters("A!´~A", ""));
    Assert.assertEquals("AABBCC", StringUtils.cleanSpecialCharacters("'AA'BB'CC'", ""));
    Assert.assertEquals("AA.BB.CC", StringUtils.cleanSpecialCharacters("'AA'BB'CC'", "."));
    Assert.assertEquals(".AA.BB.CC.", StringUtils.cleanSpecialCharacters("'AA'BB'CC'", ".", false));
  }

  @Test
  public void testSearchLines() {
    String searchLineTxt = "ABC\nDEF\n GHI";

    Assert.assertEquals("ABC", StringUtils.searchLine(searchLineTxt, "A"));
    Assert.assertEquals("DEF", StringUtils.searchLine(searchLineTxt, "D"));
    Assert.assertEquals(null, StringUtils.searchLine(searchLineTxt, "GHI"));

    Assert.assertEquals("ABC", StringUtils.searchLine(searchLineTxt, "A", true));
    Assert.assertEquals("DEF", StringUtils.searchLine(searchLineTxt, "D", true));
    Assert.assertEquals("GHI", StringUtils.searchLine(searchLineTxt, "GHI", true));
    Assert.assertEquals(null, StringUtils.searchLine(searchLineTxt, "GHI", false));

    Assert.assertEquals(null, StringUtils.searchLine(null, "GHI"));
  }

  @Test
  public void testEqualsIgnoreCaseAndAccent() {
    String str1 = "ÃÁÀÄÉÈË";
    String str2 = "AAAAEEE";

    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndAccent(str1, str2));
    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndAccent(str1, str1));
    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndAccent(str2, str2));
    Assert.assertFalse(StringUtils.equalsIgnoreCaseAndAccent(str1, null));
    Assert.assertFalse(StringUtils.equalsIgnoreCaseAndAccent(null, str1));
    Assert.assertTrue(StringUtils.equalsIgnoreCaseAndAccent(null, null));
  }
}
