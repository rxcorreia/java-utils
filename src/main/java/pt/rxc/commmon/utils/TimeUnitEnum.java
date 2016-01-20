package pt.rxc.commmon.utils;

/**
 * Enumeration values for date range operators.
 * 
 * @author ruben.correia
 * 
 */
public enum TimeUnitEnum {

	MILLISECONDS, 
	SECONDS, 
	MINUTES, 
	HOURS, 
	DAYS, 
	WEEKS, 
	MONTHS, 
	YEARS, 
	CENTURIES;

	/**
	 * Check if object instance matches fields in provided array.
	 * 
	 * @param fields
	 * @return
	 */
	public boolean matches(TimeUnitEnum... fields) {
		if (fields != null) {
			for (TimeUnitEnum field : fields) {
				if (field != null && this == field) {
					return true;
				}
			}
		}
		return false;
	}
}
