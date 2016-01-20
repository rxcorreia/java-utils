package pt.rxc.commmon.utils;

/**
 * Enumeration values for date periods.
 * 
 * @author ruben.correia
 * 
 */
public enum DatePeriodEnum {

  /**
   * NOTE: don't change order!
   */
  CURRENT_YEAR,
  NEXT_YEAR,
  PREVIOUS_YEAR,      
  CURRENT_MONTH,
  NEXT_MONTH,
  PREVIOUS_MONTH,    
  CURRENT_WEEK,
  PREVIOUS_WEEK,    
  NEXT_WEEK,
  CURRENT_DAY,
  NEXT_DAY,
  PREVIOUS_DAY,  
  CURRENT_HOUR,
  PREVIOUS_HOUR,    
  NEXT_HOUR
  ;

  /**
   * Check if object instance matches values in provided array.
   * 
   * @param values
   * @return
   */
  public boolean matches(DatePeriodEnum... values) {
    if (values != null) {
      for (DatePeriodEnum value : values) {
        if (value != null && this == value) {
          return true;
        }
      }
    }
    return false;
  }  
}
