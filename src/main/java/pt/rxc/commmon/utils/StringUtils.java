package pt.rxc.commmon.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.Collator;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * String-related utility methods.
 * 
 * @author ruben.correia
 */
public class StringUtils {

  final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

  public final static String ENCODING_UTF_8 = "UTF-8";
  public final static String ENCODING_UTF_16 = "UTF-16";
  public final static String ENCODING_UTF_32 = "UTF-32";

  protected final static String baseCharsToConvert = "ãâáàäÃÂÀÁÄêéèëÊÉÈËîíìïÎÍÌÏõôóòöÕÔÓÒÖûúùüÛÚÙÜçÇñÑýÿÝŸ";
  protected final static String baseCharsConverted = "aaaaaAAAAAeeeeEEEEiiiiIIIIoooooOOOOOuuuuUUUUcCnNýÿÝŸ";

  protected final static String extendedCharsToConvert = "ãâáàäåăÃÂÀÁÄÅĂêéèëěÊÉÈËĚîíìïÎÍÌÏõôóòöøÕÔÓÒÖØûúùüÛÚÙÜçčÇČñÑýÿÝŸšŝşŠŜŞžŽğģĞĢřŘĳĲ";
  protected final static String extendedCharsConverted = "aaaaaaaAAAAAAAeeeeeEEEEEiiiiIIIIooooooOOOOOOuuuuUUUUccCCnNyyYYsssSSSzZggGGrRyY";

  protected static final String baseChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz            \n";

  /**
   * Return the new string if the old string in the list of the given values.
   * 
   * @param oldString
   *          the old string
   * @param newString
   *          the new string
   * @param values
   *          the values to check
   * @return the old or new string
   */
  public static String replaceIfInList(String oldString, String newString, String... values) {
    if (inList(oldString, values)) {
      return newString;
    } else {
      return oldString;
    }
  }

  /**
   * Return the old string by the new string, if any of the string in values start by the old string.
   * 
   * @param oldString
   *          the old string
   * @param newString
   *          the new string
   * @param values
   *          the values to check
   * @return the new or old string
   */
  public static String replaceIfStartsWith(String oldString, String newString, String... values) {
    for (String value : values) {
      if (oldString.startsWith(value)) {
        return newString;
      }
    }

    return oldString;
  }

  /**
   * Replace null string by empty string
   * 
   * @param value
   * @return
   */
  public static String replaceNull(String value) {
    if (value == null) {
      return "";
    }
    return value;
  }

