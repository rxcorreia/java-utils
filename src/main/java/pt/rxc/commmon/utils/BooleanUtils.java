package pt.rxc.commmon.utils;

/**
 * Utility class for Boolean operations.
 * 
 * @author ruben.correia
 *
 */
public class BooleanUtils {
	/**
	 * Checks if the object is one of the accepted boolean types for "true".
	 * 
	 * @param obj
	 *            The object to parse.
	 * @return The parsed object as a boolean value.
	 */
	public static boolean parse(Object obj) {
		boolean retVal = false;
		if (obj != null) {
			retVal = StringUtils.isOneOfIgnoreCase(obj.toString(), "true", "1", "s", "sim", "y", "yes");
		}
		return retVal;
	}

	/**
	 * Check if two objects are the same boolean value.
	 * 
	 * @param bool1
	 * @param bool2
	 * @return
	 */
	public static boolean equals(Boolean bool1, Object bool2) {
		if (bool1 != null) {
			return (bool2 instanceof Boolean && bool1.equals(bool2));
		} else {
			return bool2 == null;
		}
	}

	/**
	 * Convert boolean value to int value.
	 * 
	 * @param boolVal
	 *            <code>1</code> if 'true', <code>0</code> if 'false'.
	 * @return
	 */
	public static Integer intValue(Boolean boolVal) {
		if (boolVal != null) {
			return boolVal ? 1 : 0;
		}
		return null;
	}
}
