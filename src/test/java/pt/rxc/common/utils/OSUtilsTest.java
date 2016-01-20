package pt.rxc.common.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.rxc.commmon.utils.OSUtils;
import pt.rxc.commmon.utils.StringUtils;
import pt.rxc.commmon.utils.OSUtils.OSType;

/**
 * Test class for {@link StringUtils}.
 * 
 * @author ruben.correia
 * 
 */
public class OSUtilsTest {

  @Before
  public void setUp() throws Exception {
    new OSUtils();
  }

  @Test
  public void testGetters() {
    Assert.assertNotNull(OSUtils.getExtendedSystemInfo());
    Assert.assertNotNull(OSUtils.getSystemInfo());
    Assert.assertNotNull(OSUtils.getLocale());
    Assert.assertNotNull(OSUtils.getOperationSystemType());
    Assert.assertNotNull(OSUtils.getHostname());

    Assert.assertNotNull(OSUtils.isMac());
    Assert.assertNotNull(OSUtils.isSolaris());
    Assert.assertNotNull(OSUtils.isUnix());
    Assert.assertNotNull(OSUtils.isWindows());

    Assert.assertEquals(OSUtils.isWindows(), OSUtils.getOperationSystemType().equals(OSType.WINDOWS));
    Assert.assertEquals(OSUtils.isMac(), OSUtils.getOperationSystemType().equals(OSType.MAC));
    Assert.assertEquals(OSUtils.isSolaris(), OSUtils.getOperationSystemType().equals(OSType.SOLARIS));
    Assert.assertEquals(OSUtils.isUnix(), OSUtils.getOperationSystemType().equals(OSType.UNIX));
  }
}
