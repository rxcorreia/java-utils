package pt.rxc.common.utils;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.rxc.commmon.utils.PropertiesUtils;

/**
 * Test class for {@link PropertiesUtils}.
 * 
 * @author ruben.correia
 * 
 */
public class PropertiesUtilsTest {
  private static final String separator = ",";

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testGetPropertiesValue() throws IOException {
    String propertiesContent = "A=1,B=2,C=D,D=ABC";

    Assert.assertEquals("1", PropertiesUtils.getPropertiesValue(propertiesContent, separator, "A"));
    Assert.assertEquals("2", PropertiesUtils.getPropertiesValue(propertiesContent, separator, "B"));
    Assert.assertEquals("D", PropertiesUtils.getPropertiesValue(propertiesContent, separator, "C"));
    Assert.assertEquals("ABC", PropertiesUtils.getPropertiesValue(propertiesContent, separator, "D"));
  }
}
