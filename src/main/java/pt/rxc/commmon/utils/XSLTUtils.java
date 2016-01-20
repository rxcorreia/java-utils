package pt.rxc.commmon.utils;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * This class provides methods to work with XSLT.
 * 
 * @author andre.macedo
 */
public class XSLTUtils {
  private static final Logger LOGGER = Logger.getLogger(XSLTUtils.class);

  private File xslt = null;
  private Map<String, String> parameters = new HashMap<String, String>();

  /**
   * Default constructor.
   * 
   * Check if the given XSLT exists.
   * 
   * @param xsltSource
   *          the xslt source
   */
  public XSLTUtils(String xsltSource) {
    xslt = new File(xsltSource);
    if (!xslt.exists()) {
      throw new InvalidParameterException("Unable to find XSLT " + xsltSource + " file.");
    }
  }

  /**
   * Invalid constructor.
   */
  public XSLTUtils() {
    throw new UnsupportedOperationException("Invalid constructor!");
  }

  /**
   * Add a parameter to pass to XSLT.
   * 
   * @param name
   *          the name of the parameter
   * @param value
   *          the value of the parameter
   */
  public void addParameter(String name, String value) {
    parameters.put(name, value);
  }

  /**
   * Execute de xSLT and return the output.
   * 
   * @param inputXML
   *          the input XML
   * @return the XSLT execution output
   */
  public String execute(String inputXML) {
    String retObj = null;

    try {
      StreamSource xsltSource = new StreamSource(xslt);
      Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);

      for (String parameterName : parameters.keySet()) {
        transformer.setParameter(parameterName, parameters.get(parameterName));
      }
      // transformer.setParameter('baseUrl', myUtils.getBaseUrl());

      Source input = new StreamSource(new StringReader(inputXML));
      StringWriter outputSW = new StringWriter();
      Result output = new StreamResult(outputSW);

      transformer.transform(input, output);

      retObj = outputSW.getBuffer().toString();
    } catch (Exception e) {
      InternalError exception = new InternalError("Error executing XSLT.");
      LOGGER.error(exception.getMessage(), e);
      throw exception;
    }

    return retObj;
  }
}