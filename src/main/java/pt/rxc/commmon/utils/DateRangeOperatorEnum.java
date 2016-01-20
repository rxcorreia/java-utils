package pt.rxc.commmon.utils;

/**
 * Enumeration values for date range operators.
 * 
 * @author ruben.correia
 * 
 */
public enum DateRangeOperatorEnum {
  CURRENT_HOUR,
  CURRENT_DAY,
  CURRENT_WEEK,
  CURRENT_MONTH,
  CURRENT_YEAR,
  PREVIOUS_HOUR,
  PREVIOUS_X_HOURS,
  PREVIOUS_DAY,
  PREVIOUS_X_DAYS,
  PREVIOUS_WEEK,
  PREVIOUS_X_WEEKS,
  PREVIOUS_MONTH,
  PREVIOUS_X_MONTHS,
  PREVIOUS_YEAR,
  PREVIOUS_X_YEARS,
  NEXT_HOUR,
  NEXT_X_HOURS,
  NEXT_DAY,
  NEXT_X_DAYS,
  NEXT_WEEK,
  NEXT_X_WEEKS,
  NEXT_MONTH,
  NEXT_X_MONTHS,
  NEXT_YEAR,
  NEXT_X_YEARS;

  /**
   * Check if object instance matches values in provided array.
   * 
   * @param values
   * @return
   */
  public boolean matches(DateRangeOperatorEnum... values) {
    if (values != null) {
      for (DateRangeOperatorEnum value : values) {
        if (value != null && this == value) {
          return true;
        }
      }
    }
    return false;
  }
}
