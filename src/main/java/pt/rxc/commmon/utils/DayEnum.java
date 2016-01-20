package pt.rxc.commmon.utils;

import org.joda.time.DateTimeConstants;

/**
 * Day enumeration, including their order, as defined in Joda's
 * DateTimeConstants.
 * 
 * @author ruben.correia
 *
 */
public enum DayEnum {
	MONDAY(DateTimeConstants.MONDAY), 
	TUESDAY(DateTimeConstants.TUESDAY), 
	WEDNESDAY(DateTimeConstants.WEDNESDAY), 
	THURSDAY(DateTimeConstants.THURSDAY), 
	FRIDAY(DateTimeConstants.FRIDAY), 
	SATURDAY(DateTimeConstants.SATURDAY), 
	SUNDAY(DateTimeConstants.SUNDAY);

	int order;

	DayEnum(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	public static DayEnum getFromOrder(int order) {
		DayEnum retObj = null;

		for (DayEnum dayEnum : values()) {
			if (dayEnum.getOrder() == order) {
				retObj = dayEnum;
				break;
			}
		}

		return retObj;
	}
}
