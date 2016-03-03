package org.BinghamTSA.uCount.core.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatisticsHelper {
  
  public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
  
  public static <K> int sumValues(Map<K, Integer> map) {
    int total = 0;
    for (Map.Entry<K, Integer> entry : map.entrySet()) {
      total += entry.getValue().intValue();
    }
    return total;
  }

}
