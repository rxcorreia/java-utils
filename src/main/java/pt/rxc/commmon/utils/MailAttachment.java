package pt.rxc.commmon.utils;

import java.io.Serializable;

/**
 * 
 * @author ruben.correia
 *
 */
public class MailAttachment implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1239518290970492854L;
  private byte[] byteArray;
  private String mimeType;
  private String filename;

  public MailAttachment() {

  }

  /**
   * 
   * @return Binary content of attachment.
   */
  public byte[] getByteArray() {
    return byteArray;
  }

  /**
   * @param byteArray
   *          the byteArray to set
   */
  public void setByteArray(byte[] byteArray) {
    this.byteArray = byteArray;
  }

  /**
   * @return the MimeType of the file
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * @param mimeType
   *          the mimeType to set
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * 
   * @return the filename of this attachment
   */
  public String getFilename() {
    return this.filename;
  }

  /**
   * 
   * @param filename
   *          the desired filename
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

}
