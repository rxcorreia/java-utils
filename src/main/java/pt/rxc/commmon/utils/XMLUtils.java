package pt.rxc.commmon.utils;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.thoughtworks.xstream.XStream;

/**
 * This class provides utilities to work with XML Files.
 * 
 * @author andremacedo
 * @author ruben.correia
 */
public class XMLUtils {
	private static final Logger LOGGER = Logger.getLogger(XMLUtils.class);

	private static final String CDATA_TAG = "<![CDATA[{0}]]>";

	/**
	 * Default constructor. This class should not be instantiated.
	 */
	protected XMLUtils() {
	}

	/**
	 * The next code store the jaxb context to be used by all threads. Just
	 * create a new jaxb context if the current context doesn't contain the
	 * given class.
	 */
	private static final Map<String, JAXBContext> jaxbContextMap = new HashMap<String, JAXBContext>();

	/**
	 * Get
	 * 
	 * @param classType
	 * @return
	 * @throws JAXBException
	 */
	@SuppressWarnings("rawtypes")
	private static synchronized JAXBContext getJaxbContext(Class... classTypes) throws JAXBException {
		JAXBContext retObj = null;

		String key = "";
		for (Class classType : classTypes) {
			if (!"".equals(key)) {
				key += ";";
			}
			key += classType.getName();
		}

		retObj = jaxbContextMap.get(key);
		if (retObj == null) {
			retObj = JAXBContext.newInstance(classTypes);
			jaxbContextMap.put(key, retObj);
		}

		return retObj;
	}

	/**
	 * This method makes the conversion XML -> JAVA with JAXB.
	 * 
	 * @param xml
	 *            The XML to convert.
	 * @param classType
	 *            The target class to convert.
	 * @param <E>
	 *            Class type that resolves to the given XML's NameSpace.
	 * @return The java object representation of the passed XML.
	 * @throws JAXBException
	 *             If some problem occur while making the conversion.
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getObjectByXML(String xml, Class<E> classType) throws JAXBException {
		E retObj = null;
		if (xml != null) {
			Unmarshaller unmarshaller = getJaxbContext(classType).createUnmarshaller();
			retObj = (E) unmarshaller.unmarshal(new StringReader(stripNonValidXMLCharacters(xml)));
		}
		return retObj;
	}

	/**
	 * Make the conversion Java -> XML with JAXB.
	 * 
	 * @param obj
	 *            The object to convert.
	 * @param formated
	 *            Indicates if the XML should be formated.
	 * @return The XML that represents the object.
	 * @throws JAXBException
	 *             If some problem occur while making the conversion.
	 */
	public static String getXMLByObject(Object obj, boolean formated) throws JAXBException {
		return getXMLByObject(obj, formated, obj.getClass());
	}

	/**
	 * Make the conversion Java -> XML with JAXB.
	 * 
	 * @param obj
	 *            The object to convert.
	 * @param formated
	 *            Indicates if the XML should be formated.
	 * @param classes
	 *            Additional classes to add to the JAXB. useful when there is
	 *            no @XmlSeeAlso in the java classes.
	 * @return The XML that represents the object.
	 * @throws JAXBException
	 *             If some problem occurs while making the conversion.
	 */
	public static String getXMLByObject(Object obj, boolean formated, @SuppressWarnings("rawtypes") Class... classes)
			throws JAXBException {
		String retObj = null;

		Marshaller marshaller = getJaxbContext(classes).createMarshaller();

		// TODO
		// marshaller.setProperty("com.sun.xml.bind.characterEscapeHandler", new
		// SmartCharacterEscapeHandler());

		StringWriter sw = new StringWriter();
		marshaller.marshal(obj, sw);
		if (!formated) {
			retObj = stripNonValidXMLCharacters(sw.toString());
		} else {
			try {
				retObj = prettyFormat(stripNonValidXMLCharacters(sw.toString()));
			} catch (Exception e) {// $NOSONAR$
				retObj = stripNonValidXMLCharacters(sw.toString());
				LOGGER.warn("Error formatting XML string.", e);
			}
		}

		return stripNonValidXMLCharacters(retObj);
	}

	/**
	 * Enclose provided text in CDATA tag. Text in CDATA tag is not parsed as
	 * normal XML.
	 * 
	 * @param text
	 * @return Text in CDATA tag.
	 */
	public static String tagCDataSection(String text) {
		if (text == null) {
			text = "";
		}
		return MessageFormat.format(CDATA_TAG, text);
	}

	/**
	 * Strip non valid XML characters.
	 * 
	 * @param in
	 *            the string
	 * @return the new string
	 */
	public static String stripNonValidXMLCharacters(String in) {
		StringBuffer out = new StringBuffer(); // Used to hold the output.
		char current; // Used to reference the current character.

		if (in == null || ("".equals(in)))
			return ""; // vacancy test.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
									// here; it should not happen.
			if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}

	/**
	 * Format the provided XML input with a default indentation of <i>2</i>.
	 * 
	 * @param input
	 *            XML input to format.
	 * @return Formatted XML.
	 * @throws TransformerException
	 *             if some problem occur while processing the xml
	 * @see #prettyFormat(String, int)
	 */
	public static String prettyFormat(String input) throws TransformerException {
		return prettyFormat(input, 2);
	}

