package pt.rxc.commmon.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author ruben.correia
 *
 */
public class MapUtils {

  /**
   * Get key that corresponds to provided value. If value is present in more than one entry, only FIRST key is returned.
   * 
   * @param map
   * @param value
   * @return
   */
  public static <K, V> K getKey(Map<K, List<V>> map, V value) {
    K retObj = null;

    if (map != null) {
      for (K key : map.keySet()) {
        List<V> list = map.get(key);

        if (CollectionUtils.isNotEmpty(list)) {
          if (list.contains(value)) {
            retObj = key;
          }
        }
      }
    }

    return retObj;
  }

  /**
   * Add a value to a given list contained in map. Value is added to list referenced by provided key; if no list exists matching key
   * in map, a new list is created.
   * 
   * @param map
   * @param key
   *          THe key for which
   * @param value
   */
  public static <K, V> void addValue(Map<K, List<V>> map, K key, V value) {
    if (map != null) {
      if (!map.containsKey(key)) {
        map.put(key, new ArrayList<V>());
      }
      map.get(key).add(value);
    }
  }

  /**
   * Get first value of keyed list in map.
   * 
   * @param key
   * @param listMap
   * @return
   */
  public static <K, V> V getValue(K key, Map<K, List<V>> listMap) {
    V retObj = null;

    if (listMap != null) {
      List<V> list = listMap.get(key);
      if (CollectionUtils.isNotEmpty(list)) {
        retObj = list.get(0);
      }
    }

    return retObj;
  }
}
