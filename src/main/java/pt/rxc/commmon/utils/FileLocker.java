package pt.rxc.commmon.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.apache.log4j.Logger;

/**
 * File lock using Java IO/NIO API.
 * 
 * @author ruben.correia
 */
public class FileLocker {
  private static final Logger LOGGER = Logger.getLogger(FileLocker.class);

  private static final String FILE_LOCK_SUFIX = ".lck";

  private File file = null;
  private FileChannel fileChannel = null;
  private RandomAccessFile randomAccessFile = null;
  private FileLock fileLock = null;

  /**
   * Initialize the file lock with the given filename.
   * 
   * @param filename
   *          the filename of the file to lock
   */
  public FileLocker(String filename) {
    if (StringUtils.isNotBlank(filename)) {
      this.file = new File(filename + FILE_LOCK_SUFIX);
    } else {
      throw new IllegalArgumentException("Invalid file provided: should not be null.");
    }
  }

  /**
   * Initialize the file lock with the given file.
   * 
   * @param file
   *          the file to be locked
   */
  public FileLocker(File file) {
    if (file != null) {
      this.file = file;
    } else {
      throw new IllegalArgumentException("Invalid file provided: should not be null.");
    }
  }

  /**
   * Non-blocking lock-
   * 
   * @param block
   * @return the return of the lock. <code>true</code> if successfully locked, <code>false</code> otherwise.
   */
  public boolean lock(boolean block) {
    boolean retObj = true;

    if (file == null) {
      throw new IllegalArgumentException("File to be locked not set.");
    }

    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
      fileChannel = randomAccessFile.getChannel();

      if (!block) {
        try {
          fileLock = fileChannel.tryLock();
          if (fileLock == null) {
            retObj = false;
          }
        } catch (Exception e) {
          e.printStackTrace();
          retObj = false;
        }
      } else {
        fileLock = fileChannel.lock();
      }
    } catch (Exception e) {
      LOGGER.error("Error getting file lock: " + file.getAbsolutePath(), e);
      retObj = false;
    }

    return retObj;
  }

  /**
   * Release the lock.
   */
  public void release() {
    if (fileLock != null) {
      try {
        fileLock.release();
      } catch (IOException e) {
        LOGGER.error("Error releasing lock on file " + file.getAbsolutePath());
      }
    }

    if (fileChannel != null) {
      try {
        fileChannel.close();
      } catch (IOException e) {
        LOGGER.error("Error releasing channel on file " + file.getAbsolutePath());
      }
    }

    if (randomAccessFile != null) {
      try {
        randomAccessFile.close();
      } catch (IOException e) {
        LOGGER.error("Error releasing random access file on file " + file.getAbsolutePath());
      }
    }

    if (file != null && file.exists() && file.canWrite()) {
      try {
        file.delete();
      } catch (Exception e) {
        LOGGER.warn("Error while deleting lock file", e);
      }
    }
  }
}