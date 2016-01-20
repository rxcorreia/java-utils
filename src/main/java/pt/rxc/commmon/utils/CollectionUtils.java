package pt.rxc.commmon.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Utility class for java.util.Collection classes.
 * 
 * @author ruben.correia
 */
public class CollectionUtils {

  /**
   * This method checks if the list contains the given string (case sensitive).
   * 
   * @param list
   *          the list to check
   * @param string
   *          the string
   * @return true/false
   */
  public static boolean contains(List<String> list, String string) {
    if (isNotEmpty(list)) {
      for (String item : list) {
        if ((item != null && string != null && item.compareTo(string) == 0) || (item == null && string == null)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This method checks if the list contains the given string (ignoring case).
   * 
   * @param list
   *          the list to check
   * @param string
   *          the string
   * @return true/false
   */
  public static boolean containsIgnoreCase(List<String> list, String string) {
    if (isNotEmpty(list)) {
      for (String item : list) {
        if ((item != null && string != null && item.compareToIgnoreCase(string) == 0) || (item == null && string == null)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Check if set isn't empty.
   * 
   * @param set
   *          The set to be tested.
   * @return <code>true</code> is not empty, <code>false</code> otherwise.
   */
  public static <E> boolean isNotEmpty(Set<E> set) {
    return set != null && !set.isEmpty();
  }

  /**
   * Check if set is empty.
   * 
   * @param set
   * @return
   */
  public static <E> boolean isEmpty(Set<E> set) {
    return !isNotEmpty(set);
  }

  /**
   * This method checks if the list is not null and have elements.
   * 
   * @param <E>
   * 
   * @param list
   *          the list to check
   * @return true/false
   */
  public static <E> boolean isNotEmpty(List<E> list) {
    return list != null && !list.isEmpty();
  }

  public static <E> boolean isEmpty(List<E> list) {
    return !isNotEmpty(list);
  }

  /**
   * This method checks if the list is not null and have elements.
   * 
   * @param <E>
   * 
   * @param list
   *          the list to check
   * @return true/false
   */
  public static <E> boolean isNotEmpty(E[] list) {
    return list != null && list.length > 0;
  }

  public static <E> boolean isEmpty(E[] list) {
    return !isNotEmpty(list);
  }

  /**
   * Return a string representation of list of objects.
   * 
   * @param list
   * @return
   */
  public static String toString(List<?> list) {
    return StringUtils.listValues(list);
  }



  /**
   * Partition a list in sub-lists, with max size equal to 'batchSize' parameter.
   * 
   * @param list
   *          The list to be partitioned.
   * @param batchSize
   *          The batch size, representing the maximum size allowed for a sub-list.
   * @return
   */
  public static <E> List<List<E>> partition(List<E> list, Integer batchSize) {
    List<List<E>> retObj = new ArrayList<List<E>>();

    if (isNotEmpty(list)) {
      retObj = new ArrayList<List<E>>();
      List<E> currentList = null;

      for (int i = 0; i < list.size(); i++) {
        if (i % batchSize == 0) {
          currentList = new ArrayList<E>();
          retObj.add(currentList);
        }
        currentList.add(list.get(i));
      }
    }
    return retObj;
  }

  /**
   * Clean duplicate values in target list, if present in main list. This method can be used to reduce repetition of elements in a
   * list, checked against a main reference list.
   * 
   * @param targetList
   *          The target list, for which repeated elements are to be removed.
   * @param mainList
   *          The reference list.
   * @return A new list, including ONLY items exclusively present in targetList.
   */
  public static <T> List<T> cleanListDuplicates(List<T> targetList, List<T> mainList) {
    List<T> retObj = null;

    if (isNotEmpty(targetList)) {
      if (isEmpty(mainList)) {
        retObj = new ArrayList<T>(targetList);
      } else {
        retObj = new ArrayList<T>();

        for (T value : targetList) {
          if (!mainList.contains(value)) {
            retObj.add(value);
          }
        }
      }
    }
    return retObj;
  }


}
