package pt.rxc.commmon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

/**
 * Utility class to work with dates. Draws extensively from Joda Time API.
 * 
 * @author ruben.correia
 */
public class DateUtils {

  public static final String DATABASE_SIMPLE_DATE_FORMAT = "%m-%d-%Y";
  public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
  public static final String SIMPLE_TIME_FORMAT = "HH:mm:ss";
  public static final String SIMPLE_DATETIME_FORMAT = SIMPLE_DATE_FORMAT + " " + SIMPLE_TIME_FORMAT;

  /**
   * The next thread local stores a date for each thread. It's used to calculate the difference between a start and a end date.
   */
  private static final ThreadLocal<Date> COUNT_DATE = new ThreadLocal<Date>();

  private static final ThreadLocal<Map<String, Date>> COUNT_DATE_MAP = new ThreadLocal<Map<String, Date>>();

  private static Map<TimeUnitEnum, DurationFieldType> timeUnitToDurationFieldMap = new HashMap<TimeUnitEnum, DurationFieldType>();

  static {
    timeUnitToDurationFieldMap.put(TimeUnitEnum.MILLISECONDS, DurationFieldType.millis());
    timeUnitToDurationFieldMap.put(TimeUnitEnum.SECONDS, DurationFieldType.seconds());
    timeUnitToDurationFieldMap.put(TimeUnitEnum.MINUTES, DurationFieldType.minutes());
    timeUnitToDurationFieldMap.put(TimeUnitEnum.HOURS, DurationFieldType.hours());
    timeUnitToDurationFieldMap.put(TimeUnitEnum.DAYS, DurationFieldType.days());
    timeUnitToDurationFieldMap.put(TimeUnitEnum.WEEKS, DurationFieldType.weeks());
    timeUnitToDurationFieldMap.put(TimeUnitEnum.MONTHS, DurationFieldType.months());
    timeUnitToDurationFieldMap.put(TimeUnitEnum.YEARS, DurationFieldType.years());
    timeUnitToDurationFieldMap.put(TimeUnitEnum.CENTURIES, DurationFieldType.centuries());
  }

