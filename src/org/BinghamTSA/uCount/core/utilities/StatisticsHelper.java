package org.BinghamTSA.uCount.core.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This convenience class is to help in generating and analyzing statistics about the Polls. It is a
 * work in progress, as statistics are not yet fully implemented into uCount.
 */
public class StatisticsHelper {

  public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

  /**
   * Sorts the given Map by descending order.
   * 
   * @param map The Map to sort.
   * @return A sorted Map, in descending order.
   */
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByDescendingValue(
      Map<K, V> map) {
    List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      @Override
      public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : list) {
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }

  /**
   * Creates a sum of all values in the given Map.
   * 
   * @param map The Map to sum.
   * @return The sum of the values in the Map.
   */
  public static <K> int sumValues(Map<K, Integer> map) {
    int total = 0;
    for (Map.Entry<K, Integer> entry : map.entrySet()) {
      total += entry.getValue().intValue();
    }
    return total;
  }

}
