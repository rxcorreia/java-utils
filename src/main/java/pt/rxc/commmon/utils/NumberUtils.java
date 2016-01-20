/**
 * 
 */
package pt.rxc.commmon.utils;

import java.text.DecimalFormat;

/**
 * @author ruben.correia
 *
 */
public class NumberUtils {

	/**
	 * Attempts to parse the given object to an <b>int</b> value, defaulting to
	 * <i>-1</i> if unable to do so for any reason.
	 * 
	 * @param obj
	 *            The object to parse.
	 * @return The parsed object as an int value.
	 * 
	 * @see ConverterUtils#parseToInt(Object,int)
	 */
	public static int parseToInt(Object obj) {
		return parseToInt(obj, -1);
	}

	/**
	 * Attempts to parse the given object to an <b>int</b> value, defaulting to
	 * the given value if unable to do so for any reason.
	 * 
	 * @param obj
	 *            The object to parse.
	 * @param defaultValue
	 *            The default value to return if the object is invalid
	 * @return The parsed object as an int value.
	 * 
	 * @see ConverterUtils#parseToInt(Object)
	 */
	public static int parseToInt(Object obj, int defaultValue) {
		int ret = defaultValue;
		if (obj != null && StringUtils.isNotBlank(obj.toString())) {
			try {
				ret = Integer.parseInt(obj.toString());
			} catch (Exception ex) {
			}
		}
		return ret;
	}

	/**
	 * Attempts to parse the given object to a <b>long</b> value, defaulting to
	 * <i>-1</i> if unable to do so for any reason.
	 * 
	 * @param obj
	 *            The object to parse.
	 * @return The parsed object as a long value.
	 * 
	 * @see ConverterUtils#parseToLong(Object, long)
	 */
	public static long parseToLong(Object obj) {
		return parseToLong(obj, -1);
	}

	/**
	 * Attempts to parse the given object to a <b>long</b> value, defaulting to
	 * the given value if unable to do so for any reason.
	 * 
	 * @param obj
	 *            The object to parse.
	 * @param defaultValue
	 *            The default value to return if the object is invalid
	 * @return The parsed object as a long value.
	 * 
	 * @see ConverterUtils#parseToLong(Object)
	 */
	public static long parseToLong(Object obj, long defaultValue) {
		long ret = defaultValue;
		if (obj != null && StringUtils.isNotBlank(obj.toString())) {
			try {
				ret = Long.parseLong(obj.toString());
			} catch (Exception ex) {
			}
		}
		return ret;
	}

	/**
	 * Attempts to parse the given object to a <b>double</b> value, defaulting
	 * to <i>-1</i> if unable to do so for any reason.
	 * 
	 * @param obj
	 *            The object to parse.
	 * @return The parsed object as a double value.
	 * 
	 * @see ConverterUtils#parseToDouble(Object, long)
	 */
	public static double parseToDouble(Object obj) {
		return parseToDouble(obj, -1);
	}

	/**
	 * Attempts to parse the given object to a <b>double</b> value, defaulting
	 * to the given value if unable to do so for any reason.
	 * 
	 * @param obj
	 *            The object to parse.
	 * @param defaultValue
	 *            The default value to return if the object is invalid
	 * @return The parsed object as a double value.
	 * 
	 * @see ConverterUtils#parseToDouble(Object)
	 */
	public static double parseToDouble(Object obj, double defaultValue) {
		double ret = defaultValue;
		if (obj != null && StringUtils.isNotBlank(obj.toString())) {
			try {
				ret = Double.parseDouble(obj.toString());
			} catch (Exception ex) {
			}
		}
		return ret;
	}

	public static Double round(Double number, Integer precision) {
		Double retObj = null;
		if (number != null) {
			if (precision == null)
				precision = 0;

			Double decimalPlaces = Math.pow(10, precision);
			retObj = (double) Math.round(number * decimalPlaces) / decimalPlaces;

		}
		return retObj;
	}

	public static double round(double number, int precision) {
		double decimalPlaces = Math.pow(10, precision);
		return (double) Math.round(number * decimalPlaces) / decimalPlaces;
	}

	/**
	 * Compares 2 Integer's, avoiding NullPointer.
	 * 
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public static boolean equals(Integer a, Integer b) {

		if (a == null) {
			return (b == null);
		}
		if (b == null) {
			return (a == null);
		}
		return a.intValue() == b.intValue();
	}

	/**
	 * Compares 2 Doubles's, avoiding NullPointer.
	 * 
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public static boolean equals(Double a, Double b) {

		if (a == null) {
			return (b == null);
		}
		if (b == null) {
			return (a == null);
		}
		return a.doubleValue() == b.doubleValue();
	}

	/**
	 * Compares 2 Boolean's, avoiding NullPointer.
	 * 
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public static boolean equals(Boolean a, Boolean b) {

		if (a == null) {
			return (b == null);
		}
		if (b == null) {
			return (a == null);
		}
		return a.booleanValue() == b.booleanValue();
	}

	/**
	 * Returns true if the string contains a parsable integer.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isStringNumber(String str) {

		if (StringUtils.isBlank(str)) {
			return false;
		}

		try {
			Integer.parseInt(str);
		} catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}

	/**
	 * Parse a double from a string. Method ensures decimal separator is
	 * correct, replacing if locale is not '.'.
	 * 
	 * @param str
	 * @return
	 */
	public static Double parseDouble(String str) {
		try {
			if (StringUtils.getLocalDecimalSeparator() != '.')
				return Double.parseDouble(str.replace(StringUtils.getLocalDecimalSeparator(), '.'));
		} catch (Exception e) {
			// ...
		}
		return null;
	}

	/**
	 * Returns a normalized String for a given Double value, with the specified
	 * format.
	 * 
	 * @param value
	 *            - The Double number to convert
	 * @param format
	 *            - The format to parse the Double.
	 * @return String
	 */
	public static String getFormatedStringForDouble(Double value, String format) {
		String retObj = "";

		if (!StringUtils.isBlank(format) && value != null) {
			DecimalFormat df = new DecimalFormat(format);
			retObj = df.format(value);
		}

		return retObj;
	}

	/**
	 * Parse the provided {@link Number} so that it will have the provided
	 * minimum length, filling with left <code>0</code> when necessary.
	 * 
	 * @param value
	 * @param minLength
	 * @return Parsed number.
	 */
	public static String parseToString(Number value, int minLength) {
		String ret = "";
		if (value != null) {
			ret = value.toString();
		}
		while (ret.length() < minLength) {
			ret = "0" + ret;
		}
		return ret;
	}
}
