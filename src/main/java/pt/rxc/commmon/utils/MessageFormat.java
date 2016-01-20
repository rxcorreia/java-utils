package pt.rxc.commmon.utils;

/**
 * Provides a means to produce concatenated messages in a language-neutral way.<br/>
 * This class is <b>GWT Compatible</b>.
 * 
 * @author luis.cabral
 * @see java.text.MessageFormat
 */
public class MessageFormat {

  private String pattern;

  /**
   * Hidden constructor to force the use of the format static method.
   * 
   * @param pattern
   */
  private MessageFormat(String pattern) {
    this.pattern = pattern;
  }

  /**
   * Method that handles the message values replace.
   * 
   * @param pattern
   *          the message with parameters to be replaced.
   * @param values
   *          the values to replace in the message.
   * @return the message with the values.
   */
  public static String format(String pattern, Object... values) {
    return new MessageFormat(pattern).format(values);
  }

  private String format(Object... values) {
    String retObj = (pattern == null ? "" : pattern);

    if (retObj.trim().length() > 0 && values != null && values.length > 0) {
      int i = 0;

      for (Object value : values) {
        retObj = retObj.replaceAll("\\{" + i++ + "\\}", value == null ? "" : value.toString());
      }
    }
    return retObj;
  }
}