  /**
   * Check if the given string is in the list of strings.
   * 
   * @param string
   *          the string to check
   * @param values
   *          the list value
   * @return true/false
   */
  public static boolean inList(String string, String... values) {
    for (String value : values) {
      if (string.equals(value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if the given string ends with any of the given values.
   * 
   * @param string
   *          the string to match
   * @param values
   *          the values
   * @param ignoreCase
   *          ignore case ?
   * @return true / false
   */
  private static boolean endsWithIgnoreCase(String string, boolean ignoreCase, String... values) {
    boolean retObj = false;

    if (isNotBlank(string) && values != null) {
      for (String value : values) {
        if (ignoreCase) {
          if (string.toUpperCase().endsWith(value.toUpperCase())) {
            retObj = true;
            break;
          }
        } else {
          if (string.endsWith(value)) {
            retObj = true;
            break;
          }
        }
      }
    }

    return retObj;
  }

  /**
   * Check if the given string ends with any of the given values, NOT ignoring case.
   * 
   * @param string
   *          the string to match
   * @param values
   *          the values
   * @return true / false
   */
  public static boolean endsWith(String string, String... values) {
    return endsWithIgnoreCase(string, false, values);
  }

  /**
   * Check if the given string ends with any of the given values, ignoring case.
   * 
   * @param string
   *          the string to match
   * @param values
   *          the values
   * @return true / false
   */
  public static boolean endsWithIgnoreCase(String string, String... values) {
    return endsWithIgnoreCase(string, true, values);
  }

  /**
   * Truncate a string by the given length, cutting words if necessary.
   * 
   * @param string
   *          the string
   * @param length
   *          the max length
   * @param suffix
   *          the suffix to add if the string is > length
   * @return the new string < length
   */
  public static String truncateString(String str, int length, String suffix) {
    if (str != null && length >= 0) {
      suffix = (suffix == null ? "" : suffix);
      if (str.length() > (length - suffix.length())) {
        int size = length - suffix.length();
        while (size < 0) {
          size++;
        }
        str = str.substring(0, size) + suffix;
      }
    }
    return (str == null ? null : str);
  }

  /**
   * Check if the given string is null or if it's empty ("") after a trim.
   * 
   * @param str
   *          The string to check.
   * @return True if it's blank, false otherwise.
   * @see #isNotBlank(String)
   */
  public static boolean isBlank(String str) {
    return str == null || "".equals(str.trim());
  }

  /**
   * Check if the given string is not null null or if itsn't empty ("") after a trim.
   * 
   * @param str
   *          The string to check.
   * @return True if it's not blank, false otherwise.
   * @see #isBlank(String)
   */
  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }

  /**
   * Compare the two strings.<br/>
   * Internally using {@link StringUtils#isOneOf(String, String...)}.
   * 
   * @param str1
   *          string to compare with
   * @param str2
   *          string to compare to
   * @return True if the strings match, false otherwise.
   * @see #equalsIgnoreCase(String, String)
   * @see #isOneOf(String, String...)
   */
  public static boolean equals(String str1, String str2) {
    return equals(str1, (Object) str2);
  }

  /**
   * Compare the two strings.<br/>
   * Internally using {@link StringUtils#isOneOf(String, String...)}.
   * 
   * @param str1
   *          string to compare with
   * @param str2
   *          string to compare to
   * @return True if the strings match, false otherwise.
   * @see #equalsIgnoreCase(String, String)
   * @see #isOneOf(String, String...)
   */
  public static boolean equals(String str1, Object str2) {
    return isOneOf(str1, str2);
  }

  /**
   * Compare the two strings, ignoring letter-case.<br/>
   * Internally uses {@link StringUtils#isOneOfIgnoreCase(String, String...)}.
   * 
   * @param str1
   *          string to compare with
   * @param str2
   *          string to compare to
   * @return True if the strings match regardless of letter-case, false otherwise.
   * @see #equalsIgnoreCase(String, String)
   * @see #isOneOfIgnoreCase(String, String...)
   */
  public static boolean equalsIgnoreCase(String str1, String str2) {
    return equalsIgnoreCase(str1, (Object) str2);
  }

  /**
   * Compare the two strings, ignoring letter-case.<br/>
   * Internally uses {@link StringUtils#isOneOfIgnoreCase(String, String...)}.
   * 
   * @param str1
   *          string to compare with
   * @param str2
   *          string to compare to
   * @return True if the strings match regardless of letter-case, false otherwise.
   * @see #equalsIgnoreCase(String, String)
   * @see #isOneOfIgnoreCase(String, String...)
   */
  public static boolean equalsIgnoreCase(String str1, Object str2) {
    return isOneOfIgnoreCase(str1, str2);
  }

  /**
   * Checks if the given value exists in any of the values provided.<br/>
   * If the value to search for is null and the values to search in <b>contain</b> at lest one null value, then this operation
   * returns <b>true</b>.
   * 
   * @param value
   *          Value to validate.
   * @param values
   *          Array of values to search in.
   * @return True only if the value is contained in the given array.
   * @see #isOneOfIgnoreCase(String, String...)
   */
  public static boolean isOneOf(String value, String... values) {
    return isOneOf(value, (Object[]) values);
  }

  /**
   * Checks if the given value exists in any of the values provided.<br/>
   * If the value to search for is null and the values to search in <b>contain</b> at lest one null value, then this operation
   * returns <b>true</b>.
   * 
   * @param value
   *          Value to validate.
   * @param values
   *          Array of values to search in.
   * @return True only if the value is contained in the given array.
   * @see #isOneOfIgnoreCase(String, String...)
   */
  public static boolean isOneOf(String value, Object... values) {
    return isOneOf(false, value, values);
  }

  /**
   * Checks if the given value exists in any of the values provided, regardless of letter-case.<br/>
   * If the value to search for is null and the values to search in <b>contain</b> at lest one null value, then this operation
   * returns <b>true</b>.
   * 
   * @param value
   *          Value to validate.
   * @param values
   *          Array of values to search in.
   * @return True only if the value is contained in the given array.
   * @see #isOneOf(String, String...)
   */
  public static boolean isOneOfIgnoreCase(String value, String... values) {
    return isOneOfIgnoreCase(value, (Object[]) values);
  }

  /**
   * Checks if the given value exists in any of the values provided, regardless of letter-case.<br/>
   * If the value to search for is null and the values to search in <b>contain</b> at lest one null value, then this operation
   * returns <b>true</b>.
   * 
   * @param value
   *          Value to validate.
   * @param values
   *          Array of values to search in.
   * @return True only if the value is contained in the given array.
   * @see #isOneOf(String, String...)
   */
  public static boolean isOneOfIgnoreCase(String value, Object... values) {
    return isOneOf(true, value, values);
  }

  /**
   * Checks if the given value exists in any of the values provided.
   * 
   * @param ignoreCase
   *          Toggles 'ignoreCase' mode on/off.
   * @param value
   *          Value to validate.
   * @param values
   *          Array of values to search in.
   * @return True only if the value is contained in the given array.
   */
  private static boolean isOneOf(boolean ignoreCase, String value, Object... values) {
    if (values != null) {
      for (Object item : values) {
        if (value == null && item == null) {
          return true;
        } else if (value != null && item != null) {
          if ((!ignoreCase && value.equals(item.toString())) || (ignoreCase && value.equalsIgnoreCase(item.toString()))) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Tests if the provided string starts with the specified prefix.<br/>
   * Comparison is <i>case-sensitive</i>.
   * 
   * @param str
   *          The string to validate against.
   * @param prefix
   *          The value to use in the validation.
   * @return True if the character sequence represented by the argument is a prefix of the character sequence represented by this
   *         string; false otherwise. Note also that true will be returned if the argument is an empty string or is equal to this
   *         String object as determined by the equals(Object) method.
   * @see #startsWithIgnoreCase(String, String)
   */
  public static boolean startsWith(String str, String prefix) {
    if (str != null && prefix != null) {
      return str.startsWith(prefix);
    }
    return (str == null && prefix == null);
  }

  /**
   * Tests if the provided string starts with the specified prefix.<br/>
   * Comparison is <i>case-sensitive</i>.
   * 
   * @param str
   *          The string to validate against.
   * @param suffix
   *          The value to use in the validation.
   * @return True if the character sequence represented by the argument is a suffix of the character sequence represented by this
   *         object; false otherwise. Note that the result will be true if the argument is the empty string or is equal to this
   *         String object as determined by the equals(Object) method.
   * @see #endsWithIgnoreCase(String, String)
   */
  public static boolean endsWith(String str, String suffix) {
    if (str != null && suffix != null) {
      return str.endsWith(suffix);
    }
    return (str == null && suffix == null);
  }

  /**
   * Check if a string contains any of the provided values.
   * 
   * @param haystack
   *          the string to check
   * @param needles
   *          the list of values
   * @return True if the string contains <b>at least</b> one of the provided values, false otherwise.
   * @see #containsIgnoreCase(String, String...)
   */
  public static boolean contains(String haystack, String... needles) {
    if (haystack != null && needles != null) {
      for (String needle : needles) {
        if (needle != null && haystack.indexOf(needle) > -1) {
          return true;
        }
      }
    }
    return (haystack == null && (needles == null || needles.length == 0));
  }

  /**
   * Check if a string contains any of the provided values.
   * 
   * @param haystack
   *          the string to check
   * @param needles
   *          the list of values
   * @return True if the string contains <b>at least</b> one of the provided values, false otherwise.
   * @see #containsIgnoreCase(String, String...)
   */
  public static boolean contains(String haystack, List<String> needles) {
    return CollectionUtils.isNotEmpty(needles) ? contains(haystack, needles.toArray(new String[needles.size()])) : false;
  }

  /**
   * Check if a string contains any of the provided values (ignoring case).
   * 
   * @param haystack
   *          the string to check
   * @param needles
   *          the list of values
   * @return True if the string contains <b>at least</b> one of the provided values, false otherwise.
   * @see #contains(String, String...)
   */
  public static boolean containsIgnoreCase(String haystack, String... needles) {
    if (haystack != null && needles != null) {
      for (String needle : needles) {
        if (needle != null && haystack.toLowerCase().indexOf(needle.toLowerCase()) > -1) {
          return true;
        }
      }
    }
    return (haystack == null && (needles == null || needles.length == 0));
  }

  public static boolean containsIgnoreCase(String haystack, List<String> needles) {
    return CollectionUtils.isNotEmpty(needles) ? contains(haystack, needles.toArray(new String[needles.size()])) : false;
  }

  /**
   * Check if a string contains any value of the array of provided values, and return it.
   * 
   * @param haystack
   *          the string to check
   * @param needles
   *          the list of values
   * @return The first found value that performs a valid match or null if none is found.
   * @see #containsAnyIgnoreCase(String, String...)
   */
  public static String containsAny(String haystack, String... needles) {
    if (haystack != null && needles != null) {
      for (String needle : needles) {
        if (contains(haystack, needle)) {
          return needle;
        }
      }
    }
    return null;
  }

  /**
   * Check if a string contains any value (ignoring case) of the array of provided values, and return it.
   * 
   * @param haystack
   *          the string to check
   * @param needles
   *          the list of values
   * @return The first found value that performs a valid match or null if none is found.
   * @see #containsAny(String, String...)
   */
  public static String containsAnyIgnoreCase(String haystack, String... needles) {
    if (haystack != null && needles != null) {
      for (String needle : needles) {
        if (containsIgnoreCase(haystack, needle)) {
          return needle;
        }
      }
    }
    return null;
  }

  /**
   * Performs a WildCard matching for the text and pattern provided.
   * 
   * @param text
   *          The text to be tested for matches.
   * @param wildcard
   *          The pattern to be matched for. This can contain the WildCard character '*' (asterisk).
   * @return <tt>true</tt> if a match is found, <tt>false</tt> otherwise.
   * @see #wildCardMatchIgnoreCase(String, String)
   */
  public static boolean wildCardMatch(String text, String wildcard) {
    if (wildcard == null || text == null) {
      return wildcard == null && text == null;
    }
    return text.matches(wildcardToRegex(wildcard));
  }

  /**
   * Performs a <i>case-insensitive</i> WildCard matching for the text and pattern provided.
   * 
   * @param text
   *          The text to be tested for matches.
   * @param wildcard
   *          The pattern to be matched for. This can contain the WildCard character '*' (asterisk).
   * @return <tt>true</tt> if a match is found, <tt>false</tt> otherwise.
   * @see #wildCardMatch(String, String)
   */
  public static boolean wildCardMatchIgnoreCase(String text, String wildcard) {
    return wildCardMatch(text == null ? null : text.toLowerCase(), wildcard == null ? null : wildcard.toLowerCase()); // $NOSONAR$
  }

  /**
   * Converts the provided WildCard text to a Regular Expression.
   * 
   * @param wildcard
   *          Text to convert.
   * @return The converted WildCard text.
   */
  public static String wildcardToRegex(String wildcard) {
    String retObj = "^";
    if (wildcard != null) {
      for (int i = 0, is = wildcard.length(); i < is; i++) {
        char c = wildcard.charAt(i);
        switch (c) {
        case '*':
          retObj += ".*";
          break;
        case '?':
          retObj += ".";
          break;
        case '(':
        case ')':
        case '[':
        case ']':
        case '$':
        case '^':
        case '.':
        case '{':
        case '}':
        case '|':
        case '\\':
          // escape special RegExp chars
          retObj += "\\" + c;
          break;
        default:
          retObj += c;
          break;
        }
      }
    }
    retObj += "$";
    return retObj;
  }

  /**
   * Capitalize the provided text.<br/>
   * For example, "<code>the TOOTH faiRy? no wAy!</code>" is converted to "<code>The tooth fairy? No way!</code>".
   * 
   * @param str
   *          Value to capitalize.
   * @return Capitalized text.
   */
  public static String capitalize(String str) {
    if (str == null || str.length() == 0) {
      return str;
    }
    char[] arr = str.toCharArray();
    boolean cap = true;
    for (int i = 0; i < arr.length; i++) {
      if (cap) {
        if (!("" + arr[i]).matches("\\s")) {
          if (!Character.isUpperCase(arr[i])) {
            arr[i] = Character.toUpperCase(arr[i]);
          }
          cap = false;
        }
      } else {
        if (arr[i] == '.' || arr[i] == '?' || arr[i] == '!') {
          cap = true;
        } else {
          arr[i] = Character.toLowerCase(arr[i]);
        }
      }
    }
    return new String(arr);
  }

  /**
   * Capitalize the provided text, word by word.<br/>
   * For example, "<code>the TOOTH faiRy</code>" is converted to "<code>The Tooth Fairy</code>".
   * 
   * @param str
   *          Value to capitalize.
   * @return Capitalized text.
   */
  public static String capitalizeFullText(String str) {
    if (StringUtils.isBlank(str)) {
      return str;
    }
    String retObj = "";
    for (String word : str.split("\\s")) {
      if (word == null || word.length() == 0) {
        continue;
      }
      retObj += (StringUtils.isBlank(retObj) ? "" : " ") + capitalize(word);
    }
    return retObj;
  }

  public static String cleanSpecialCharacters(String text, String replacement) {
    return cleanSpecialCharacters(text, replacement, true);
  }

  /**
   * Replace extended characters in provided string.
   * 
   * @param text
   *          The text with characters to be replaced.
   * @param extended
   *          Use (more) extended characters
   * @return
   */
  public static String replaceExtendedCharacters(String text, boolean extended) {
    String retObj = "";

    if (isNotBlank(text)) {
      retObj = text;

      char[] beforeConversion = baseCharsToConvert.toCharArray();
      char[] afterConversion = baseCharsConverted.toCharArray();

      if (extended) {
        beforeConversion = extendedCharsToConvert.toCharArray();
        afterConversion = extendedCharsConverted.toCharArray();
      }

      for (int i = 0; i < beforeConversion.length; i++) {
        retObj = retObj.replaceAll(String.valueOf(beforeConversion[i]), String.valueOf(afterConversion[i]));
      }
    }

    return retObj;
  }

  public static String cleanSpecialCharacters(String text, String replacement, boolean trim) {
    String retObj = "";

    if (isNotBlank(text)) {
      retObj = text;

      char[] beforeConversion = extendedCharsToConvert.toCharArray();
      char[] afterConversion = extendedCharsConverted.toCharArray();

      for (int i = 0; i < beforeConversion.length; i++) {
        retObj = retObj.replaceAll(String.valueOf(beforeConversion[i]), String.valueOf(afterConversion[i]));
      }

      retObj = retObj.replaceAll("[^a-zA-Z0-9]", replacement);
      retObj = retObj.replaceAll("[_]{2,}", replacement);
    }

    // Clean replacement string at beginning of text
    if (trim && !"".equals(replacement)) {
      while (retObj.startsWith(replacement)) {
        retObj = retObj.replaceFirst(replacement, "");
      }
      while (retObj.endsWith(replacement)) {
        int idx = retObj.lastIndexOf(replacement);
        retObj = retObj.substring(0, idx);
      }
    } else {
      retObj = retObj.trim();
    }

    return retObj;
  }

  public static String cleanSpecialCharacters(String title) {
    return cleanSpecialCharacters(title, "_");
  }

  public static String singleString(String[] stringArray, String separator) {
    String retObj = null;

    if (isBlank(separator)) {
      separator = " ";
    }

    if (stringArray != null && stringArray.length > 0) {
      StringBuffer buf = new StringBuffer();
      int idx = 0;

      for (String str : stringArray) {
        buf.append(str);
        idx++;

        if (idx < stringArray.length) {
          buf.append(separator);
        }
      }

      retObj = buf.toString();
    }
    return retObj;
  }

  /**
   * Removes all whitespace from a string (space, tab, newline, CR...)
   * 
   * @param str
   *          Original string.
   * @return String with whitespace removed.
   */
  public static String removeWhitespace(String str) {
    String retObj = null;

    if (isNotBlank(str)) {
      retObj = str.replaceAll("\\s", "");
    }
    return retObj;
  }

  /**
   * Generate and return an hash code for the provided string.
   * 
   * @param password
   *          Password to encrypt.
   * @return
   * @throws ContentServicesException
   */
  public static String generateHashCode(String password) throws Exception {
    String retObj = null;
    try {
      if (StringUtils.isNotBlank(password)) {
        MessageDigest msgDigest = MessageDigest.getInstance("MD5");
        msgDigest.update(password.getBytes("UTF-8"));
        byte rawByte[] = msgDigest.digest();

        if (rawByte != null) {
          StringBuffer stringBuffer = new StringBuffer();

          for (int i = 0; i < rawByte.length; i++) {
            stringBuffer.append(Integer.toString((rawByte[i] & 0xff) + 0x100, 16).substring(1));
          }
          retObj = stringBuffer.toString();
        }
      }
    } catch (Exception e) {
      throw new Exception("Error when encrypting password");
    }
    return retObj;
  }

  /**
   * Convert the provided file size (in bytes) into a human readable format.
   * 
   * @param fileBytes
   * @return
   */
  public static String convertFileSizeIntoHumanReadableFormat(Long fileSizeInBytes) {
    String[] byteUnits = new String[] { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
    int exp = 0;
    String retObj = "";
    String size = "";
    double fileSize = (double) fileSizeInBytes;

    if (fileSizeInBytes >= 0) {
      while (fileSize > 1024 && exp < (byteUnits.length - 1)) {
        fileSize = fileSize / 1024;
        exp++;
      }
      size = fileSize + "";

      // Handle decimal places
      if (size.contains(".")) {
        if (exp == 0) {
          retObj = size.substring(0, size.indexOf("."));
        } else if (size.indexOf(".") + 3 <= size.length()) {
          retObj = size.substring(0, size.indexOf(".") + 3);
        } else {
          retObj = size.substring(0, size.indexOf(".") + 2);
        }
      } else {
        retObj = size;
      }

      // Add byte units
      retObj = retObj + " " + byteUnits[exp];
    } else {
      retObj = "[Negative size]";
    }
    return retObj;
  }

  public static String convertFileSizeIntoHumanReadableFormat(int fileSizeInBytes) {
    return convertFileSizeIntoHumanReadableFormat((long) fileSizeInBytes);
  }

  /**
   * Create a string representation for a {@link List} of objects, with default separator (", ").
   * 
   * @param list
   *          the list of objects to be displayed.
   * @return String representation of list, with default separator between values.
   */
  public static String listValues(List<?> list) {
    return listValues(list, ", ");
  }

  /**
   * Create a string representation for a {@link List} of objects, with provided separator.
   * 
   * @param list
   *          the list of objects to be displayed.
   * @return String representation of list, with provided separator between values.
   */
  public static String listValues(List<?> list, String separator) {
    String retObj = "";

    if (CollectionUtils.isNotEmpty(list)) {
      for (Object value : list) {
        retObj += (retObj.equals("") ? "" : separator) + (value != null ? value.toString() : null);
      }
    }
    return retObj;
  }

  public static List<String> valuesList(String string) {
    return valuesList(string, ", ");
  }

  /**
   * Convert a string in a lista of strings.
   * 
   * @param string
   *          the string
   * @param separator
   *          the operator
   * @return the list of values
   */
  public static List<String> valuesList(String string, String separator) {
    List<String> retObj = new ArrayList<String>();

    if (isNotBlank(string) && isNotBlank(separator)) {
      retObj = Arrays.asList(string.split(separator));
    }

    return retObj;
  }

  /** Locked c'tor. This class should not be instantiated. */
  protected StringUtils() {
  }

  /**
   * Replace the old string bt the new string, if the old string is in the list of values.
   * 
   * @param oldString
   *          the old string
   * @param newString
   *          the new string
   * @param values
   *          the list of values
   * @return the new or old string
   */
  public static String replaceIfContains(String oldString, String newString, String... values) {
    for (String value : values) {
      if (oldString.contains(value)) {
        return newString;
      }
    }

    return oldString;
  }

  /**
   * Return the new string of any of the strings in the list of values end with the old string.
   * 
   * @param oldString
   *          the old string
   * @param newString
   *          the new string
   * @param values
   *          the list of values
   * @return the new string or the old string
   */
  public static String replaceIfEndsWith(String oldString, String newString, String... values) {
    for (String value : values) {
      if (oldString.endsWith(value)) {
        return newString;
      }
    }
    return oldString;
  }

  /**
   * Replace the old string by the new string, if in the given list of strings any string starts with or ends with the old string.
   * 
   * @param oldString
   *          the old string
   * @param newString
   *          the new string
   * @param values
   *          the list of values to check
   * @return the old or new string
   */
  public static String replaceIfStartsOrEndsWith(String oldString, String newString, String... values) {
    for (String value : values) {
      if (oldString.startsWith(value) || oldString.endsWith(value)) {
        return newString;
      }
    }

    return oldString;
  }

  /**
   * Return the string which is after the given string.
   * 
   * @param originalString
   *          the original string
   * @param match
   *          the string to look string after
   * @return the after string, or the original string
   */
  public static String stringAfter(String originalString, String match) {
    if (originalString.indexOf(match) != -1) {
      return originalString.substring(originalString.indexOf(match));
    } else {
      return originalString;
    }
  }

  /**
   * Return a string of the given list of string if it end's with the given string and is not equal to the whole string.
   * 
   * @param string
   *          the string to check
   * @param strings
   *          the list of strings
   * @return the new string or old strind
   */
  public static String endWithAnyAndNotEqual(String string, String... strings) {
    for (String tmpString : strings) {
      if (string.endsWith(tmpString) && !string.equals(tmpString)) {
        return tmpString;
      }
    }
    return null;
  }

  /**
   * Return the string which is between a given separator.
   * 
   * @param string
   *          the string
   * @param separator
   *          the separator
   * @return the string between the separators
   */
  public static String stringBetween(String string, String separator) {
    return stringBetween(string, separator, separator);
  }

  /**
   * Return the string which is between the first and second separator.
   * 
   * @param string
   *          the entire string
   * @param firstSeparator
   *          the first separator
   * @param secondSeparator
   *          the second separator
   * @return the string between the two separators
   */
  public static String stringBetween(String string, String firstSeparator, String secondSeparator) {
    int firstOcurrence = string.indexOf(firstSeparator);
    if (firstOcurrence != -1) {
      int secondOcurrence = string.lastIndexOf(secondSeparator);
      if (secondOcurrence != -1 && firstOcurrence != secondOcurrence) {
        return string.substring(firstOcurrence + 1, secondOcurrence);
      }
    }
    return null;
  }

  /**
   * Get string tokens that are located between element.
   * 
   * @param msg
   *          The message to search pattern on
   * @param element
   *          The delimiter element to be included in pattern. Special characters should be escaped.
   * @return
   */
  public static List<String> getTokensBetweenElements(String msg, String element) {
    List<String> retObj = new ArrayList<String>();

    Pattern pattern = Pattern.compile(element + "(.*?)" + element);
    Matcher matcher = pattern.matcher(msg);
    while (matcher.find()) {
      retObj.add(matcher.group(1));
    }
    return retObj;
  }

  /**
   * Get string tokens that are located between element.
   * 
   * @param msg
   *          The message to search pattern on
   * @param element
   *          The delimiter element to be included in pattern. Special characters should be escaped.
   * @return
   */
  public static List<String> getTokensBetweenElements(String msg, String startElement, String endElement) {
    List<String> retObj = new ArrayList<String>();

    Pattern pattern = Pattern.compile(startElement + "(.*?)" + endElement);
    Matcher matcher = pattern.matcher(msg);
    while (matcher.find()) {
      retObj.add(matcher.group(1));
    }
    return retObj;
  }

  /**
   * Gets the tokens between and containing delimitors.
   * 
   * @param msg
   *          the msg
   * @param startDelimitors
   *          the start delimitors
   * @param endDelimitors
   *          the end delimitors
   * @return the tokens between and containing delimitors
   */
  public static List<String> getTokensBetweenAndContainingDelimitors(String msg, String startDelimitors, String endDelimitors) {
    List<String> retObj = new ArrayList<String>();

    Pattern pattern = Pattern.compile(startDelimitors + "(.*?)" + endDelimitors);
    Matcher matcher = pattern.matcher(msg);
    while (matcher.find()) {
      retObj.add(matcher.group(0));
    }
    return retObj;
  }

  /**
   * Truncate a string by the given length, but does not cut words.
   * 
   * @param string
   *          the string
   * @param length
   *          the max length
   * @param suffix
   *          the suffix to add if the string is > length
   * @return the new string < length
   * @see pt.gsoft.oasis.utils.shared.tools.StringUtils#truncateString(String, int, String)
   */
  public static String truncateStringByWord(String string, int length, String suffix) {
    if (string == null) {
      return string;
    }
    String retObj = "";

    // Split the string by white spaces
    StringTokenizer tokens = new StringTokenizer(string, " ");
    while (tokens.hasMoreTokens()) {
      String newWord = tokens.nextToken();
      int newSize = retObj.length() + newWord.length() + 1;
      if (newSize <= (length + 1)) {
        if (!"".equals(retObj)) {
          retObj += " ";
        }
        retObj += newWord;
      } else {
        retObj += suffix;
        break;
      }
    }

    return retObj;
  }

  public static String[] tokenize(String string, String[] delimiters) {
    String[] retObj = null;

    if (string != null && delimiters != null) {
      String replaceDelimiter = "$_" + System.currentTimeMillis() + "_$";

      for (String delimiter : delimiters) {
        // Make all delimiters uniform
        string = string.replace(delimiter, replaceDelimiter);
      }

      List<String> strList = new ArrayList<>();
      StringTokenizer tokens = new StringTokenizer(string, replaceDelimiter);
      while (tokens.hasMoreTokens()) {
        strList.add(tokens.nextToken());
      }

      retObj = new String[strList.size()];
      strList.toArray(retObj);
    }

    return retObj;
  }

  /**
   * Validate if the given value is a valid Email address.
   * 
   * @param email
   *          the email to validate
   * @return <ii>true</ii> if the provided value is a valid Email, <ii>false</ii> otherwise
   */
  public static boolean isValidEmail(String email) {
    if (email == null) {
      return false;
    }

    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
    Matcher m = p.matcher(email);
    return m.matches();
  }

  /**
   * Remove accentuation from the provided string.<br/>
   * For example, the value "<code>SchÃ¶n</code>" is converted to "<code>Schon</code>".
   * 
   * @param str
   *          The string to process.
   * @return The provided string, without any accentuation.
   */
  public static String removeAccents(CharSequence str) {
    if (str == null) {
      return null;
    }
    return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
  }

  /**
   * Tests if the provided string starts with the specified prefix.<br/>
   * Comparison is <i>case-insensitive</i>.
   * 
   * @param str
   *          The string to validate against.
   * @param prefix
   *          The value to use in the validation.
   * @return True if the character sequence represented by the argument is a case-insensitive prefix of the character sequence
   *         represented by this string; false otherwise.
   * @see #startsWith(String, String)
   */
  public static boolean startsWithIgnoreCase(String str, String prefix) {
    return startsWith(toLowerCase(str), toLowerCase(prefix));
  }

  /**
   * Tests if the provided string starts with the specified prefix.<br/>
   * Comparison is <i>case-insensitive</i>.
   * 
   * @param str
   *          The string to validate against.
   * @param suffix
   *          The value to use in the validation.
   * @return True if the character sequence represented by the argument is a case-insensitive suffix of the character sequence
   *         represented by this object; false otherwise.
   * @see #endsWith(String, String)
   */
  public static boolean endsWithIgnoreCase(String str, String suffix) {
    return endsWith(toLowerCase(str), toLowerCase(suffix));
  }

  /**
   * Converts all of the characters in the provided String to lower case using the rules of the system's default Locale.
   * 
   * @param str
   *          The {@link String} to convert.
   * @return The provided string, converted to lowercase.
   * @see #toUpperCase(String)
   */
  public static String toLowerCase(String str) {
    if (str != null) {
      return str.toLowerCase(Locale.getDefault());
    }
    return str;
  }

  /**
   * Converts all of the characters in the provided String to upper case using the rules of the system's default Locale.
   * 
   * @param str
   *          The {@link String} to convert.
   * @return The provided string, converted to uppercase.
   * @see #toLowerCase(String)
   */
  public static String toUpperCase(String str) {
    if (str != null) {
      return str.toUpperCase(Locale.getDefault());
    }
    return str;
  }

  /**
   * Compare the two strings.<br/>
   * 
   * @param str1
   *          string to compare with
   * @param str2
   *          string to compare to
   * @param collator
   *          pre-prepared {@link Collator} to perform the comparison with
   * @return True if the strings match according with the provided {@link Collator strength}, false otherwise.
   * @see #equals(String, String, int)
   * @see #equals(String, Object, int)
   * @see #equals(String, Object, Collator)
   */
  public static boolean equals(String str1, String str2, Collator collator) {
    return equals(str1, (Object) str2, collator);
  }

  /**
   * Compare the two strings.<br/>
   * Internally using {@link StringUtils#equals(String, Object, int)}.
   * 
   * @param str1
   *          string to compare with
   * @param str2
   *          string to compare to
   * @param strength
   *          {@link Collator} value indicating the strength of the comparison to perform. Must be one of {@link Collator#PRIMARY} ,
   *          {@link Collator#SECONDARY} or {@link Collator#TERTIARY}
   * @return True if the strings match according with the provided {@link Collator strength}, false otherwise.
   * @see #equals(String, Object, int)
   * @see #equals(String, String, Collator)
   * @see #equals(String, Object, Collator)
   */
  public static boolean equals(String str1, String str2, int strength) {
    return equals(str1, (Object) str2, strength);
  }

  /**
   * Compare the two strings.<br/>
   * Internally using {@link StringUtils#equals(String, Object, Collator)}.
   * 
   * @param str
   *          string to compare with
   * @param obj
   *          string to compare to
   * @param strength
   *          {@link Collator} value indicating the strength of the comparison to perform. Must be one of {@link Collator#PRIMARY} ,
   *          {@link Collator#SECONDARY} or {@link Collator#TERTIARY}
   * @return True if the strings match according with the provided {@link Collator strength}, false otherwise.
   * @see #equals(String, String, int)
   * @see #equals(String, String, Collator)
   * @see #equals(String, Object, Collator)
   */
  public static boolean equals(String str, Object obj, int strength) {
    if (strength == Collator.PRIMARY || strength == Collator.SECONDARY || strength == Collator.TERTIARY) {
      Collator collator = Collator.getInstance(new Locale("pt", "PT"));
      collator.setStrength(strength);
      return equals(str, obj, collator);
    }
    return equals(str, obj);
  }

  /**
   * Compare the two strings.<br/>
   * 
   * @param str
   *          string to compare with
   * @param obj
   *          string to compare to
   * @param collator
   *          pre-prepared {@link Collator} to perform the comparison with
   * @return True if the strings match according with the provided {@link Collator strength}, false otherwise.
   * @see #equals(String, String, int)
   * @see #equals(String, Object, int)
   * @see #equals(String, String, Collator)
   */
  public static boolean equals(String str, Object obj, Collator collator) {
    if (str != null && obj != null) {
      if (collator == null) {
        collator = Collator.getInstance(new Locale("pt", "PT"));
      }
      return collator.equals(str, obj.toString());
    }
    return equals(str, obj);
  }

  /**
   * Performs a string comparison ignoring case and accent. Beginning and ending spaces are trimmed.
   * 
   * @param string1
   *          string to be compared.
   * @param string2
   *          string to be compared.
   * @param locale
   *          The locale to perform the comparison.
   * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the
   *         second.
   */
  public static int compareIgnoreCaseAndAccent(String string1, String string2, Locale locale) {
    if (string1 == null || string2 == null) {
      if (string1 == null && string2 == null)
        return 0;
      if (string1 == null)
        return -1;
      return 1;
    }

    Collator collator = Collator.getInstance(locale);
    collator.setStrength(Collator.PRIMARY);
    collator.setDecomposition(Collator.FULL_DECOMPOSITION);

    return collator.compare(string1, string2);
  }

  public static boolean equalsIgnoreCaseAndAccent(String string1, String string2) {
    return compareIgnoreCaseAndAccent(string1, string2) == 0;
  }

  public static boolean equalsIgnoreCaseAndAccent(String string1, String string2, Locale locale) {
    return compareIgnoreCaseAndAccent(string1, string2, locale) == 0;
  }

  /**
   * Performs a string comparison ignoring case and accent. Beginning and ending spaces are trimmed.
   * 
   * @param string1
   *          string to be compared.
   * @param string2
   *          string to be compared.
   * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the
   *         second.
   */
  public static int compareIgnoreCaseAndAccent(String string1, String string2) {
    return compareIgnoreCaseAndAccent(string1, string2, Locale.getDefault());
  }

  /**
   * Converts a string to its hexadecimal representation.
   * 
   * @param arg
   * @return
   */
  public static String toHex(String arg) {
    return String.format("%040x", new BigInteger(arg.getBytes()));
  }

  /**
   * Deserialize (read) the object from Base64 encoded string. Use {@link #serializeObject(String) serializeObject} method to
   * serialize the object.
   * 
   * @param objString
   *          The Base64 string representing the serialized object.
   * @return The object.
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static Object deserializeObject(String objString) throws IOException, ClassNotFoundException {
    byte[] data = Base64.decodeBase64(objString.getBytes());

    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    Object o = ois.readObject();
    ois.close();
    return o;
  }

  /**
   * Serialize (write) the object to a Base64 encoded string. Use {@link #deserializeObject(String) deserializeObject} method to
   * deserialize object.
   * 
   * @param object
   *          The object to be serialized
   * @return The Base64 string representation of the object.
   * @throws IOException
   */
  public static String serializeObject(Serializable object) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(object);
    oos.close();
    return new String(Base64.encodeBase64(baos.toByteArray()));
  }

  /**
   * Encode binary data in Base64
   * 
   * @param bytes
   * @return
   */
  public static byte[] encodeBase64(byte[] bytes) {
    return Base64.encodeBase64(bytes);
  }

  /**
   * Decode binary data in Base64
   * 
   * @param bytes
   * @return
   */
  public static byte[] decodeBase64(byte[] bytes) {
    return Base64.decodeBase64(bytes);
  }

  public static String encrypt(String plainText, String password) {
    BasicTextEncryptor encryptor = new BasicTextEncryptor();
    encryptor.setPassword(password);
    return encryptor.encrypt(plainText);
  }

  public static String decrypt(String encryptedMessage, String password) {
    BasicTextEncryptor encryptor = new BasicTextEncryptor();
    encryptor.setPassword(password);
    return encryptor.decrypt(encryptedMessage);
  }

  /**
   * Generates a random string with the provided length.
   * 
   * @param length
   * @return
   */
  public static String getRandomString(int length) {
    StringBuffer retObj = new StringBuffer();
    String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";

    Random random = new Random(System.currentTimeMillis());

    for (int i = 0; i < length; i++) {
      int pos = random.nextInt(letters.length());
      retObj.append(letters.charAt(pos));
    }
    return retObj.toString();
  }

  public static String merge(String[] strArray) {
    return merge(strArray, ' ');
  }

  public static String merge(String[] strArray, char separator) {
    String retObj = null;

    if (strArray != null && strArray.length > 0) {
      retObj = "";
      for (String str : strArray) {
        retObj += str + separator;
      }
      retObj = retObj.substring(0, retObj.length() - 1); // remove final separator character
    }
    return retObj;
  }

  /**
   * Perform URL encode, with default 'UTF-8' encoding.
   * 
   * @param text
   * @return
   */
  public static String encodeUrl(String text) {
    return encodeUrl(text, ENCODING_UTF_8);
  }

  /**
   * Perform URL encoding, with designated encoding.
   * 
   * @param text
   * @param encoding
   * @return
   */
  public static String encodeUrl(String text, String encoding) {
    try {
      return URLEncoder.encode(text, encoding);
    } catch (UnsupportedEncodingException e) {
      // ...
    }
    return null;
  }

  /**
   * Perform URL decoding, with default UTF-8 encoding.
   * 
   * @param text
   * @return
   */
  public static String decodeUrl(String text) {
    return decodeUrl(text, ENCODING_UTF_8);
  }

  public static String decodeUrl(String text, String encoding) {
    try {
      return URLDecoder.decode(text, encoding);
    } catch (UnsupportedEncodingException e) {
      // ...
    }
    return null;
  }

  /**
   * Converts from an array of byes into Hex String
   * 
   * @param bytes
   * @return
   */
  public static String byteArrayToHexString(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  /**
   * Replaces variables of a text by values using a {@link Map}, where the <i>key</i> represents the name of the variable and the
   * <i>value</i> represents the value that will replace the first. Variables must be between curls and preceded by the symbol
   * <i>$</i> and the keyword <i>var</i>. For the variable called <i>variable</i>, its occurrence on the text should appear as
   * <i>$var{variable}</i>. If a <i>value</i> is not found on the {@link Map} for a certain <i>variable (key)</i>, it will either
   * remove it from the text or simply leave it untouched, depending on the option selected.
   * 
   * @param mapParams
   *          the {@link Map} of <i>variables</i> and <i>replacing values</i>,
   * @param text
   *          a {@link String} of the text to be parsed.
   * @param keepNullVar
   *          <b>true</b> will leave <i>variables</i> with no values untouched, while, <b>false</b> will remove them from text.
   * @return a {@link String} parsed.
   */
  public static String parseTextWithMap(Map<String, String> mapParams, String text, boolean keepNullVar) {

    String patPreffix = "\\$var\\{";
    String nonSpaceCharsExp = "\\S*";
    String patSuffix = "\\}";

    Pattern p = Pattern.compile(patPreffix + nonSpaceCharsExp + patSuffix);

    Matcher m = p.matcher(text);
    String param;
    String value;
    String paramValue;

    while (m.find()) {
      param = m.group();
      value = param.replaceFirst(patPreffix, "").replaceFirst(patSuffix, "");
      paramValue = mapParams.get(value);
      if (isNotBlank(paramValue)) {
        text = text.replace(param, paramValue);
      } else if (!keepNullVar) {
        text = text.replace(param, "");
      }
    }
    return text;
  }

  /**
   * Extract subtext from text, using provided regex.
   * 
   * @param text
   * @param regex
   * @return
   */
  public static String extractText(String text, String regex) {
    String retObj = null;
    Pattern pattern = Pattern.compile(regex);

    Matcher matcher = pattern.matcher(text);
    if (matcher.find() && matcher.groupCount() > 0) {
      retObj = matcher.group(1);
    }

    return retObj;
  }

  /**
   * Count occurrences of characters in provided text.
   * 
   * @param text
   * @return
   */
  public static Map<Character, Integer> countOccurences(String text) {
    Map<Character, Integer> retObj = new TreeMap<Character, Integer>();

    if (isNotBlank(text)) {
      text = text.trim();

      for (int i = 0; i < text.length(); i++) {
        Character charAt = text.charAt(i);

        if (!Character.isWhitespace(charAt)) {
          Integer count = retObj.get(charAt);
          if (count == null) {
            count = 0;
          }
          retObj.put(charAt, ++count);
        }
      }
    }
    return retObj;
  }

  /**
   * List occurrences of characters in provided text.
   * 
   * @param text
   * @return
   */
  public static List<Character> listOccurrences(String text) {
    List<Character> retObj = new ArrayList<Character>();

    if (isNotBlank(text)) {
      text = text.trim();

      for (int i = 0; i < text.length(); i++) {
        Character charAt = text.charAt(i);

        if (charAt != null && !Character.isWhitespace(charAt) && !retObj.contains(charAt)) {
          retObj.add(charAt);
        }
      }
      Collections.sort(retObj);
    }
    return retObj;
  }

  public static String randomString(int len) {
    Random rnd = new Random();
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++)
      sb.append(baseChars.charAt(rnd.nextInt(baseChars.length())));
    return sb.toString();
  }

  public static List<String> searchLines(String text, String lineStart) {
    return searchLines(text, lineStart, false);
  }

  /**
   * Search line that starts with prefix, from provided text. Text to be searched should have carriage/newline characters. All
   * occurrences are returned.
   * 
   * @param text
   *          The complete text to be searched.
   * @param lineStart
   *          The line 'prefix' to be search (returned line must start with this value).
   * @return Lines that match prefix.
   */
  public static List<String> searchLines(String text, String lineStart, boolean trimStart) {
    List<String> retObj = new ArrayList<>();

    if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(lineStart)) {
      Scanner scanner = null;
      try {
        scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();

          if (trimStart) {
            line = line.trim();
          }
          // process the line
          if (line.startsWith(lineStart)) {
            retObj.add(line);
          }
        }
      } finally {
        if (scanner != null) {
          scanner.close();
        }
      }
    }

    return retObj;
  }

  public static String searchLine(String text, String lineStart) {
    return searchLine(text, lineStart, false);
  }

  /**
   * Search line that starts with prefix, from provided text. Text to be searched should have carriage/newline characters. Only
   * first occurrence is returned.
   * 
   * @param text
   *          The complete text to be searched.
   * @param lineStart
   *          The line 'prefix' to be search (returned line must start with this value).
   */
  public static String searchLine(String text, String lineStart, boolean trimStart) {
    String retObj = null;

    if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(lineStart)) {
      Scanner scanner = null;
      try {
        scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();
          // process the line
          if (trimStart) {
            line = line.trim();
          }

          if (line.startsWith(lineStart)) {
            retObj = line;
            break;
          }
        }
      } finally {
        if (scanner != null) {
          scanner.close();
        }
      }
    }
    return retObj;

  }

  public static Currency getLocalCurrency() {
    Currency retObj = null;

    if (OSUtils.getLocale() != null) {
      retObj = Currency.getInstance(OSUtils.getLocale());
    }
    return retObj;
  }

  public static String getLocalCurrencySymbol() {
    if (getLocalCurrency() != null) {
      return getLocalCurrency().getSymbol();
    }
    return null;
  }

  public static String getLocalCurrencyCode() {
    if (getLocalCurrency() != null) {
      return getLocalCurrency().getCurrencyCode();
    }
    return null;
  }

  public static char getLocalDecimalSeparator() {
    DecimalFormatSymbols dfs = new DecimalFormatSymbols(OSUtils.getLocale());
    return dfs.getDecimalSeparator();
  }

  public static void main(String[] args) {
    // String text = "sadnsamdn
    // Ã§Ã§Z{8â•šÂ§AÃ„ÃƒeiÃµBEÃ Ã„Ã´Ã¢zÃ Ã§ÃªÃ«Ã¨Ã¯Ã®Ã¬Ã„Ã…6Ã‰Ã¦Ã†Ã¦Ã†Ã´Ã²Ã»Ã¿Ã¹Ã¿Ã¿Ã–Ã¼psaod
    // sad salkdjsa dopsid poi ewmnd ,smnd sadpoisadposa
    // d";
    // System.out.println(listOccurrences(text));
    // System.out.println(countOccurences(text));
    // System.out.println(urlDecode("endere%C3%A7o"));
    // System.out.println(randomString(100));

    // String searchLineTxt = "Number of unique stations: 228\n" + "Number
    // of unique programs: 27244\n"
    // + "Number of unique series titles: 9955\n" + "Number of programs with
    // a series ID or Series Flag: 24515\n"
    // + "Number of programs with a series ID but no series flag: 0\n"
    // + "Number of programs with a series flag but no series ID: 0\n" + "";

    // System.out.println(searchLines(searchLineTxt, "Number of"));
    // System.out.println(getLocalCurrencySymbol());

    String[] tokenize = tokenize("ABC;CED/DEF.GHI-XYZ", new String[] { ";", ",", "/" });

    System.out.println(tokenize);
  }

  public static String cleanWhitespace(String str) {
    if (str != null) {
      return str.trim().replaceAll("\u00a0", "");
    }
    return null;
  }

}