	/**
	 * Format the provided XML input.
	 * 
	 * @param input
	 *            XML input to format.
	 * @param indent
	 *            Indentation to use on formatted XML.
	 * @return Formatted XML.
	 * @throws TransformerException
	 *             if some problem occur while processing the xml
	 * @see #prettyFormat(String)
	 */
	public static String prettyFormat(String input, Integer indent) throws TransformerException {
		if (input != null) {
			Source xmlInput = new StreamSource(new StringReader(input));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
					String.valueOf(indent == null ? 2 : indent));
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		}
		return input;
	}

	/**
	 * Get (simple) XML string representation of object using XStream.<br />
	 * <br />
	 * <strong>Note:</strong> The provided XML representation is to be
	 * considered incomplete and should be used only by entities that have
	 * knowledge of serialized object's class.
	 * 
	 * @param obj
	 *            The object to be serialized to XML.
	 * @return The XML string representation of the object, as provided by
	 *         XStream.
	 */
	public static String getSimpleXMLFromObject(Object obj) {
		String retObj = null;
		if (obj != null) {
			retObj = new XStream().toXML(obj);
		}
		return retObj;
	}

	/**
	 * Get object from its (simple) XML representation. The XML representation
	 * is the one provided by XStream.
	 * 
	 * @param objXml
	 *            The XML string representation of the object.
	 * @param classType
	 *            The class of the object.
	 * @return The object represented by provided XML.
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getObjectFromSimpleXML(String objXml, Class<E> classType) {
		E retObj = null;
		if (objXml != null) {
			retObj = (E) new XStream().fromXML(objXml);
		}
		return retObj;
	}

	public static void saveXMLObjectToFile(String filename, Object obj, String schemaLocation) throws JAXBException {

		Marshaller marshaller = getJaxbContext(obj.getClass()).createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		if (StringUtils.isNotBlank(schemaLocation)) {
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
		}

		marshaller.marshal(obj, new File(filename));

	}

	/**
	 * Converts a {@link Date} into a {@link XMLGregorianCalendar}
	 * 
	 * @param date
	 *            the date to be converted
	 * @return the XMLGregorianCalendar representing the date converted
	 * @throws DatatypeConfigurationException
	 */
	public static XMLGregorianCalendar getXMLGregorianCalendarFromDate(Date date)
			throws DatatypeConfigurationException {
		XMLGregorianCalendar retObj = null;

		if (date != null) {
			GregorianCalendar gregCal = new GregorianCalendar();
			gregCal.setTime(date);

			retObj = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal);
		}

		return retObj;
	}

	/**
	 * Converts a {@link Date} into a {@link XMLGregorianCalendar}
	 * 
	 * @param date
	 *            the date to be converted
	 * @return the XMLGregorianCalendar representing the date converted
	 * @throws DatatypeConfigurationException
	 */
	public static Date getDateFromXMLGregorianCalendar(XMLGregorianCalendar xmlCalendar) {
		Date retObj = null;
		if (xmlCalendar != null) {
			retObj = xmlCalendar.toGregorianCalendar().getTime();
		}
		return retObj;
	}

	/**
	 * Extract the node string representation.
	 * 
	 * @param nodeList
	 * @return
	 */
	public static String getXmlNodeValue(NodeList nodeList) {
		String retObj = null;

		if (nodeList != null && nodeList.getLength() > 0) {
			Element element = (Element) nodeList.item(0);
			if (element != null && element.getChildNodes() != null) {
				NodeList childNodes = element.getChildNodes();
				if (childNodes != null && childNodes.getLength() > 0) {
					retObj = ((Node) childNodes.item(0)).getNodeValue();
				}
			}
		}
		return retObj;
	}

	/**
	 * Extract the node string representation.
	 * 
	 * @param nodeList
	 * @return
	 */
	public static String getXmlNodeAttribute(String attributeName, NodeList nodeList) {
		String retObj = null;

		if (nodeList != null && nodeList.getLength() > 0) {
			Element element = (Element) nodeList.item(0);
			retObj = element.getAttribute(attributeName);
		}
		return retObj;
	}

	/**
	 * Smart implementation for {@link CharacterEscapeHandler}.<br/>
	 * This implementation was based on
	 * {@link com.sun.xml.bind.marshaller.DumbEscapeHandler DumbEscapeHandler},
	 * with the addition of also escaping <i>NewLine</i> and
	 * <i>CarriageReturn</i> characters.
	 * 
	 * @author jose.salgueiro
	 * @author luis.cabral
	 */
	// private static class SmartCharacterEscapeHandler implements
	// com.sun.xml.bind.AccessorFactory CharacterEscapeHandler {
	// @Override
	// @SuppressWarnings("all")
	// public void escape(char[] buf, int start, int len, boolean isAttValue,
	// Writer out) throws IOException {
	// for (int i = start; i < start + len; i++) {
	// switch (buf[i]) {
	// case '&':
	// out.write("&amp;");
	// break;
	// case '<':
	// out.write("&lt;");
	// break;
	// case '>':
	// out.write("&gt;");
	// break;
	// case '\n':
	// out.write("&#010;");
	// break;
	// case '\"':
	// if (isAttValue) {
	// out.write("&quot;");
	// } else {
	// out.write('\"');
	// }
	// break;
	// case '\r':
	// if (i > 0 && buf[i - 1] == '\n') {
	// continue;
	// } else if (i < start + len && buf[i + 1] == '\n') {
	// continue;
	// }
	// out.write("&#013;");
	// break;
	// default:
	// if (buf[i] > '\u007f') {
	// out.write("&#");
	// out.write(Integer.toString(buf[i]));
	// out.write(';');
	// } else {
	// out.write(buf[i]);
	// }
	// }
	// }
	// }
	// }
}
