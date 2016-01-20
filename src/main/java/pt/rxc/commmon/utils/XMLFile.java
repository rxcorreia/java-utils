package pt.rxc.commmon.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * This provide a wrapper to allow us to work if a XML file with xpath expressions.
 * 
 * @author ruben.correia
 */
public class XMLFile {
  private static final Logger LOGGER = Logger.getLogger(XMLFile.class);

  private File file;
  private Document document;
  private String xmlContent;

  /**
   * Default constructor.
   */
  public XMLFile() {
  }

  /**
   * Initialize the given constructor with the given filenme
   * 
   * @param filename
   *          the path to the file
   */
  public XMLFile(String filename) {
    file = new File(filename);
    if (!file.exists() || !file.isFile()) {
      throw new IllegalArgumentException("The provided file (" + file.getAbsolutePath() + ") doest not exist.");
    }
  }

  /**
   * Load content from XML Document.
   * 
   * @param xmlContent
   *          the xml content
   */
  public void loadContentFromXML(String xmlContent) {
    this.xmlContent = xmlContent;
  }

  /**
   * Initialize the file with the given file.
   * 
   * @param file
   *          the file
   */
  public XMLFile(File file) {
    this.file = file;
    if (!file.exists() || !file.isFile()) {
      throw new IllegalArgumentException("The provided file (" + file.getAbsolutePath() + ") doest not exist.");
    }
  }

  /**
   * @return the file
   */
  public File getFile() {
    return file;
  }

  /**
   * @param file
   *          the file to set
   */
  public void setFile(File file) {
    this.file = file;
  }

  /**
   * @return the document
   */
  public Document getDocument() {
    if (document == null) {
      if (file == null && StringUtils.isBlank(xmlContent)) {
        throw new IllegalArgumentException("XML file and content no set.");
      }
      try {
        SAXBuilder builder = new SAXBuilder();
        if (file != null) {
          document = (Document) builder.build(new FileInputStream(file));
        } else {
          document = (Document) builder.build(new StringReader(xmlContent));
        }
      } catch (Exception e) {
        LOGGER.error("Error parsing XML file: " + (file != null ? file.getAbsolutePath() : null), e);
        throw new IllegalArgumentException("Invalid XML file " + file.getAbsolutePath());
      }
    }
    return document;
  }

  /**
   * Get the single value for the given xpath expression.
   * 
   * @param xPathExpression
   *          the xpath expression
   * @return the value
   * @throws JDOMException
   *           if some unexpected error occur
   */
  public String getSingleValue(String xPathExpression) throws JDOMException {
    String retObj = null;

    List<String> values = getValue(xPathExpression);
    if (values != null && values.size() > 0) {
      retObj = values.get(0);
    }

    return retObj;
  }

  /**
   * Get the values which are associated to the given xpath expression.
   * 
   * @param xPathExpression
   *          the xpath expression
   * @return the list of values
   * @throws JDOMException
   *           if some unexpected error occur
   */
  public List<String> getValue(String xPathExpression) throws JDOMException {
    List<String> retObj = new ArrayList<String>();

    // TODO Replace deprecated XPath
    // @SuppressWarnings("rawtypes")
    // List selectNodes = XPath.selectNodes(getDocument(), xPathExpression);
    // for (Object object : selectNodes) {
    // if (object instanceof Element) {
    // Element element = (Element) object;
    // retObj.add(element.getValue());
    // } else if (object instanceof Attribute) {
    // Attribute attribute = (Attribute) object;
    // retObj.add(attribute.getValue());
    // } else {
    // throw new JDOMException("Object not mapped: " +
    // object.getClass().getSimpleName());
    // }
    // }

    return retObj;
  }
}
