package com.mipt.arturozolin.hw_generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CollectionUtils {
  public static <T> List<T> mergeLists(List<? extends T> list1,
                                       List<? extends T> list2) {
    int cap = (list1 == null ? 0 : list1.size()) + (list2 == null ? 0 : list2.size());
    List<T> merged = new ArrayList<>(cap);
    if (list1 != null) merged.addAll(list1);
    if (list2 != null) merged.addAll(list2);
    return merged;
  }

  public static <T> void addAll(List<? super T> destination,
                                List<? extends T> source) {
    if (source == null || source.isEmpty()) return;
    destination.addAll(source);
  }

  public static void main(String[] args) {
    final List<Integer> list1 = Arrays.asList(1, 2, 3);
    final List<Double> list2 = Arrays.asList(4.5, 5.6);
    final List<Number> merged = CollectionUtils.mergeLists(list1, list2);

    final List<Object> destination = new ArrayList<>();
    CollectionUtils.addAll(destination, list1);
    System.out.println(merged);
  }

}
