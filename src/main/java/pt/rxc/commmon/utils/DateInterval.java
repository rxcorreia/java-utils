/**
 * 
 */
package pt.rxc.commmon.utils;

import java.util.Date;

/**
 * This class represents a date interval, consisting in a start and end date
 * defining that interval.
 * 
 * @author ruben.correia
 *
 */
public class DateInterval {

	private Date start;
	private Date end;

	public DateInterval() {

	}

	/**
	 * @param start
	 * @param end
	 */
	public DateInterval(Date start, Date end) {
		super();
		this.start = start;
		this.end = end;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "DateInterval [start=" + start + ", end=" + end + "]";
	}

}
