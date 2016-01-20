package pt.rxc.commmon.utils;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * File-related utility methods.
 *
 * @author ruben.correia
 */
public class FileUtils {

  /**
   * File read buffer size.
   */
  private static final int BUFFER_SIZE = 4096;

  /** The Microsoft Power Pont pptx Extension */
  public static final String PPTX_EXT = ".pptx";

  /** The Microsoft Word Docx Extension */
  public static final String WORD_DOCX_EXT = ".docx";

  /** The Microsoft Word Doc Extension */
  public static final String WORD_DOC_EXT = ".doc";

  /** The Microsoft Excel Extension */
  public static final String EXCEL_XLS_EXT = "xls";

  /** The Microsoft Excel XLSX Extension */
  public static final String EXCEL_XLSX_EXT = "xlsx";

  /** Microsoft Outlook MSG Extension */
  public static final String OUTLOOK_MSG_EXT = "msg";

  public static final String UNKNOWN_MIME_TYPE = "application/octet-stream";

  public static final String MIME_TYPE_APP_UNKNOWN = "application/unknown";

  private final static String specialChars = "„‚·‡‰√¬¿¡ƒÍÈËÎ …»ÀÓÌÏÔŒÕÃœıÙÛÚˆ’‘”“÷˚˙˘¸€⁄Ÿ‹Á«Ò—˝ˇ›";
  private final static String convertedChars = "aaaaaAAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuUUUUcCnNyyY";

  private static String fileSeparator = System.getProperty("file.separator");

  private static final Map<FileTypeEnum, List<String>> fileTypeExtensionMap = new HashMap<FileTypeEnum, List<String>>();

