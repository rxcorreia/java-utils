package pt.rxc.common.utils;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.rxc.commmon.utils.DateUtils;
import pt.rxc.commmon.utils.DayEnum;

/**
 * Test class for {@link DateUtils}.
 * 
 * @author ruben.correia
 * 
 */
public class DateUtilsTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testParse() {
    Date date1 = DateUtils.parse("1970-01-01 01:00:00");

    Assert.assertNull(DateUtils.parse("new date"));
    Assert.assertNull(DateUtils.parse(""));
    Assert.assertNull(DateUtils.parse(null));
    Assert.assertNotNull(date1);
    Assert.assertEquals(0, date1.getTime());
  }

  @Test
  public void testGetUTCDate() {
    Date date1 = DateUtils.parse("2014-01-01 01:23:45");

    Assert.assertNotNull(DateUtils.getUTCDate(date1, "GMT"));
    Assert.assertNull(DateUtils.getUTCDate(null, ""));
    Assert.assertNull(DateUtils.getUTCDate(null, null));
  }

  @Test
  public void testDayOfWeek() {
    Assert.assertEquals(DayEnum.MONDAY, DateUtils.getWeekDay(DateUtils.parse("2015-10-26 00:00:05")));
    Assert.assertEquals(DayEnum.TUESDAY, DateUtils.getWeekDay(DateUtils.parse("2015-10-27 00:00:05")));
    Assert.assertEquals(DayEnum.WEDNESDAY, DateUtils.getWeekDay(DateUtils.parse("2015-10-28 00:00:05")));
    Assert.assertEquals(DayEnum.THURSDAY, DateUtils.getWeekDay(DateUtils.parse("2015-10-29 00:00:05")));
    Assert.assertEquals(DayEnum.FRIDAY, DateUtils.getWeekDay(DateUtils.parse("2015-10-30 00:00:05")));
    Assert.assertEquals(DayEnum.SATURDAY, DateUtils.getWeekDay(DateUtils.parse("2015-10-31 00:00:05")));
    Assert.assertEquals(DayEnum.SUNDAY, DateUtils.getWeekDay(DateUtils.parse("2015-11-01 00:00:05")));

    Assert.assertEquals(null, DateUtils.getWeekDay(null));

  }

  @Test
  public void testRanges() {
    Date date1 = DateUtils.parse("2014-01-01 01:23:45");
    Date date2 = DateUtils.parse("2014-01-31 01:23:45");
    Date date3 = DateUtils.parse("2014-01-05 01:23:45");
    Date date4 = DateUtils.parse("2014-01-05 01:23:45");
    Assert.assertEquals(false, DateUtils.checkDateOverlap(null, null, null, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, null, null, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, null, null));
    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date2, date3, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(null, null, null, null, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, null, null, null, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, null, null, false));
    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date2, date3, null, false));

    Assert.assertEquals(true, DateUtils.isSamePeriod(date1, date2, date1, date2));
    Assert.assertEquals(false, DateUtils.isSamePeriod(date1, null, null, null));
    Assert.assertEquals(false, DateUtils.isSamePeriod(date1, date2, null, null));
    Assert.assertEquals(false, DateUtils.isSamePeriod(date1, date2, date3, null));

    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date2, date3, date4));
    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date2, date3, date4, false));
    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date2, date1, date2));
    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date2, date1, date2, false));
    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date1, date1, date1));

    date1 = DateUtils.parse("2014-01-01 01:23:45");
    date2 = DateUtils.parse("2014-01-31 01:23:45");
    date3 = DateUtils.parse("2014-02-01 01:23:45");
    date4 = DateUtils.parse("2014-02-15 01:23:45");
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, date4));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, date4, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(null, null, null, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, null, null, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, null, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, null, false));

    date1 = DateUtils.parse("2014-01-01 00:00:00");
    date2 = DateUtils.parse("2014-01-31 23:59:59");
    date3 = DateUtils.parse("2014-01-31 23:59:59");
    date4 = DateUtils.parse("2014-02-15 23:59:59");
    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date2, date3, date4));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, date4, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, null, null));
    Assert.assertEquals(true, DateUtils.checkDateOverlap(date1, date2, date3, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, null, null, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, null, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, null, false));

    date1 = DateUtils.parse("2014-01-01 00:00:00");
    date2 = DateUtils.parse("2014-01-31 23:59:58");
    date3 = DateUtils.parse("2014-01-31 23:59:59");
    date4 = DateUtils.parse("2014-02-15 23:59:59");
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, date4));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, date4, true));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(null, null, null, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, null, null, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, null, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, null));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(null, null, null, null, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, null, null, null, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, null, null, false));
    Assert.assertEquals(false, DateUtils.checkDateOverlap(date1, date2, date3, null, false));
  }

  @Test
  public void testDayBounds() {
    Assert.assertEquals(DateUtils.parse("2015-01-01 00:00:00"), DateUtils.getStartOfDay(DateUtils.parse("2015-01-01 12:34:56")));
    Assert.assertEquals(DateUtils.parse("2015-01-01 00:00:00"), DateUtils.getStartOfDay(DateUtils.parse("2015-01-01 00:00:00")));
    Assert.assertEquals(null, DateUtils.getStartOfDay(null));

    Assert.assertEquals(DateUtils.parse("2015-01-01 23:59:59"), DateUtils.getEndOfDay(DateUtils.parse("2015-01-01 23:59:59")));
    Assert.assertEquals(DateUtils.parse("2015-01-01 23:59:59"), DateUtils.getEndOfDay(DateUtils.parse("2015-01-01 12:34:56")));
    Assert.assertEquals(null, DateUtils.getEndOfDay(null));
  }
}