  /**
   * Convert a date in a given timezone to a UTC date.
   * 
   * @param date
   *          the date
   * @param timezone
   *          the timezone
   * @return the converted utc date
   */
  public static Date getUTCDate(Date date, String timezone) {
    if (date == null || StringUtils.isBlank(timezone)) {
      return null;
    }

    long utcTimeMillis = date.getTime();

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timezone));
    calendar.setTimeInMillis(utcTimeMillis);
    TimeZone fromTimeZone = calendar.getTimeZone();
    TimeZone toTimeZone = TimeZone.getTimeZone("UTC");

    calendar.setTimeZone(fromTimeZone);
    calendar.add(Calendar.MILLISECOND, fromTimeZone.getRawOffset() * -1);
    if (fromTimeZone.inDaylightTime(calendar.getTime())) {
      calendar.add(Calendar.MILLISECOND, calendar.getTimeZone().getDSTSavings() * -1);
    }

    calendar.add(Calendar.MILLISECOND, toTimeZone.getRawOffset());
    if (toTimeZone.inDaylightTime(calendar.getTime())) {
      calendar.add(Calendar.MILLISECOND, toTimeZone.getDSTSavings());
    }

    return calendar.getTime();
  }

  public static Date addDay(Date referenceDate) {
    Date retObj = null;
    if (referenceDate != null) {
      MutableDateTime refDate = new MutableDateTime(referenceDate.getTime());
      refDate.addDays(1);
      retObj = refDate.toDate();
    }
    return retObj;
  }

  /**
   * Convert a time string in format HH:MM:SS in a milliseconds duration.
   * 
   * @param time
   *          the time
   * @return
   */
  public static long convertTimeToDurationMilliseconds(String time) {
    long retObj = 0;

    if (time != null) {
      try {
        Integer hours = Integer.valueOf(time.substring(0, 2));
        Integer minutes = Integer.valueOf(time.substring(3, 5));
        Integer seconds = Integer.valueOf(time.substring(6, 8));
        retObj += hours * 60 * 60 * 1000;
        retObj += minutes * 60 * 1000;
        retObj += seconds * 1000;
      } catch (NumberFormatException e) {

      }
    }

    return retObj;
  }

  /**
   * Convert miliseconds to display (00:00:00:00)
   * 
   * @param miliseconds
   *          the number of miliseconds
   * @param fps
   *          the number of fps
   * @return the formated string
   */
  public static String convertMilisecondsToDisplay(Integer milliseconds, int fps) {
    String retObj = "";

    int hour = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    int minutes = ((milliseconds / (1000 * 60)) % 60);
    int seconds = (int) (milliseconds / 1000) % 60;
    int frames = convertMilisecondsRemainderToFrames(milliseconds, fps);
    retObj += hour < 10 ? "0" + hour : hour;
    retObj += ":" + (minutes < 10 ? "0" + minutes : minutes);
    retObj += ":" + (seconds < 10 ? "0" + seconds : seconds);
    retObj += ":" + (frames < 10 ? "0" + frames : frames);

    return retObj;
  }

  /**
   * Convert frames to miliseconds.
   * 
   * @param timeInFrames
   * @param fps
   * @return
   */
  public static int convertMilisecondsRemainderToFrames(Integer miliseconds, int fps) {
    int retObj = 0;

    if (miliseconds != null) {
      retObj = (miliseconds % 1000) / (1000 / fps);
    }

    return retObj;
  }

  private static void initializeCountDateMap() {
    if (COUNT_DATE_MAP.get() == null) {
      COUNT_DATE_MAP.set(new HashMap<String, Date>());
    }
  }

  /**
   * Start counting timer for the given identifier.
   * 
   * @param identifier
   *          the identifier
   */
  public static void startCount(String identifier) {
    if (StringUtils.isBlank(identifier)) {
      throw new IllegalArgumentException("Identifier can't not null!");
    }

    initializeCountDateMap();

    COUNT_DATE_MAP.get().put(identifier, new Date());
  }

  /**
   * Start counting time.
   */
  public static void startCount() {
    COUNT_DATE.set(new Date());
  }

  /**
   * Return the amount of time (in ms) beetween the last call of startCount for this identifier.
   * 
   * @param identifier
   *          the identifier
   * @return the amount of time
   */
  public static long endCount(String identifier) {
    if (StringUtils.isBlank(identifier)) {
      throw new IllegalArgumentException("Identifier can't not null!");
    }

    long retObj = -1;

    if (COUNT_DATE_MAP.get() != null) {
      Date startDate = COUNT_DATE_MAP.get().get(identifier);
      if (startDate != null) {
        retObj = new Date().getTime() - startDate.getTime();
      }
    }

    return retObj;
  }

  /**
   * Print the global amount of time.
   * 
   * @param logger
   *          the logger to use
   */
  public static void printEndCount(Logger logger) {
    logger.info("Total time: " + endCount() + " ms!");
  }

  /**
   * Print the amount of time for the given identifier.
   * 
   * @param logger
   *          the logger to use
   * @param identifier
   *          the identifier
   */
  public static void printEndCount(Logger logger, String identifier) {
    logger.info(identifier + ": " + endCount(identifier) + " ms!");
  }

  /**
   * Return the amout of time (is ms) between the last call of startCount by this thread.
   * 
   * @return the amout of time between the call of startCount and now.
   */
  public static long endCount() {
    long retObj = -1;
    Date startDate = COUNT_DATE.get();
    if (startDate != null) {
      retObj = new Date().getTime() - startDate.getTime();
    }
    return retObj;
  }

  /**
   * Convert a {@link Date} in a {@link XMLGregorianCalendar} date.
   * 
   * @param date
   *          the date to convert
   * @return the converted date
   */
  public static XMLGregorianCalendar convertToXmlGregorianCalendar(Date date) {
    try {
      GregorianCalendar c = new GregorianCalendar();
      c.setTime(date);
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    } catch (DatatypeConfigurationException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  /**
   * Convert a {@link XMLGregorianCalendar} to a {@link Date}.
   * 
   * @param date
   *          the date to convert
   * @return the converted date
   */
  public static Date convertToDate(XMLGregorianCalendar date) {
    return date.toGregorianCalendar().getTime();
  }

  /**
   * Generic method to retrieve the time span between the provided time and the system's current time.
   * 
   * @param startTime
   *          Start time to set.
   * @return The calculated time between the start time and now, formatted for pretty output.
   * @see #outputDuration(long, long)
   */
  public static String outputDuration(final long startTime) {
    return outputDuration(startTime, System.currentTimeMillis());
  }

  /**
   * Generic method to retrieve the time span between the provided start and end times.
   * 
   * @param startTime
   *          Start time to set.
   * @param endTime
   *          End time to set.
   * @return The calculated time between the start time and end time, formatted for pretty output.
   * @see #outputDuration(long)
   */
  public static String outputDuration(final long startTime, final long endTime) {
    final long duration = (endTime - startTime);
    final long second = 1000L;

    String retObj = "";
    if (duration < (second + 1)) {
      retObj += duration + "ms";
    } else {
      retObj += (duration / second);
      if (duration < (second * 10)) {
        retObj += "." + (duration % second);
      }
      retObj += "s";
    }
    return retObj;
  }

  /**
   * Get range start date. Assumes reference date is 'NOW'.
   * 
   * @param rangeOperator
   *          The range operator.
   * @return The date corresponding to range's start
   */
  public static Date getStartOfDateRange(DatePeriodEnum datePeriod) {
    return getStartOfDateRange(datePeriod, new Date());
  }

  /**
   * Get range start date.
   * 
   * @param rangeOperator
   *          The range operator.
   * @param referenceDate
   *          The reference date to work with.
   * @return The date corresponding to range's start
   */
  public static Date getStartOfDateRange(DatePeriodEnum datePeriod, Date referenceDate) {
    return getDateRange(datePeriod, referenceDate).getStart();
  }

  /**
   * Get range end date. Assumes reference date is 'NOW'.
   * 
   * @param rangeOperator
   *          The range operator.
   * @return The date corresponding to range's end
   */
  public static Date getEndOfDateRange(DatePeriodEnum datePeriod) {
    return getEndOfDateRange(datePeriod, new Date());
  }

  /**
   * Get range end date.
   * 
   * @param rangeOperator
   *          The range operator.
   * @param referenceDate
   *          The reference date to work with.
   * @return The date corresponding to range's end
   */
  public static Date getEndOfDateRange(DatePeriodEnum datePeriod, Date referenceDate) {
    return getDateRange(datePeriod, referenceDate).getEnd();
  }

  /**
   * Get date range for operator. A {@link DateRangeOperatorEnum} is expected to calculate start and end period. Assumes reference
   * date corresponds to 'NOW'.
   * 
   * @param rangeOperator
   *          The range operator.
   * @return A {@link DateInterval} with period's start/end dates.
   */
  public static DateInterval getDateRange(DatePeriodEnum datePeriod) {
    return getDateRange(datePeriod, new Date());
  }

  /**
   * 
   * @param rangeOperator
   * @param referenceDate
   *          The reference date to work with.
   * @return
   */
  public static DateInterval getDateRange(DatePeriodEnum datePeriod, Date referenceDate) {
    return getDateRange(datePeriod, referenceDate, true);
  }

  /**
   * 
   * @param datePeriod
   * @param referenceDate
   * @param resetSub
   *          Signal if sub-time divisions (minutes, seconds, millis), should be set to 0.
   * @return
   */
  public static DateInterval getDateRange(DatePeriodEnum datePeriod, Date referenceDate, boolean resetSub) {
    DateInterval retObj = null;

    if (datePeriod != null) {
      retObj = new DateInterval();

      if (referenceDate == null) {
        // Default to 'NOW', if not defined
        referenceDate = new Date();
      }
      MutableDateTime startDateTime = new MutableDateTime(referenceDate.getTime());
      MutableDateTime endDateTime = new MutableDateTime(referenceDate.getTime());
      boolean validRangeOperator = true;

      switch (datePeriod) {
      case PREVIOUS_HOUR:
        startDateTime.addHours(-1);
        endDateTime.addHours(-1);
        break;
      case CURRENT_HOUR:
        break;
      case NEXT_HOUR:
        startDateTime.addHours(1);
        endDateTime.addHours(1);
        break;
      case PREVIOUS_DAY:
        startDateTime.addDays(-1);
        endDateTime.addDays(-1);

        startDateTime.setHourOfDay(0);
        endDateTime.setHourOfDay(23);

        break;
      case CURRENT_DAY:
        startDateTime.setHourOfDay(0);
        endDateTime.setHourOfDay(23);
        break;
      case NEXT_DAY:
        startDateTime.addDays(1);
        endDateTime.addDays(1);

        startDateTime.setHourOfDay(0);
        endDateTime.setHourOfDay(23);

        break;
      case PREVIOUS_WEEK:
        startDateTime.addWeeks(-1);
        endDateTime.addWeeks(-1);

        startDateTime.setDayOfWeek(1);
        startDateTime.setHourOfDay(0);

        endDateTime.setDayOfWeek(7);
        endDateTime.setHourOfDay(23);

        break;
      case CURRENT_WEEK:
        startDateTime.setDayOfWeek(1);
        endDateTime.setDayOfWeek(7);

        startDateTime.setHourOfDay(0);
        endDateTime.setHourOfDay(23);

        break;
      case NEXT_WEEK:
        startDateTime.addWeeks(1);
        startDateTime.setDayOfWeek(1);
        startDateTime.setHourOfDay(0);

        endDateTime.addWeeks(1);
        endDateTime.setDayOfWeek(7);
        endDateTime.setHourOfDay(23);
        break;
      case PREVIOUS_MONTH:
        DateMidnight firstDayOfLastMonth = new DateMidnight(referenceDate).minusMonths(1).withDayOfMonth(1);
        startDateTime = firstDayOfLastMonth.toMutableDateTime();

        DateMidnight lastDayOfLastMonth = firstDayOfLastMonth.plusMonths(1).minusDays(1);
        endDateTime = lastDayOfLastMonth.toMutableDateTime();
        endDateTime.setHourOfDay(23);
        break;
      case CURRENT_MONTH:
        DateMidnight firstDayOfCurrentMonth = new DateMidnight(referenceDate).withDayOfMonth(1);
        startDateTime = firstDayOfCurrentMonth.toMutableDateTime();

        DateMidnight lastDayOfCurrentMonth = firstDayOfCurrentMonth.plusMonths(1).minusDays(1);
        endDateTime = lastDayOfCurrentMonth.toMutableDateTime();
        endDateTime.setHourOfDay(23);
        break;
      case NEXT_MONTH:
        DateMidnight firstDayOfNextMonth = new DateMidnight(referenceDate).plusMonths(1).withDayOfMonth(1);
        startDateTime = firstDayOfNextMonth.toMutableDateTime();

        DateMidnight lastDayOfNextMonth = firstDayOfNextMonth.plusMonths(1).minusDays(1);
        endDateTime = lastDayOfNextMonth.toMutableDateTime();
        endDateTime.setHourOfDay(23);
        break;
      case PREVIOUS_YEAR:
        DateMidnight firstDayOfLastYear = new DateMidnight(referenceDate).minusYears(1).withDayOfYear(1);
        startDateTime = firstDayOfLastYear.toMutableDateTime();

        DateMidnight lastDayOfYearMonth = firstDayOfLastYear.plusYears(1).minusDays(1);
        endDateTime = lastDayOfYearMonth.toMutableDateTime();
        endDateTime.setHourOfDay(23);
        break;
      case CURRENT_YEAR:
        DateMidnight firstDayOfCurrentYear = new DateMidnight(referenceDate).withDayOfYear(1);
        startDateTime = firstDayOfCurrentYear.toMutableDateTime();

        DateMidnight lastDayOfCurrentYear = firstDayOfCurrentYear.plusYears(1).minusDays(1);
        endDateTime = lastDayOfCurrentYear.toMutableDateTime();
        endDateTime.setHourOfDay(23);
        break;
      case NEXT_YEAR:
        DateMidnight firstDayOfNextYear = new DateMidnight(referenceDate).plusYears(1).withDayOfYear(1);
        startDateTime = firstDayOfNextYear.toMutableDateTime();

        DateMidnight lastDayOfNextYearMonth = firstDayOfNextYear.plusYears(1).minusDays(1);
        endDateTime = lastDayOfNextYearMonth.toMutableDateTime();
        endDateTime.setHourOfDay(23);
        break;
      default:
        validRangeOperator = false;
        break;
      }

      if (validRangeOperator) {
        if (resetSub) {
          startDateTime.setMinuteOfHour(0);
          startDateTime.setSecondOfMinute(0);
          startDateTime.setMillisOfSecond(0);

          endDateTime.setMinuteOfHour(59);
          endDateTime.setSecondOfMinute(59);
          endDateTime.setMillisOfSecond(999);
        }

        retObj.setStart(startDateTime.toDate());
        retObj.setEnd(endDateTime.toDate());
      }
    }
    return retObj;
  }

  /**
   * Get start and end date of a week.
   * 
   * @param year
   *          the year (optional)
   * @param week
   *          the week number
   * @return
   */
  public static List<Date> getWeekStartAndEnd(Integer year, Integer week) {
    List<Date> retObj = new ArrayList<Date>();

    if (week == null) {
      throw new IllegalArgumentException("Invalid week number!");
    }

    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(Calendar.WEEK_OF_YEAR, week);
    if (year != null) {
      calendar.set(Calendar.YEAR, year);
    } else {
      Calendar now = Calendar.getInstance();
      now.setTime(new Date());
      calendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
    }

    retObj.add(calendar.getTime());
    retObj.add(addWeeks(calendar.getTime(), 1));

    return retObj;
  }

  /**
   * 
   * @param datePeriod
   * @param referenceDate
   * @param resetSub
   * @return
   */
  public static Date getDateForPeriod(DatePeriodEnum datePeriod, Date referenceDate, boolean resetSub) {
    Date retObj = null;

    if (datePeriod != null) {
      retObj = null;

      if (referenceDate == null) {
        // Default to 'NOW', if not defined
        referenceDate = new Date();
      }
      MutableDateTime dateTime = new MutableDateTime(referenceDate.getTime());

      boolean validRangeOperator = true;

      switch (datePeriod) {
      case PREVIOUS_HOUR:
        dateTime.addHours(-1);
        break;
      case CURRENT_HOUR:
        break;
      case NEXT_HOUR:
        dateTime.addHours(1);
        break;
      case PREVIOUS_DAY:
        dateTime.addDays(-1);

        if (resetSub) {
          dateTime.setHourOfDay(0);
        }
        break;
      case CURRENT_DAY:
        dateTime.setHourOfDay(0);
        break;
      case NEXT_DAY:
        dateTime.addDays(1);
        if (resetSub) {
          dateTime.setHourOfDay(0);
        }
        break;
      case PREVIOUS_WEEK:
        dateTime.addWeeks(-1);
        if (resetSub) {
          dateTime.setDayOfWeek(1);
          dateTime.setHourOfDay(0);
        }
        break;
      case CURRENT_WEEK:
        dateTime.setDayOfWeek(1);

        if (resetSub) {
          dateTime.setHourOfDay(0);
        }
        break;
      case NEXT_WEEK:
        dateTime.addWeeks(1);

        if (resetSub) {
          dateTime.setDayOfWeek(1);
          dateTime.setHourOfDay(0);
        }
        break;
      case PREVIOUS_MONTH:

        if (resetSub) {
          DateMidnight firstDayOfLastMonth = new DateMidnight(referenceDate).minusMonths(1).withDayOfMonth(1);
          dateTime = firstDayOfLastMonth.toMutableDateTime();
        } else {
          dateTime.addMonths(-1);
        }

        break;
      case CURRENT_MONTH:
        DateMidnight firstDayOfCurrentMonth = new DateMidnight(referenceDate).withDayOfMonth(1);
        dateTime = firstDayOfCurrentMonth.toMutableDateTime();
        break;
      case NEXT_MONTH:
        if (resetSub) {
          DateMidnight firstDayOfNextMonth = new DateMidnight(referenceDate).plusMonths(1).withDayOfMonth(1);
          dateTime = firstDayOfNextMonth.toMutableDateTime();
        } else {
          dateTime.addMonths(1);
        }
        break;
      case PREVIOUS_YEAR:
        if (resetSub) {
          DateMidnight firstDayOfLastYear = new DateMidnight(referenceDate).minusYears(1).withDayOfYear(1);
          dateTime = firstDayOfLastYear.toMutableDateTime();
        } else {
          dateTime.addYears(-1);
        }
        break;
      case CURRENT_YEAR:
        DateMidnight firstDayOfCurrentYear = new DateMidnight(referenceDate).withDayOfYear(1);
        dateTime = firstDayOfCurrentYear.toMutableDateTime();
        break;
      case NEXT_YEAR:
        if (resetSub) {
          DateMidnight firstDayOfNextYear = new DateMidnight(referenceDate).plusYears(1).withDayOfYear(1);
          dateTime = firstDayOfNextYear.toMutableDateTime();
        } else {
          dateTime.addYears(1);
        }
        break;
      default:
        validRangeOperator = false;
        break;
      }

      if (validRangeOperator) {
        if (resetSub) {
          dateTime.setMinuteOfHour(0);
          dateTime.setSecondOfMinute(0);
          dateTime.setMillisOfSecond(0);
        }
        retObj = dateTime.toDate();
      }
    }
    return retObj;
  }

  public static Date reset(Date date, DateTimeFieldType... fields) {
    Date retObj = null;
    if (date != null && fields != null && fields.length > 0) {
      MutableDateTime mdt = new MutableDateTime(date.getTime());
      int resetVal = 0;

      for (DateTimeFieldType type : fields) {
        if (type.equals(DateTimeFieldType.dayOfMonth()) || type.equals(DateTimeFieldType.dayOfYear())
            || type.equals(DateTimeFieldType.monthOfYear())) {
          resetVal = 1;
        } else {
          resetVal = 0;
        }

        mdt.set(type, resetVal);
      }
      retObj = mdt.toDate();
    }
    return retObj;
  }

  /**
   * @param day
   * @param startHour
   * @return
   */
  public static boolean isValidDateTime(Date date, Date time) {
    MutableDateTime mdt = new MutableDateTime(date.getTime());
    boolean retObj = false;

    int hour = DateUtils.getDateField(time, DateTimeFieldType.hourOfDay());

    try {
      mdt.setHourOfDay(hour);
      retObj = true;
    } catch (Exception e) {
      // ...
    }

    return retObj;
  }

  /**
   * Set the hour, minutes and seconds of the given date.
   * 
   * @param date
   *          the date to update
   * @param hour
   *          the hour
   * @param minutes
   *          minutes
   * @param seconds
   *          seconds
   * @return the updated date
   */
  public static Date setTime(Date date, int hour, int minutes, int seconds) {
    Date retObj = null;

    if (date != null) {
      MutableDateTime mdt = new MutableDateTime(date.getTime());

      try {
        mdt.setHourOfDay(hour);
      } catch (Exception e) {
        mdt.setHourOfDay(hour + 1); // Invalid hour (daylight saving ->
        // shift to next hour
      }
      mdt.setMinuteOfHour(minutes);
      mdt.setSecondOfMinute(seconds);
      mdt.setMillisOfSecond(0);
      retObj = mdt.toDate();
    }
    return retObj;
  }

  /**
   * Set the hour, minutes and seconds of the given date.
   * 
   * @param date
   *          the date to update
   * @param referenceTime
   *          the reference time, defining the hour/minute/second value to be set.
   * @return the updated date. The 'date' portion remains the same has original date, time component is set to reference date time.
   */
  public static Date setTime(Date date, Date referenceTime) {
    Date retObj = null;
    if (date != null) {
      int hour = DateUtils.getDateField(referenceTime, DateTimeFieldType.hourOfDay());
      int minutes = DateUtils.getDateField(referenceTime, DateTimeFieldType.minuteOfHour());
      int seconds = DateUtils.getDateField(referenceTime, DateTimeFieldType.secondOfMinute());

      retObj = setTime(date, hour, minutes, seconds);
    }
    return retObj;
  }

  /**
   * Resets provided date hour. Hour, minutes, seconds and milliseconds are set to 0.
   * 
   * @param date
   * @param fields
   * @return
   */
  public static Date resetTime(Date date) {
    return reset(date, DateTimeFieldType.hourOfDay(), DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute(),
        DateTimeFieldType.millisOfSecond());
  }

  public static Date resetDate(Date date) {
    Date retObj = new Date(0);
    retObj = setTime(retObj, date);

    return retObj;
  }

  public static int getDateField(Date date, DateTimeFieldType field) {
    int retObj = -1;
    MutableDateTime mdt = new MutableDateTime(date.getTime());
    retObj = mdt.get(field);
    return retObj;
  }

  /**
   * Get week of month for provided date.
   * 
   * @param date
   * @return
   */
  public static int getWeekOfMonth(Date date) {
    int retObj = -1;

    if (date != null) {
      int month = DateUtils.getDateField(date, DateTimeFieldType.monthOfYear());
      int monthDay = DateUtils.getDateField(date, DateTimeFieldType.dayOfMonth());

      int firstWeekInMonth = new LocalDate().withMonthOfYear(month).withDayOfMonth(1).getWeekOfWeekyear();
      int weekInMonth = new LocalDate().withMonthOfYear(month).withDayOfMonth(monthDay).getWeekOfWeekyear();

      retObj = weekInMonth - firstWeekInMonth + 1;
    }

    return retObj;
  }

  /**
   * Get date interval for start/end of week for given date. Uses ISO standard: week starts in MONDAY, ends SUNDAY.
   * 
   * @param startDate
   * @return
   */
  public static DateInterval getWeek(Date date) {
    DateInterval retObj = DateUtils.getDateRange(DatePeriodEnum.CURRENT_WEEK, date);
    return retObj;
  }

  /**
   * Get date interval representing the day for given date. This method can be helpful when comparing if two dates are in the same
   * day.
   * 
   * @param date
   *          : reference date, for which we want the day.
   * @return
   */
  public static DateInterval getDay(Date date) {
    DateInterval retObj = DateUtils.getDateRange(DatePeriodEnum.CURRENT_DAY, date);
    return retObj;
  }

  /**
   * Check if two dates belong to same day.
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static boolean isSameDay(Date date1, Date date2) {
    boolean retObj = false;

    if (date1 != null && date2 != null) {
      SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
      retObj = fmt.format(date1).equals(fmt.format(date2));
    }

    return retObj;
  }

  /**
   * Check if two dates have the same hour.
   * 
   * @param date1
   * @param date2
   * @return
   */
  public static boolean isSameHour(Date date1, Date date2) {
    boolean retObj = false;

    if (date1 != null && date2 != null) {
      SimpleDateFormat fmt = new SimpleDateFormat("HHmmss,SSS");
      retObj = fmt.format(date1).equals(fmt.format(date2));
    }

    return retObj;
  }

  /**
   * Check if a date if older than 'X time units' compared to current instant.
   * 
   * @param date
   *          The date to be compared.
   * @param type
   *          The time unit type (e.g. days, hours, minutes...)
   * @param amount
   *          The amount of time units.
   * @return <code>true</code> if first date is 'X time units' newer than reference date, false otherwise.
   */
  public static boolean isOlderThan(Date date, TimeUnitEnum timeUnit, int amount) {
    return isOlderThan(date, new Date(), timeUnit, amount);
  }

  /**
   * Check if a date if older than 'X time units' compared to reference date.
   * 
   * @param date
   *          The date to be compared.
   * @param referenceDate
   *          The reference date.
   * @param type
   *          The time unit type (e.g. days, hours, minutes...)
   * @param amount
   *          The amount of time units.
   * @return <code>true</code> if first date is 'X time units' newer than reference date, false otherwise.
   */
  public static boolean isOlderThan(Date date, Date referenceDate, TimeUnitEnum timeUnit, int amount) {
    boolean retObj = false;
    DurationFieldType type = timeUnitToDurationFieldMap.get(timeUnit);
    if (type != null) {
      MutableDateTime dateTime = new MutableDateTime(date.getTime());
      dateTime.add(type, amount);
      retObj = dateTime.isBefore(referenceDate.getTime());
    }
    return retObj;
  }

  /**
   * Check if a date if newer than 'X time units' compared to current instant.
   * 
   * @param date
   *          The date to be compared.
   * @param type
   *          The time unit type (e.g. days, hours, minutes...)
   * @param amount
   *          The amount of time units.
   * @return <code>true</code> if first date is 'X time units' newer than reference date, false otherwise.
   */
  public static boolean isNewerThan(Date date, TimeUnitEnum timeUnit, int amount) {
    return isNewerThan(date, new Date(), timeUnit, amount);
  }

  /**
   * Check if a date if newer than 'X time units' compared to reference date.
   * 
   * @param date
   *          The date to be compared.
   * @param referenceDate
   *          The reference date.
   * @param type
   *          The time unit type (e.g. days, hours, minutes...)
   * @param amount
   *          The amount of time units.
   * @return <code>true</code> if first date is 'X time units' newer than reference date, false otherwise.
   */
  public static boolean isNewerThan(Date date, Date referenceDate, TimeUnitEnum timeUnit, int amount) {
    boolean retObj = false;
    DurationFieldType type = timeUnitToDurationFieldMap.get(timeUnit);
    if (type != null && date != null && referenceDate != null) {
      MutableDateTime dateTime = new MutableDateTime(date.getTime());

      dateTime.add(type, amount);
      retObj = dateTime.isAfter(referenceDate.getTime());
    }
    return retObj;
  }

  public static synchronized List<DatePeriodEnum> getDatePeriod(Date date) {
    List<DatePeriodEnum> retObj = new ArrayList<DatePeriodEnum>();

    for (DatePeriodEnum datePeriod : DatePeriodEnum.values()) {
      if (isDateInRange(date, datePeriod)) {
        retObj.add(datePeriod);
      }
    }
    return retObj;
  }

  /**
   * Check if provided date is in desired date period (e.g. current week/month/year).
   * 
   * @param date
   *          The date to be analyzed.
   * @param rangeOperator
   *          The date period against which date should be matched.
   * @return
   */
  public static boolean isDateInRange(Date date, DatePeriodEnum rangeOperator) {
    DateInterval rangeDate = getDateRange(rangeOperator);
    boolean retObj = false;

    if (rangeDate != null) {
      retObj = isDateInRange(date, rangeDate.getStart(), rangeDate.getEnd());
    }
    return retObj;
  }

  public static boolean isDateInRange(Date date, Date startDate, Date endDate) {
    return isDateInRange(date, startDate, endDate, true);
  }

  public static boolean isDateInRange(Date date, Date startDate, Date endDate, boolean includeBoundaries) {
    boolean retObj = false;
    if (date != null && startDate != null && endDate != null) {
      retObj = ((date.after(startDate) || (includeBoundaries && date.equals(startDate)))
          && (date.before(endDate) || (includeBoundaries && date.equals(endDate))));
    }
    return retObj;
  }

  public static boolean checkDateOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2,
      boolean includeBoundaries) {
    // If periods start/end date coincide, dates overlap, regardless of
    // boundary inclusion.
    if (isSamePeriod(startDate1, endDate1, startDate2, endDate2)) {
      return true;
    } else {
      // [includeBoundaries] (StartA <= EndB) and (EndA >= StartB) /
      // [!includeBoundaries] (StartA < EndB) and (EndA > StartB)
      return isDateInRange(startDate2, startDate1, endDate1, includeBoundaries)
          || isDateInRange(endDate2, startDate1, endDate1, includeBoundaries);
    }
  }

  public static boolean checkDateOverlap(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
    // (StartA <= EndB) and (EndA >= StartB)
    return checkDateOverlap(startDate1, endDate1, startDate2, endDate2, true);
  }

  public static boolean checkDateOverlap(DateInterval dateInterval1, DateInterval dateInterval2) {
    // (StartA <= EndB) and (EndA >= StartB)
    return checkDateOverlap(dateInterval1, dateInterval2, true);
  }

  public static boolean checkDateOverlap(DateInterval dateInterval1, DateInterval dateInterval2, boolean includeBoundaries) {
    return checkDateOverlap(dateInterval1.getStart(), dateInterval1.getEnd(), dateInterval2.getStart(), dateInterval2.getEnd(),
        includeBoundaries);
  }

  public static boolean isSamePeriod(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
    return (startDate1 != null && startDate2 != null && startDate1.getTime() == startDate2.getTime() && endDate1 != null
        && endDate2 != null && endDate1.getTime() == endDate2.getTime());
  }

  /**
   * Format a date, using provided date format.
   * 
   * @param date
   *          The date to be formatted.
   * @param string
   *          The format to apply for date.
   * @return The string representing the date, with specified format.
   */
  public static String format(Date date, String dateFormat) {
    String retObj = null;

    if (date != null && StringUtils.isNotBlank(dateFormat)) {
      try {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        retObj = dateFormatter.format(date);
      } catch (Exception e) {

      }
    }

    return retObj;
  }

  /**
   * Format a date, using default date format ('yyyy-MM-dd HH:mm:ss').
   * 
   * @param date
   *          The date to be formatted, with default date format.
   * @return The string representing the date, with default format.
   */
  public static String format(Date date) {
    return format(date, SIMPLE_DATETIME_FORMAT);
  }

  /**
   * 
   * @param dateList
   * @return
   */
  public static String format(List<Date> dateList) {
    return format(dateList, SIMPLE_DATE_FORMAT, ",");
  }

  /**
   * 
   * @param dateList
   * @param dateFormat
   * @return
   */
  public static String format(List<Date> dateList, String dateFormat) {
    return format(dateList, dateFormat, ",");
  }

  /**
   * 
   * @param dateList
   * @param dateFormat
   * @param separator
   * @return
   */
  public static String format(List<Date> dateList, String dateFormat, String separator) {
    String retObj = null;

    if (dateList != null && StringUtils.isNotBlank(dateFormat)) {
      try {
        List<String> dateStrList = new ArrayList<>();

        for (Date date : dateList) {
          if (date != null) {
            dateStrList.add(format(date, dateFormat));
          }
        }
        retObj = StringUtils.listValues(dateStrList, separator);
      } catch (Exception e) {

      }
    }

    return retObj;
  }

  /**
   * Parses a date from a string, using provided date format
   * 
   * @param date
   *          A string representation of the date.
   * @param dateFormat
   *          The date format to be used for parsing date string.
   * @return The Date object for provided date string.
   */
  public static Date parse(String date, String dateFormat) {
    Date retObj = null;

    if (date != null && StringUtils.isNotBlank(dateFormat)) {
      try {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        retObj = dateFormatter.parse(date);
      } catch (Exception e) {

      }
    }
    return retObj;
  }

  /**
   * Parses a date from a string, using default date format
   * 
   * @param date
   *          A string representation of the date.
   * @return The Date object for provided date string.
   */
  public static Date parse(String date) {
    return parse(date, SIMPLE_DATETIME_FORMAT);
  }

  /**
   * Convert a time period represented in string format to milliseconds.
   * 
   * @param period
   * @param format
   *          The date format for period.
   * @return The milliseconds corresponding to provided period.
   * @throws ParseException
   */
  public static long parseTime(String time, String format) throws ParseException {
    long retObj = 0;

    if (StringUtils.isNotBlank(time) && StringUtils.isNotBlank(format)) {
      Date timeDate = new SimpleDateFormat(format).parse(time);

      Calendar cal = Calendar.getInstance();
      cal.setTime(timeDate);

      retObj += TimeUnit.MILLISECONDS.convert(cal.get(Calendar.HOUR_OF_DAY), TimeUnit.HOURS);
      retObj += TimeUnit.MILLISECONDS.convert(cal.get(Calendar.MINUTE), TimeUnit.MINUTES);
      retObj += TimeUnit.MILLISECONDS.convert(cal.get(Calendar.SECOND), TimeUnit.SECONDS);
      retObj += TimeUnit.MILLISECONDS.convert(cal.get(Calendar.MILLISECOND), TimeUnit.MILLISECONDS);
    } else {
      retObj = -1;
    }
    return retObj;
  }

  /**
   * Convert time period in milliseconds to hh:mm:ss:SSS format.
   * 
   * @param milliseconds
   * @return
   */
  public static String periodFormat(long milliseconds) {
    return periodFormat(milliseconds, "%02d:%02d:%02d:%02d");
  }

  /**
   * Convert time period in milliseconds to hh:mm:ss:SSS format.
   * 
   * @param milliseconds
   * @return
   */
  public static String periodFormat(long milliseconds, String format) {
    String retObj = null;
    if (milliseconds >= 0) {
      int secondsIn = (int) milliseconds / 1000;
      int hours = secondsIn / 3600;
      int remainder = secondsIn % 3600;
      int minutes = remainder / 60;
      int seconds = remainder % 60;

      milliseconds = milliseconds % 1000;

      retObj = String.format(format, hours, minutes, seconds, milliseconds);

    }
    return retObj;
  }

  /**
   * Get the date of the next weekday (the weekdays starts at SUNDAY ) starting in provided date.
   * 
   * @param currentDate
   * @param weekday
   * @return
   */
  public static Date getNextWeekDay(Date currentDate, int weekday) {
    Calendar retObj = Calendar.getInstance();
    retObj.setTime(currentDate);

    int currentWeekDay = retObj.get(Calendar.DAY_OF_WEEK);
    int days = weekday - currentWeekDay;
    if (days < 0) {
      days += 7;
    }
    retObj.add(Calendar.DAY_OF_YEAR, days);

    return retObj.getTime();
  }

  public static List<Integer> getOrderWeekDaysStartinginDate(Date fromDate, List<Integer> weekDays) {
    List<Integer> retObj = new ArrayList<Integer>();

    Calendar date = Calendar.getInstance();
    date.setTime(fromDate);
    int currentDay = date.get(Calendar.DAY_OF_WEEK);

    List<Integer> nextWeekDays = new ArrayList<Integer>();

    for (int i = 0; i < weekDays.size(); i++) {
      int weekDay = weekDays.get(i);
      if (weekDay >= currentDay) {
        retObj.add(weekDay);
      } else {
        nextWeekDays.add(weekDay);
      }
    }
    retObj.addAll(nextWeekDays);

    return retObj;
  }

  /**
   * 
   * @param timeInMillis
   * @param fps
   * @return
   */
  public static Integer convertTimeToFrames(Integer timeInMillis, int fps) {
    Integer retObj = null;

    if (timeInMillis != null && fps > 0) {
      int frameDuration = (int) ((double) 1.0 / fps * 1000);
      retObj = (int) (timeInMillis / frameDuration);

      // Add remaining frames
      retObj += (int) (timeInMillis % frameDuration);
    }
    return retObj;
  }

  /**
   * Convert frames to desired time format.
   * 
   * @param timeInFrames
   *          The time represented in frames.
   * @param fps
   *          The frames-per-second, needed to convert frames to milliseconds.
   * @param timeFormat
   *          The format for output time to be displayed.
   * @return
   */
  public static String convertFramesToTime(Integer timeInFrames, int fps, String timeFormat) {
    String retObj = null;
    String remainingFrames = "00";

    if (timeInFrames != null && fps > 0) {
      long time = (long) timeInFrames / fps * 1000;
      String timePortion = periodFormat(time, timeFormat);
      remainingFrames = String.format("%02d", timeInFrames % fps);
      retObj = timePortion + remainingFrames;
    }

    return retObj;
  }

  /**
   * Convert frames to default time format.
   * 
   * @param timeInFrames
   *          The time represented in frames.
   * @param fps
   *          The frames-per-second, needed to convert frames to milliseconds.
   * @return
   */
  public static String convertFramesToTime(Integer timeInFrames, int fps) {
    return convertFramesToTime(timeInFrames, fps, "%02d%02d%02d");
  }

  public static List<Date> planDates(Date startDate, int total, PeriodicityEnum periodicity) {
    List<Date> retObj = new ArrayList<Date>();

    if (startDate != null && total > 0 && periodicity != null) {

      DatePeriodEnum periodEnum = null;

      switch (periodicity) {
      case HOURLY:
        periodEnum = DatePeriodEnum.NEXT_HOUR;
        break;
      case WORKDAYS:
      case DAILY:
        periodEnum = DatePeriodEnum.NEXT_DAY;
        break;
      case TWO_WEEKS:
      case WEEKLY:
        periodEnum = DatePeriodEnum.NEXT_WEEK;
        break;
      case MONTHLY:
        periodEnum = DatePeriodEnum.NEXT_MONTH;
        break;
      default:
        break;
      }

      Date nextDateTime = startDate;
      retObj.add(startDate);
      int fortnightCounter = 0;

      for (int i = 1; i < total;) {
        nextDateTime = getDateForPeriod(periodEnum, nextDateTime, false);

        if (periodicity.equals(PeriodicityEnum.WORKDAYS)) {
          if (isWorkingDay(nextDateTime)) {
            i++;
            retObj.add(nextDateTime);
          }
        } else if (periodicity.equals(PeriodicityEnum.TWO_WEEKS)) {
          if ((fortnightCounter % 2) != 0) {
            i++;
            retObj.add(nextDateTime);
          }
          fortnightCounter++;
        } else {
          i++;
          retObj.add(nextDateTime);
        }
      }
    }
    return retObj;
  }

  /**
   * Check if date corresponds to a working day.
   * 
   * @param date
   * @return
   */
  public static boolean isWorkingDay(Date date) {
    MutableDateTime dateTime = new MutableDateTime(date.getTime());
    return (dateTime.getDayOfWeek() != DateTimeConstants.SATURDAY && dateTime.getDayOfWeek() != DateTimeConstants.SUNDAY);
  }

  public static List<Date> planDates(Date startDate, Date endDate, PeriodicityEnum periodicity) {
    List<Date> retObj = new ArrayList<Date>();

    if (startDate != null && endDate != null && periodicity != null) {
      if (startDate.before(endDate)) {
        DatePeriodEnum periodEnum = null;

        switch (periodicity) {
        case HOURLY:
          periodEnum = DatePeriodEnum.NEXT_HOUR;
          break;
        case WORKDAYS:
        case DAILY:
          periodEnum = DatePeriodEnum.NEXT_DAY;
          break;
        case TWO_WEEKS:
        case WEEKLY:
          periodEnum = DatePeriodEnum.NEXT_WEEK;
          break;
        case MONTHLY:
          periodEnum = DatePeriodEnum.NEXT_MONTH;
          break;
        default:
          break;
        }

        Date nextDateTime = startDate;

        int fortnightCounter = 0;
        retObj.add(nextDateTime);

        while (true) {
          nextDateTime = getDateForPeriod(periodEnum, nextDateTime, false);

          if (nextDateTime.before(endDate)) {
            if (periodicity.equals(PeriodicityEnum.WORKDAYS)) {
              if (isWorkingDay(nextDateTime)) {
                retObj.add(nextDateTime);
              }
            } else if (periodicity.equals(PeriodicityEnum.TWO_WEEKS)) {
              if ((fortnightCounter % 2) != 0) {
                retObj.add(nextDateTime);
              }
              fortnightCounter++;
            } else {
              retObj.add(nextDateTime);
            }
          } else {
            break;
          }
        }

      }
    }
    return retObj;
  }

  /**
   * Set the date field to desired value.
   * 
   * @param date
   *          The reference date to be set.
   * @param field
   *          The field to be set in date.
   * @param value
   *          The value of the field.
   * @return The new date, with field properly set.
   */
  public static Date setDateField(Date date, DateTimeFieldType field, int value) {
    Date retObj = null;
    if (date != null && field != null) {
      MutableDateTime mdt = new MutableDateTime(date.getTime());
      mdt.set(field, value);
      retObj = mdt.toDate();
    }
    return retObj;
  }

  /**
   * Add a X amount of weeks to reference date.
   * 
   * @param referenceDate
   *          Original date.
   * @param amount
   *          Amount of weeks to be added. Negative numbers are allowed (subtracts weeks to date).
   * @return The new date, with desired amount of weeks added.
   */
  public static Date addWeeks(Date referenceDate, int amount) {
    Date retObj = null;
    if (referenceDate != null) {
      MutableDateTime refDate = new MutableDateTime(referenceDate.getTime());
      refDate.addWeeks(amount);
      retObj = refDate.toDate();
    }
    return retObj;
  }

  /**
   * Add a X amount of days to reference date.
   * 
   * @param referenceDate
   * @param amount
   * @return
   */
  public static Date addDays(Date referenceDate, int amount) {
    Date retObj = null;
    if (referenceDate != null) {
      MutableDateTime refDate = new MutableDateTime(referenceDate.getTime());
      refDate.addDays(amount);
      retObj = refDate.toDate();
    }
    return retObj;
  }

  /**
   * Add a X amount of hours to reference date.
   * 
   * @param referenceDate
   *          Original date.
   * @param amount
   *          Amount of hours to be added. Negative numbers are allowed (subtracts hours to date).
   * @return
   */
  public static Date addHours(Date referenceDate, int amount) {
    Date retObj = null;
    if (referenceDate != null) {
      MutableDateTime refDate = new MutableDateTime(referenceDate.getTime());
      refDate.addHours(amount);
      retObj = refDate.toDate();
    }
    return retObj;
  }

  /**
   * Add a X amount of minutes to reference date.
   * 
   * @param referenceDate
   *          Original date.
   * @param amount
   *          Amount of minutes to be added. Negative numbers are allowed (subtracts minutes to date).
   * @return
   */
  public static Date addMinutes(Date referenceDate, int amount) {
    Date retObj = null;
    if (referenceDate != null) {
      MutableDateTime refDate = new MutableDateTime(referenceDate.getTime());
      refDate.addMinutes(amount);
      retObj = refDate.toDate();
    }
    return retObj;
  }

  /**
   * Add a X amount of seconds to reference date.
   * 
   * @param referenceDate
   *          Original date.
   * @param amount
   *          Amount of seconds to be added. Negative numbers are allowed (subtracts seconds to date).
   * @return
   */
  public static Date addSeconds(Date referenceDate, int amount) {
    Date retObj = null;
    if (referenceDate != null) {
      MutableDateTime refDate = new MutableDateTime(referenceDate.getTime());
      refDate.addSeconds(amount);
      retObj = refDate.toDate();
    }
    return retObj;
  }

  /**
   * Compare two dates against only the time field (ignores Year/Month/Day)
   * 
   * @param date1
   *          The first date in comparison
   * @param date2
   *          The second date in comparison
   * @return
   */
  public static int timeCompare(Date date1, Date date2) {
    DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
    return comparator.compare(date1, date2);
  }

  /**
   * Compare two dates (as Long) against only the time field (ignores Year/Month/Day)
   * 
   * @param date1
   *          The first date in comparison
   * @param date2
   *          The second date in comparison
   * @return
   */
  public static int timeCompare(Long date1, Long date2) {
    DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
    return comparator.compare(date1, date2);
  }

  /**
   * 
   * @param weekOfMonth
   *          The n-th week of month (e.g. 2nd week of month). WARNING: designated month might not have this week, since different
   *          months have different number of weeks.
   * @param weekDay
   *          The day in week (using {@link DateTimeConstants} values - SUNDAY is 1st of week)
   * @param month
   *          The month to get date from
   * @param year
   *          The year to get date from
   * @return
   */
  public static Date getNthOfMonth(int weekOfMonth, int weekDay, int month, int year) {
    Date retObj = null;
    if (weekOfMonth == -1) {
      retObj = getNthOfMonth(0, weekDay, month + 1, year);
    }
    LocalDate compareDate = new LocalDate(year, month, 1);
    if (compareDate.getDayOfWeek() > weekDay) {
      compareDate = compareDate.withDayOfWeek(weekDay).plusDays(7 * weekOfMonth);
    } else {
      compareDate = compareDate.withDayOfWeek(weekDay).plusDays(7 * (weekOfMonth - 1));
    }

    if (compareDate != null) {
      retObj = compareDate.toDate();

      if (getDateField(retObj, DateTimeFieldType.monthOfYear()) != month) {
        retObj = null;
      }
    }
    return retObj;
  }

  /**
   * @param gridDay
   * @param gridDay2
   * @return
   */
  public static Integer calculateDayDifference(Date day1, Date day2) {
    Integer retObj = null;

    if (day1 != null && day2 != null) {
      DateTime refDate = new DateTime(day1.getTime());
      DateTime refDate2 = new DateTime(day2.getTime());

      retObj = Days.daysBetween(refDate.toLocalDate(), refDate2.toLocalDate()).getDays();
    }

    return retObj;
  }

  /**
   * Get the month start date for the current year and given month
   * 
   * @param month
   *          The given month
   * @return The month start date
   */
  public static Date getMonthStartDate(Integer month, Integer year) {
    GregorianCalendar gc = new GregorianCalendar(year, month - 1, 1);
    return gc.getTime();
  }

  /**
   * Get the month end date for the current year and given month
   * 
   * @param month
   *          The given month
   * @return The month end date
   */
  public static Date getMonthEndDate(Integer month, Integer year) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(getMonthStartDate(month, year));
    calendar.add(Calendar.MONTH, 1);
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    return calendar.getTime();
  }

  /**
   * Get weeks between two dates
   * 
   * @param startDate
   *          The start date
   * @param endDate
   *          The end date
   * @return The number of weeks between two dates
   */
  public static Integer getWeeksBetweenDates(Date startDate, Date endDate) {
    DateTime dateTime1 = new DateTime(startDate);
    DateTime dateTime2 = new DateTime(endDate);

    return Weeks.weeksBetween(dateTime1, dateTime2).getWeeks();
  }

  /**
   * Get closest future date, from reference date.
   * 
   * @param referenceDate
   * @param dateList
   * @return
   */
  public static Date getClosestDateInFuture(Date referenceDate, List<Date> dateList) {
    Date retObj = null;

    if (CollectionUtils.isNotEmpty(dateList)) {
      Collections.sort(dateList);
      Date currentDate = referenceDate;

      for (Date date : dateList) {
        if (date != null) {
          if (retObj == null) {
            retObj = date;
          } else {
            if (date.before(currentDate) && date.after(retObj)) {
              retObj = date;
            } else if (date.after(currentDate) && retObj.before(currentDate)) {
              retObj = date;
            }
          }
        }
      }
    }

    return retObj;
  }

  /**
   * Retrieve closest date in the future, referencing NOW (current time). If all dates are in the past, closest date to NOW is
   * retrieved. If several dates are in the future, the closest to NOW is retrieved.
   * 
   * @param dateList
   * @return
   */
  public static Date getClosestDateInFuture(List<Date> dateList) {
    return getClosestDateInFuture(new Date(), dateList);
  }

  /**
   * 
   * @param xmlCalendar
   * @return
   */
  public static Date toDate(XMLGregorianCalendar xmlCalendar) {
    Date retObj = null;

    if (xmlCalendar != null) {
      xmlCalendar.toGregorianCalendar().getTime();
    }

    return retObj;
  }

  public static Date getCurrentDay() {
    return resetTime(new Date());
  }

  public static DayEnum getWeekDay(Date date) {
    DayEnum retObj = null;

    if (date != null) {
      DateTime dateTime = new DateTime(date);
      retObj = DayEnum.getFromOrder(dateTime.getDayOfWeek());
    }

    return retObj;
  }

  /**
   * Get start of day, from provided date. E.g. if '2015-01-01 12:34:56' provided, '2015-01-01 00:00:00' is returned.
   * 
   * @param date
   * @return
   */
  public static Date getStartOfDay(Date date) {
    return resetTime(date);
  }

  /**
   * Get end of day, from provided date. E.g. if '2015-01-01 12:34:56' provided, '2015-01-01 23:59:00' is returned.
   * 
   * @param date
   * @return
   */
  public static Date getEndOfDay(Date date) {
    return addSeconds(addDay(resetTime(date)), -1);
  }

}
