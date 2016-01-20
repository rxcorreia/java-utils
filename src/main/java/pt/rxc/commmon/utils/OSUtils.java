package pt.rxc.commmon.utils;

import java.io.File;
import java.util.Locale;

/**
 * Utility methods related with Operating System detection and information.
 * 
 * @author ruben.correia
 *
 */
public class OSUtils {

  public static final String SYS_PROP_OS_NAME = "os.name";
  public static final String SYS_PROP_OS_VERSION = "os.version";
  public static final String SYS_PROP_OS_ARCH = "os.arch";

  public static final String SYS_PROP_JAVA_VERSION = "java.version";
  public static final String SYS_PROP_JAVA_VENDOR = "java.vendor";
  public static final String SYS_PROP_JAVA_VENDOR_URL = "java.vendor.url";
  public static final String SYS_PROP_JAVA_HOME = "java.home";
  public static final String SYS_PROP_JAVA_VM_VERSION = "java.vm.version";
  public static final String SYS_PROP_JAVA_VM_VENDOR = "java.vm.vendor";
  public static final String SYS_PROP_JAVA_VM_NAME = "java.vm.name";
  public static final String SYS_PROP_JAVA_IO_TMPDIR = "java.io.tmpdir";

  public static final String SYS_PROP_USER_NAME = "user.name";
  public static final String SYS_PROP_USER_HOME = "user.home";
  public static final String SYS_PROP_USER_DIR = "user.dir";

  public static String getSystemInfo() {
    return getSystemInfo(false);
  }

  public static String getExtendedSystemInfo() {
    return getSystemInfo(true);
  }

  private static String getSystemInfo(boolean extended) {
    StringBuffer retObj = new StringBuffer();

    // JAVA
    retObj.append("Java version: " + System.getProperty(SYS_PROP_JAVA_VERSION));
    if (extended) {
      retObj.append("\nJava vendor: " + System.getProperty(SYS_PROP_JAVA_VENDOR));
      retObj.append("\nJava home: " + System.getProperty(SYS_PROP_JAVA_HOME));
      retObj.append("\nJVM name: " + System.getProperty(SYS_PROP_JAVA_VM_NAME));
      retObj.append("\nJVM version: " + System.getProperty(SYS_PROP_JAVA_VM_VERSION));
      retObj.append("\nJava tmp dir: " + System.getProperty(SYS_PROP_JAVA_IO_TMPDIR));
    }

    // OS
    retObj.append("\nOS Name: " + System.getProperty(SYS_PROP_OS_NAME));
    retObj.append("\nOS Version: " + System.getProperty(SYS_PROP_OS_VERSION));
    retObj.append("\nOS Arch: " + System.getProperty(SYS_PROP_OS_ARCH));
    retObj.append("\nOS Hostname: " + getHostname());
    retObj.append("\nOS Locale: " + getLocale());

    // CPU
    retObj.append("\nCPU (cores): " + Runtime.getRuntime().availableProcessors());

    // MEMORY
    retObj.append("\nMemory (free): " + StringUtils.convertFileSizeIntoHumanReadableFormat(Runtime.getRuntime().freeMemory()));
    long maxMemory = Runtime.getRuntime().maxMemory();
    retObj.append("\nMemory (max): "
        + (maxMemory == Long.MAX_VALUE ? "no limit" : StringUtils.convertFileSizeIntoHumanReadableFormat(maxMemory)));
    retObj.append(
        "\nMemory (available to JVM): " + StringUtils.convertFileSizeIntoHumanReadableFormat(Runtime.getRuntime().totalMemory()));

    if (extended) {
      retObj.append("\nUser name: " + System.getProperty(SYS_PROP_USER_NAME));
      retObj.append("\nUser home: " + System.getProperty(SYS_PROP_USER_HOME));
      retObj.append("\nUser dir: " + System.getProperty(SYS_PROP_USER_DIR));
    }

    // FILESYSTEM
    File[] roots = File.listRoots();

    /* For each filesystem root, print some info */
    for (File root : roots) {
      String rootPath = root.getAbsolutePath();
      retObj.append("\nFilesystem root: " + rootPath);
      retObj
          .append("\nTotal space (" + rootPath + "): " + StringUtils.convertFileSizeIntoHumanReadableFormat(root.getTotalSpace()));
      retObj.append("\nFree space (" + rootPath + "): " + StringUtils.convertFileSizeIntoHumanReadableFormat(root.getFreeSpace()));
      retObj
          .append("\nUsable space(" + rootPath + "): " + StringUtils.convertFileSizeIntoHumanReadableFormat(root.getUsableSpace()));
    }

    return retObj.toString();
  }

  /**
   * Get the operation system type.
   * 
   * @return the operating system type.
   */
  public static OSType getOperationSystemType() {
    OSType retObj = OSType.UNKNOWN;

    if (isWindows()) {
      retObj = OSType.WINDOWS;
    } else if (isMac()) {
      retObj = OSType.MAC;
    } else if (isUnix()) {
      retObj = OSType.UNIX;
    } else if (isSolaris()) {
      retObj = OSType.SOLARIS;
    }

    return retObj;
  }

  public static String getHostname() {
    String retObj = null;

    if (isWindows()) {
      retObj = System.getenv("COMPUTERNAME");
    } else if (isUnix()) {
      retObj = System.getenv("HOSTNAME");
    }

    return retObj;
  }

  /**
   * OS type enumeration.
   * 
   * @author ruben.correia
   */
  public enum OSType {
                      UNIX,
                      WINDOWS,
                      SOLARIS,
                      MAC,
                      UNKNOWN
  }

  /**
   * Check Windows OS.
   * 
   * @return true/false
   */
  public static boolean isWindows() {
    String os = System.getProperty(SYS_PROP_OS_NAME).toLowerCase();
    return (os.indexOf("win") >= 0);
  }

  /**
   * Check Mac OS.
   * 
   * @return true/false
   */
  public static boolean isMac() {
    String os = System.getProperty(SYS_PROP_OS_NAME).toLowerCase();
    return (os.indexOf("mac") >= 0);
  }

  /**
   * Check Unix OS.
   * 
   * @return true/false
   */
  public static boolean isUnix() {
    String os = System.getProperty(SYS_PROP_OS_NAME).toLowerCase();
    return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
  }

  /**
   * Check Solaris OS.
   * 
   * @return true/false
   */
  public static boolean isSolaris() {
    String os = System.getProperty(SYS_PROP_OS_NAME).toLowerCase();
    return (os.indexOf("sunos") >= 0);
  }

  /**
   * Return current system locale.
   * 
   * <br />
   * Note: Check <a href=http://bugs.java.com/bugdatabase/view_bug.do?bug_id=7073906m"> JDK-7073906 : Locale.getDefault() returns
   * wrong Locale for Java SE 7</a> for current implementation logic.
   *
   * @return The current system locale.
   */
  public static Locale getLocale() {
    Locale retObj = null;
    String javaVersion = System.getProperty(SYS_PROP_JAVA_VERSION).toLowerCase();

    if (javaVersion.startsWith("1.5.") || javaVersion.startsWith("1.6.")) {
      retObj = Locale.getDefault();
    } else if (javaVersion.startsWith("1.7.")) {
      retObj = Locale.getDefault(Locale.Category.FORMAT);
    }
    return retObj;
  }

}