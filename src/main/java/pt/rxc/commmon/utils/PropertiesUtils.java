package pt.rxc.commmon.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

/**
 * This class contains methods to work with {@link Properties}.
 * 
 * @author ruben.correia
 *
 */
public class PropertiesUtils {

  /**
   * Format map to Properties string, with default separator (',').
   * 
   * @param valueMap
   * @return
   */
  public static <E extends Object> String formatMapAsProperties(Map<String, E> valueMap) {
    return formatMapAsProperties(valueMap, ",");
  }

  /**
   * Format map to Properties string, with provided separator.
   * 
   * @param valueMap
   * @param separator
   * @return
   */
  public static <E extends Object> String formatMapAsProperties(Map<String, E> valueMap, String separator) {
    String retObj = null;
    if (valueMap != null) {
      Properties props = convertToProperties(valueMap);
      for (String key : valueMap.keySet()) {
        props.setProperty(key, valueMap.get(key).toString());
      }
      retObj = props.toString().replace("{", "").replace("}", "").replaceAll("\\s", "").replace(",", separator);
    }
    return retObj;
  }

  /**
   * Convert a map to a Properties object.
   * 
   * @param valueMap
   * @return
   */
  public static <E extends Object> Properties convertToProperties(Map<String, E> valueMap) {
    Properties retObj = null;
    if (valueMap != null) {
      retObj = new Properties();

      for (String key : valueMap.keySet()) {
        retObj.setProperty(key, valueMap.get(key).toString());
      }
    }
    return retObj;
  }

  /**
   * Convert text content to {@link Properties} object.
   * 
   * @param propertiesContent
   * @param separator
   * @return
   * @throws IOException
   */
  public static Properties convertToProperties(String propertiesContent, String separator) throws IOException {
    Properties retObj = null;

    if (propertiesContent != null && StringUtils.isNotBlank(separator)) {
      propertiesContent = propertiesContent.replace(separator, "\r\n");

      retObj = new Properties();
      retObj.load(new StringReader(propertiesContent));
    }
    return retObj;
  }

  /**
   * Get properties value from text.
   * 
   * @param propertiesContent
   * @param separator
   * @param key
   * @return
   * @throws IOException
   */
  public static String getPropertiesValue(String propertiesContent, String separator, String key) throws IOException {
    String retObj = null;

    if (propertiesContent != null && StringUtils.isNotBlank(separator)) {
      Properties props = convertToProperties(propertiesContent, separator);
      if (props != null) {
        retObj = props.getProperty(key);
      }
    }
    return retObj;
  }
}