  static {
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "7z");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "apk");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "bzip2");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "bz2");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "cab");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "deb");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "gzip");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "ear");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "jar");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "rar");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "tar");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "war");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.ARCHIVE, "zip");

    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "aac");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "aif");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "aiff");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "au");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "flac");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "isma");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "m4a");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "mp3");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "wav");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "wma");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.AUDIO, "ast");

    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "bmp");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "gif");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "ico");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "jpg");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "jpeg");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "jng");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "pdn");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "png");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "psd");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "psp");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "tif");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.IMAGE, "tiff");

    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "doc");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "docx");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "dot");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "dotx");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "odt");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "ott");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "ppt");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "pptx");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "xls");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.OFFICE, "xlsx");

    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.PDF, "pdf");

    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "3gp");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "asf");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "avi");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "avchd");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "flv");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "ismv");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "m2t");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "m2ts");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "m4v");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "mkv");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "mov");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "mpe");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "mpeg");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "mpg");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "mp4");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "mxf");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "ogg");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "ts");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.VIDEO, "wmv");

    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.TEXT, "asc");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.TEXT, "ascii");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.TEXT, "log");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.TEXT, "txt");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.TEXT, "html");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.TEXT, "xhtml");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.TEXT, "srt");

    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.XML, "wsdl");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.XML, "xml");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.XML, "ism");
    MapUtils.addValue(fileTypeExtensionMap, FileTypeEnum.XML, "ismc");
  }

  public static File getLatestFile(String directory, boolean recursive) {
    if (StringUtils.isBlank(directory)) {
      return null;
    }

    return getLatestFile(new File(directory), recursive);
  }

  /**
   * Get latest file (< last modified date)
   * 
   * @param directory
   *          the directory to search
   * @param recursive
   *          indicate if we should search recursively
   * @return the latest file
   */
  public static File getLatestFile(File directory, boolean recursive) {
    File retObj = null;

    if (directory != null && directory.isDirectory()) {
      List<File> fileList = null;
      if (recursive) {
        fileList = listFiles(directory);
      } else {
        fileList = Arrays.asList(directory.listFiles());
      }

      for (File file : fileList) {
        if (file.isFile()) {
          if (retObj == null || retObj.lastModified() < file.lastModified()) {
            retObj = file;
          }
        }
      }
    }

    return retObj;
  }

  /**
   * List all files recursively in the given directory.
   * 
   * @param directory
   *          the directory to search
   * @return
   */
  public static List<File> listFiles(File directory) {
    List<File> retObj = new ArrayList<File>();

    if (directory != null && directory.isDirectory()) {
      for (File file : directory.listFiles()) {
        if (file.isDirectory()) {
          retObj.addAll(listFiles(file));
        } else {
          retObj.add(file);
        }
      }
    }

    return retObj;
  }

  /**
   * Get System 'file.separator' property value.
   * 
   * @return
   */
  public static String getFileSeparator() {
    return fileSeparator;
  }

  public static boolean writeObject(File f, Object obj) {
    boolean retObj = true;

    if (!f.getParentFile().isDirectory()) {
      f.getParentFile().mkdirs();
    }

    FileOutputStream fileOutputStream = null;
    ObjectOutputStream objectOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(f);
      objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(obj);
      objectOutputStream.close();
    } catch (IOException e) {
      retObj = false;
    } finally {
      if (fileOutputStream != null) {
        try {
          fileOutputStream.close();
        } catch (IOException e) {
        }
      }
      if (objectOutputStream != null) {
        try {
          objectOutputStream.close();
        } catch (IOException e) {
        }
      }
    }

    return retObj;
  }

  /**
   * Read an object from file.
   * 
   * @param f
   *          the file to get
   * @return
   */
  public static Object readObject(File f) {
    Object retObj = null;

    FileInputStream fileInputStream = null;
    ObjectInputStream objectInputStream = null;
    try {
      fileInputStream = new FileInputStream(f);
      objectInputStream = new ObjectInputStream(fileInputStream);
      retObj = objectInputStream.readObject();
      objectInputStream.close();
    } catch (ClassNotFoundException | IOException e) {
    } finally {
      if (objectInputStream != null) {
        try {
          objectInputStream.close();
        } catch (IOException e) {
        }
      }
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
        }
      }
    }

    return retObj;
  }

  /**
   * Calculate the speed of a transfer.
   * 
   * @param fileSize
   *          file size in bytes
   * @param startDate
   *          the start date
   * @param endDate
   *          the end date
   * @return the speed
   */
  public static String calculateSpeed(Long fileSize, Date startDate, Date endDate) {
    if (fileSize == null || startDate == null || endDate == null) {
      throw new IllegalArgumentException("File size, start and end date are mandatory!");
    }
    return calculateSpeed(fileSize, endDate.getTime() - startDate.getTime());
  }

  /**
   * Calculate the speed of a transfer.
   * 
   * @param fileSize
   *          file size in bytes
   * @param duration
   *          the duration in miliseconds
   * @return the speed
   */
  public static String calculateSpeed(Long fileSize, Long duration) {
    if (fileSize == null || duration == null) {
      throw new IllegalArgumentException("File size and duration are mandatory!");
    }

    String retObj = "N/A";

    if (fileSize > 0 && duration > 0) {
      Long speed = fileSize / (duration / 1000);
      retObj = StringUtils.convertFileSizeIntoHumanReadableFormat(speed) + "/s";
    }

    return retObj;
  }

  /**
   * Remove illegal characters from a string which represents a file.
   * 
   * @param filename
   *          the filename
   * @return the updated string
   */
  public static String removeIllegalCharacters(String filename) {
    if (filename != null && !filename.equals("")) {
      char[] beforeConversion = specialChars.toCharArray();
      char[] afterConversion = convertedChars.toCharArray();

      for (int i = 0; i < beforeConversion.length; i++) {
        filename = filename.replace(String.valueOf(beforeConversion[i]), String.valueOf(afterConversion[i]));
      }
      filename = filename.replace(" ", "_");

      String newFilename = "";
      for (int i = 0; i < filename.length(); i++) {
        char ch = filename.charAt(i);
        if (!(ch >= 'a' && ch <= 'z') && !(ch >= 'A' && ch <= 'Z') && !(ch >= '0' && ch <= '9') && ch != '_' && ch != '-'
            && ch != '.') {
          newFilename += "-";
        } else {
          newFilename += ch;
        }
      }
      filename = newFilename;
    }

    return filename;
  }

  /**
   * Default constructor. This class should not be instantiated.
   */
  protected FileUtils() {
  }

  public static void processDir(File dir) {
    if (dir != null && dir.listFiles() != null) {
      for (File file : dir.listFiles()) {
        if (file != null) {
          if (file.isDirectory()) {
            processDir(file);
          } else {
            if (file.getName() != null && file.getName().contains("SITE_WEB")) {
              file.delete();
            }
          }
        }
      }
    }
  }

  /**
   * Get the file lines of the given file.
   * 
   * @param filename
   *          the file to get lines
   * @return the list of lines
   * @throws IOException
   *           if some problem occur while reading the file
   */
  public static List<String> getFileLines(String filename) throws IOException {
    return getFileLines(new File(filename));
  }

  /**
   * Get the file lines of the given file.
   * 
   * @param filename
   *          the file to get lines
   * @return the list of lines
   * @throws IOException
   *           if some problem occur while reading the file
   */
  public static List<String> getFileLines(File file) throws IOException {
    List<String> lines = new ArrayList<String>();

    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
    } finally {
      if (br != null) {
        br.close();
      }
    }

    return lines;
  }

  /**
   * Get the content (as string) of the given file.
   * 
   * @param file
   *          the file to get the content
   * @return the content of the file as string
   * @throws IOException
   *           is some problem occur while reading from file.
   */
  public static String getFileContentAsString(File file, String encoding) throws IOException {
    return new String(getFileContentAsBytes(file), encoding);
  }

  /**
   * Get the content (as string) of the given file.
   * 
   * @param file
   *          the file to get the content
   * @return the content of the file as string
   * @throws IOException
   *           is some problem occur while reading from file.
   */
  public static String getFileContentAsString(File file) throws IOException {
    return getFileContentAsString(file, "UTF-8");
  }

  /**
   * Get the content of the given file (as byte[]).
   * 
   * @param file
   *          the file to get content.
   * @return the content of the file as byte[].
   * @throws IOException
   *           if some problem occur while reading file
   */
  public static byte[] getFileContentAsBytes(File file) throws IOException {
    if (file == null || !file.exists() || !file.canRead()) {
      throw new IllegalArgumentException("Can't read the given file: " + (file == null ? null : file.getAbsoluteFile()));
    }

    byte[] data = new byte[(int) file.length()];
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);

      int offset = 0;
      int readCharacteres = 0;
      while ((readCharacteres = fis.read(data, offset, fis.available() >= BUFFER_SIZE ? BUFFER_SIZE : fis.available())) > 0) {
        offset += readCharacteres;
      }
    } finally {
      if (fis != null) {
        fis.close();
      }
    }

    return data;
  }

  /**
   * Calculate the file digest. Supported algorithms are MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512.
   * 
   * @param file
   * @param algorithm
   *          The desired algorithm for creating the message digest.
   * @return
   * @throws IOException
   */
  public static String calculateFileDigest(File file, String algorithm) throws IOException {
    String retObj = null;
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      InputStream is = new FileInputStream(file);
      try {
        is = new DigestInputStream(is, md);
        byte[] buffer = new byte[8 * 1024];
        int numRead = -1;

        do {
          numRead = is.read(buffer);
          if (numRead > 0) {
            md.update(buffer, 0, numRead);
          }
        } while (numRead != -1);
      } finally {
        is.close();
      }

      retObj = (new HexBinaryAdapter()).marshal(md.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("Algorithm not supported: " + algorithm);
    }
    return retObj;
  }

  /**
   * Calculate the file digest. Supported algorithms are MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512.
   * 
   * @param file
   * @param algorithm
   *          The desired algorithm for creating the message digest.
   * @return
   * @throws IOException
   */
  public static String calculateFileHash(File file, String algorithm) throws IOException {
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      InputStream is = new FileInputStream(file);
      try {
        byte[] buffer = new byte[8 * 1024];
        int numRead = -1;

        do {
          numRead = is.read(buffer);
          if (numRead > 0) {
            md.update(buffer, 0, numRead);
          }
        } while (numRead != -1);
      } finally {
        is.close();
      }
      BigInteger bi = new BigInteger(1, md.digest());
      return bi.toString(16);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("Algorithm not supported: " + algorithm);
    }
  }

  /**
   * Calculate the file Cycle Redundancy Check.
   * 
   * @param file
   * @return
   * @throws IOException
   */
  public static String calculateFileCRC(File file) throws IOException {
    String retObj = null;

    // TODO Update API used to generate checksum
    // retObj = Files.getChecksum(file, new CRC32()) + "";
    return retObj;
  }

  /**
   * Copy a file from a given source to a given target file.
   * 
   * @param source
   *          the source file
   * @param target
   *          the target file
   * @throws IOException
   *           if some IO exception occur
   */
  public static int copyContent(File source, File target) throws IOException {
    if (source == null || !source.exists() || !source.isFile() || !source.canRead()) {
      throw new IllegalArgumentException("Can't read the given file: " + (source != null ? source.getAbsolutePath() : null));
    }
    if (target == null) {
      throw new IllegalArgumentException(
          "Can't write in the given location: " + (target != null ? target.getAbsolutePath() : null));
    }

    if (target.getParentFile() != null && !target.getParentFile().isDirectory()) {
      target.getParentFile().mkdirs();
    }

    int readBytes = 0;
    byte[] data = new byte[BUFFER_SIZE];
    FileInputStream fis = null;
    FileOutputStream fos = null;
    try {
      fis = new FileInputStream(source);
      fos = new FileOutputStream(target);

      int read = 0;
      while ((read = fis.read(data)) > 0) {
        readBytes += read;
        fos.write(data, 0, read);
        fos.flush();
      }
    } finally {
      if (fis != null) {
        fis.close();
      }
      if (fos != null) {
        fos.close();
      }
    }

    return readBytes;
  }

  /**
   * Write content into a file.
   * 
   * @param filepath
   *          the file path to write to.
   * @param content
   *          the content to be written into the given file.
   * @throws IOException
   *           if some error occurs while writing to file.
   */
  public static void writeFileContent(String filepath, byte[] content) throws IOException {
    writeFileContent(new File(filepath), content);
  }

  /**
   * Write content into a file.
   * 
   * @param f
   *          the file to write to.
   * @param content
   *          the content to be written into the given file.
   * @throws IOException
   *           if some error occurs while writing to file.
   */
  public static void writeFileContent(File f, byte[] content) throws IOException {
    if (f == null) {
      throw new IllegalArgumentException("No file information found.");
    }
    if (content == null) {
      throw new IllegalArgumentException("The content to write should not be null.");
    }

    if (f != null && content != null) {
      if (!f.getParentFile().isDirectory()) {
        f.getParentFile().mkdirs();
      }
      BufferedOutputStream bufferedOutputStream = null;
      FileOutputStream fileOutputStream = null;
      try {
        fileOutputStream = new FileOutputStream(f);
        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(content);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
      } finally {
        if (bufferedOutputStream != null) {
          try {
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
          } catch (Exception e) {
          }
        }
        if (fileOutputStream != null) {
          fileOutputStream.close();
        }
      }
    }
  }

  /**
   * Append text content to file.
   * 
   * @param filepath
   *          Path to file
   * @param content
   *          String content to be appended to file.
   * @throws IOException
   */
  public static void appendContent(String filepath, String content) throws IOException {
    appendContent(new File(filepath), content);
  }

  /**
   * Append text content to file.
   * 
   * @param filepath
   *          File to be appended.
   * @param content
   *          String content to be appended to file.
   * @throws IOException
   */
  public static void appendContent(File file, String content) throws IOException {
    appendContent(file, content, "UTF-8");
  }

  /**
   * Append text content to file, with encoding.
   * 
   * @param filepath
   *          Path to file
   * @param content
   *          String content to be appended to file.
   * @param encoding
   *          Encoding used for string content.
   * 
   * @throws IOException
   */
  public static void appendContent(String filepath, String content, String encoding) throws IOException {
    appendContent(new File(filepath), content, encoding);
  }

  /**
   * Append text content to file, with encoding.
   * 
   * @param filepath
   *          File to be appended.
   * @param content
   *          String content to be appended to file.
   * @param encoding
   *          Encoding used for string content.
   * 
   * @throws IOException
   */
  public static void appendContent(File file, String content, String encoding) throws IOException {
    Writer writer = null;
    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), encoding));
      writer.append(content);
    } finally {
      if (writer != null) {
        writer.flush();
        writer.close();
      }
    }
  }

  /**
   * Retrieve the given file's MIME type;
   * 
   * @param fileUrl
   *          File's URL.
   * @return File's MIME type (for example: <i>image/jpeg</i>)
   * @throws IOException
   *           If an exception occurs.
   */
  public static String getMimeType(String fileUrl) throws IOException {
    FileNameMap fileNameMap = URLConnection.getFileNameMap();
    return fileNameMap.getContentTypeFor(fileUrl);
  }

  /**
   * Open the file/folder with the provided file name, in the provided path directory.<br/>
   * Internally uses {@link #openFile(String, String)}.
   * 
   * @param folder
   *          The file's directory.
   * @param file
   *          The file's name (case-insensitive).
   * @return The specified file.
   * @throws Exception
   *           If the provided path and/or file are invalid, or if any standard java file handling exception occurs.
   * @see #openFile(String)
   * @see #openFile(String, String)
   */
  public static File openFile(File folder, String file) throws Exception {
    if (folder != null && folder.isDirectory()) {
      return FileUtils.openFile(folder.getCanonicalPath(), file);
    }
    return new File(folder, file);
  }

  /**
   * Open the file/folder with the provided file name, in the provided path directory.<br/>
   * Internally uses {@link #openFile(String)}.
   * 
   * @param path
   *          The file's directory.
   * @param file
   *          The file's name (case-insensitive).
   * @return The specified file.
   * @throws Exception
   *           If the provided path and/or file are invalid, or if any standard java file handling exception occurs.
   * @see #openFile(String)
   * @see #openFile(File, String)
   */
  public static File openFile(String path, String file) throws Exception {
    if (StringUtils.isNotBlank(path) && StringUtils.isNotBlank(file)) {
      String separator = "";
      if (!path.endsWith("/") && !path.endsWith("\\") && !file.startsWith("\\") && !file.startsWith("/")) {
        separator = System.getProperty("file.separator");
        if (StringUtils.isBlank(separator)) {
          separator = (path.indexOf("/") < 0 ? "\\" : "/");
        }
      }
      return FileUtils.openFile(path + separator + file);
    }
    return new File(path, file);
  }

  /**
   * Open the file/folder in the provided filepath.<br/>
   * The last name in the filepath's name sequence is <u>not</u> case-sensitive.<br/>
   * This is mostly useful for finding a specific file in a directory without worrying about it's extension case (i.e., '<i>.XML</i>
   * ' is considered to be the same as '<i>.xml</i>').<br/>
   * However, if an immediate match is found, it is returned without any additional processing, making this a viable and light
   * choice for file handling.
   * 
   * @param filepath
   *          Full path of the file or folder's location.
   * @return The specified file.
   * @throws Exception
   *           If the provided filepath is invalid or if any standard java file handling exception occurs.
   * @see #openFile(File, String)
   * @see #openFile(String, String)
   */
  public static File openFile(String filepath) throws Exception {
    File file = new File(filepath);
    if (file != null && file.canRead()) {
      return file;
    }

    if (StringUtils.isBlank(filepath) || (filepath.indexOf("\\") < 0 && filepath.indexOf("/") < 0)) {
      throw new Exception("Invalid file path provided: " + filepath);
    }

    int pos = filepath.lastIndexOf("\\");
    if (pos < 0) {
      pos = filepath.lastIndexOf("/");
    }
    final String pathname = filepath.substring(0, pos);
    final String filename = filepath.substring(pos + 1, filepath.length());

    File fileFolder = new File(pathname);
    if (fileFolder == null || !fileFolder.isDirectory() || !fileFolder.canRead()) {
      throw new Exception("Invalid folder: " + pathname);
    }
    File[] atomFiles = fileFolder.listFiles(new FileFilter() {
      @Override
      public boolean accept(File file) {
        return file != null && StringUtils.equalsIgnoreCase(file.getName(), filename);
      }
    });
    if (atomFiles == null || atomFiles.length == 0) {
      return file;
    }
    return atomFiles[0];
  }

  /**
   * Get parent directory for filepath. If no parent directory is detected in filepath, an empty string is returned.
   * 
   * @param filepath
   * @return
   */
  public static String getParent(String filepath) {
    String retObj = null;
    if (StringUtils.isNotBlank(filepath)) {
      retObj = new File(filepath).getParent();

      if (retObj == null) {
        retObj = "";
      }
    }
    return retObj;
  }

  public static String getFilename(String filepath) {
    String retObj = null;
    if (StringUtils.isNotBlank(filepath)) {
      retObj = new File(filepath).getName();
    }
    return retObj;
  }

  /**
   * Get file extension.
   * 
   * @param file
   * @return
   */
  public static String getFileExtension(File file) {
    if (file != null && StringUtils.isNotBlank(file.getName())) {
      return getFileExtension(file.getName());
    } else {
      return null;
    }
  }

  /**
   * Get file extension.
   * 
   * @param filename
   *          The filename.
   * @return
   */
  public static String getFileExtension(String filename) {
    String retObj = null;
    if (StringUtils.isNotBlank(filename) && filename.lastIndexOf(".") != -1) {
      retObj = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }
    return retObj;
  }

  /**
   * Get file type from extension.
   * 
   * @param filename
   * @return
   */
  public static FileTypeEnum getFiletypeFromExtension(String extension) {
    FileTypeEnum retObj = null;

    if (StringUtils.isNotBlank(extension)) {
      retObj = MapUtils.getKey(fileTypeExtensionMap, extension);

      if (retObj == null) {
        retObj = FileTypeEnum.GENERIC;
      }
    }

    return retObj;
  }

  /**
   * Get (expected) file type from filename. NOTE: file type is informative only, there's no guarantee that actual file content
   * corresponds to this file type.
   * 
   * @param filename
   * @return
   */
  public static FileTypeEnum getFiletype(String filename) {
    FileTypeEnum retObj = null;

    if (StringUtils.isNotBlank(filename)) {
      String fileExtension = getFileExtension(filename);

      if (StringUtils.isNotBlank(fileExtension)) {
        retObj = MapUtils.getKey(fileTypeExtensionMap, fileExtension);

        if (retObj == null) {
          retObj = FileTypeEnum.GENERIC;
        }
      }
    }
    return retObj;
  }

  /**
   * 
   * @param file
   * @return
   */
  public static FileTypeEnum getFiletype(File file) {
    FileTypeEnum retObj = null;

    if (file != null) {
      retObj = getFiletype(file.getName());
    }
    return retObj;
  }

  /**
   * Get the name name of the file without extension.
   * 
   * Ex.: d:\svn\andre.docx -> andre
   * 
   * @param file
   *          the file
   * @return the filename without extension
   */
  public static String getFilenameWithoutExtension(String filename) {
    String retObj = null;

    if (StringUtils.isNotBlank(filename)) {
      if (filename.indexOf(".") != -1) {
        retObj = filename.substring(0, filename.lastIndexOf("."));
      } else {
        retObj = filename;
      }
    }

    return retObj;
  }

  public static String getFilenameWithoutExtension(File file) {
    if (file != null && StringUtils.isNotBlank(file.getName())) {
      return getFilenameWithoutExtension(file.getName());
    } else {
      return null;
    }
  }

  public static File moveToDir(File src, File target) throws IOException {
    return moveToDir(src, target, false);
  }

  /**
   * Moves a directory/file to another directory. Action is 'forced' (i.e. if a target file with same name exists, it will be
   * deleted).
   * 
   * @param srcDir
   *          Source directory to be moved.
   * @param destDir
   *          Target directory for original directory to be move.
   * @throws Exception
   */
  public static File moveToDir(File src, File target, boolean createDir) throws IOException {
    File retObj = null;
    if (src != null && src.exists()) {
      if (target != null) {
        if (!target.isDirectory()) {
          if (!createDir) {
            throw new IOException("Invalid target directory: " + target);
          } else {
            createFolder(target.getParent());
          }
        }

        retObj = new File(target, src.getName());

        if (retObj.exists()) {
          retObj.delete();
        }
        if (!src.renameTo(retObj)) {
          retObj = null;
        }
      } else {
        throw new IOException("Invalid target directory: " + target);
      }
    } else {
      throw new IOException("Invalid source dir/file: " + src);
    }
    return retObj;
  }

  /**
   * Moves a directory/file to another directory
   * 
   * @param srcDir
   *          Source directory to be moved.
   * @param destDir
   *          Target directory for original directory to be move.
   * @throws Exception
   */
  public static File moveToDir(String src, String target) throws IOException {
    return moveToDir(new File(src), new File(target));
  }

  /**
   * Moves a directory/file to another directory
   * 
   * @param srcDir
   *          Source directory to be moved.
   * @param destDir
   *          Target directory for original directory to be move.
   * @throws Exception
   */
  public static File moveToDir(File src, String target) throws IOException {
    return moveToDir(src, new File(target));
  }

  /**
   * Moves a directory/file to another directory
   * 
   * @param srcDir
   *          Source directory to be moved.
   * @param destDir
   *          Target directory for original directory to be move.
   * @throws Exception
   */
  public static File moveToDir(String src, File target) throws IOException {
    return moveToDir(new File(src), target);
  }

  public static String removeExtension(String filename) {
    String retObj = null;
    if (StringUtils.isNotBlank(filename)) {
      if (filename.contains(".") && filename.lastIndexOf(".") != 0) {
        retObj = filename.substring(0, filename.lastIndexOf("."));
      }
    }
    return retObj;
  }

  /**
   * Create a file in temporary dir with filename from current milliseconds with 'tmp' extension.
   * 
   * @return The temporary file. NOTE: file is not actually created in file system, only path to target temp file; interested
   *         parties should create file.
   * @throws IOException
   */
  public static File createTempFile() throws IOException {
    return createTempFile(System.currentTimeMillis() + ".tmp");
  }

  /**
   * Create a file in temporary dir with provided filename.
   * 
   * @param filename
   *          The desired filename. If no filename is provided, {@link #createTempFile()} is invoked.
   * 
   * @param allowAlreadyExists
   *          allows to chose if returning a representation of an already existing file is permitted.
   * 
   * @return The temporary file. NOTE: file is not actually created in file system, only path to target temp file; interested
   *         parties should create file.
   * 
   * @throws IOException
   */
  public static File createTempFile(String filename, boolean allowAlreadyExists) throws IOException {
    File retObj = null;

    String tmpDirPath = System.getProperty("java.io.tmpdir");

    if (filename.startsWith("\\") || filename.startsWith("/")) {
      filename = filename.substring(1); // remove initial 'separator'
      // character
    }

    if (StringUtils.isBlank(filename)) {
      retObj = createTempFile();
    } else {
      retObj = new File(tmpDirPath, filename);

      if (retObj.isDirectory()) {
        throw new IOException("Temp file creation aborted; target is directory: " + retObj);
      } else if (retObj.isFile() && !allowAlreadyExists) {
        throw new IOException("Temp file creation aborted, file already exists: " + retObj);
      }
    }
    return retObj;
  }

  /**
   * Create a file in temporary dir with provided filename. Does not allow to create a Temp file that already exists
   * 
   * @param filename
   *          The desired filename. If no filename is provided, {@link #createTempFile()} is invoked.
   * @return The temporary file. NOTE: file is not actually created in file system, only path to target temp file; interested
   *         parties should create file.
   * @throws IOException
   */
  public static File createTempFile(String filename) throws IOException {
    return createTempFile(filename, false);
  }

  /**
   * Create a temporary file with provided content.
   * 
   * @param filename
   *          The filename for temporary file.
   * @param content
   *          The file's content.
   * @return The temporary file with provided content.
   * @throws IOException
   */
  public static File createTempFileWithContent(String filename, byte[] content) throws IOException {
    File tmpFile = createTempFile(filename);

    writeFileContent(tmpFile, content);

    return tmpFile;
  }

  /**
   * Create a temporary file with provided content.
   * 
   * @param content
   *          The file's content.
   * @return The temporary file with provided content.
   * @throws IOException
   */
  public static File createTempFileWithContent(byte[] content) throws IOException {
    File tmpFile = createTempFile();
    writeFileContent(tmpFile, content);

    return tmpFile;
  }

  /**
   * 
   * @param parent
   * @param filename
   * @param create
   * @return
   * @throws IOException
   */
  public static File getFile(String parent, String filename, boolean create) throws IOException {
    File retObj = new File(parent, filename);

    if (create && !retObj.isFile()) {
      retObj.getParentFile().mkdirs();
      retObj.createNewFile();
    }
    return retObj;
  }

  /**
   * Generate a md5 hash string from the byte fiile content
   * 
   * @param file
   *          The file
   * @return The md5 hash string
   * @throws NoSuchAlgorithmException
   * @throws Exception
   */
  public static String generateDocumentHash(byte[] file) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    StringBuilder hashValueSB = new StringBuilder();

    byte[] thedigest = md.digest(file);

    StringBuilder hexValue = new StringBuilder();

    for (byte md5byte : thedigest) {

      hexValue.delete(0, hexValue.length());
      hexValue.append(Integer.toHexString(0xFF & md5byte));

      if (hexValue.length() == 1) {
        // Append a leading 0
        hexValue.insert(0, '0');
      }

      hashValueSB.append(hexValue);
    }

    return hashValueSB.toString();
  }

  /**
   * Creates a folder on the specified path. If necessary, creates parent folders in order to create the folder at the specified
   * path. If the folder already exists, FALSE result is expected.
   * 
   * @param folderPath
   *          the Path to the desired folder
   * @return true if the folder was successfully created, false otherwise.
   */
  public static boolean createFolder(String folderPath) {
    return new File(folderPath).mkdirs();
  }

  /**
   * Resize a given image
   * 
   * @param fileData
   *          The image in bytes
   * @param width
   *          The desired width
   * @param height
   *          The desired height
   * @param fileType
   *          The image extension
   * @return The image resized
   * @throws IOException
   */
  public static byte[] resizeImage(byte[] fileData, int width, int height, String fileType) throws IOException {
    ByteArrayInputStream in = new ByteArrayInputStream(fileData);
    BufferedImage img = ImageIO.read(in);
    if (height == 0) {
      height = (width * img.getHeight()) / img.getWidth();
    }
    if (width == 0) {
      width = (height * img.getWidth()) / img.getHeight();
    }
    Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    ImageIO.write(imageBuff, fileType, buffer);

    return buffer.toByteArray();
  }

  public static String getDefaultMimeType() {
    return UNKNOWN_MIME_TYPE;
  }
}