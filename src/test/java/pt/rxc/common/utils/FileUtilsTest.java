package pt.rxc.common.utils;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.rxc.commmon.utils.FileTypeEnum;
import pt.rxc.commmon.utils.FileUtils;
import pt.rxc.commmon.utils.PropertiesUtils;

/**
 * Test class for {@link PropertiesUtils}.
 * 
 * @author ruben.correia
 * 
 */
public class FileUtilsTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testFiletype() throws IOException {
    Assert.assertEquals(FileTypeEnum.TEXT, FileUtils.getFiletype("abc.txt"));
    Assert.assertEquals(FileTypeEnum.XML, FileUtils.getFiletype("abc.xml"));
    Assert.assertEquals(FileTypeEnum.XML, FileUtils.getFiletype("abc.wsdl"));
    Assert.assertEquals(FileTypeEnum.XML, FileUtils.getFiletype("abc.ism"));
    Assert.assertEquals(FileTypeEnum.XML, FileUtils.getFiletype("abc.ismc"));
    Assert.assertEquals(FileTypeEnum.TEXT, FileUtils.getFiletype("abc.asc"));

    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.mov"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.mp4"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.wmv"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.mkv"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.ts"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.m2ts"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.avi"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.mpg"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletype("abc.ismv"));

    Assert.assertEquals(FileTypeEnum.AUDIO, FileUtils.getFiletype("abc.mp3"));
    Assert.assertEquals(FileTypeEnum.AUDIO, FileUtils.getFiletype("abc.aiff"));
    Assert.assertEquals(FileTypeEnum.AUDIO, FileUtils.getFiletype("abc.wav"));
    Assert.assertEquals(FileTypeEnum.AUDIO, FileUtils.getFiletype("abc.wma"));
    Assert.assertEquals(FileTypeEnum.AUDIO, FileUtils.getFiletype("abc.isma"));

    Assert.assertEquals(FileTypeEnum.GENERIC, FileUtils.getFiletype("abc.bin"));

    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletypeFromExtension("mov"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletypeFromExtension("mp4"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletypeFromExtension("wmv"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletypeFromExtension("mkv"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletypeFromExtension("ts"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletypeFromExtension("m2ts"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletypeFromExtension("avi"));
    Assert.assertEquals(FileTypeEnum.VIDEO, FileUtils.getFiletypeFromExtension("mpg"));
  }

  @Test
  public void testGetFilepath() throws IOException {
    Assert.assertEquals("abc" + File.separator + "def" + File.separator + "ghi", FileUtils.getParent("abc/def/ghi/test.jpg"));
    Assert.assertEquals("", FileUtils.getParent("test.jpg"));
    Assert.assertEquals(null, FileUtils.getParent(null));
  }
}
