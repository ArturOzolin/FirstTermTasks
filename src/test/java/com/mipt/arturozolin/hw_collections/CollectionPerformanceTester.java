package com.mipt.arturozolin.hw_collections;



import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionPerformanceTester {

  private static final int N = 10_000;

  private static long ms(Runnable r) {
    long t0 = System.nanoTime();
    r.run();
    return (System.nanoTime() - t0) / 1_000_000;
  }

  private static long addEnd(List<Integer> list) {
    list.clear();
    return ms(() -> {
      for (int i = 0; i < N; i++) list.add(i);
    });
  }

  private static long addStart(List<Integer> list) {
    list.clear();
    return ms(() -> {
      for (int i = 0; i < N; i++) list.add(0, i);
    });
  }

  private static long insertMiddle(List<Integer> list) {
    list.clear();
    return ms(() -> {
      for (int i = 0; i < N; i++) list.add(list.size() / 2, i);
    });
  }

  private static long getByIndex(List<Integer> list) {
    list.clear();
    for (int i = 0; i < N; i++) list.add(i);
    return ms(() -> {
      long s = 0;
      for (int i = 0; i < N; i++) s += list.get(i);
    });
  }

  private static long removeStart(List<Integer> list) {
    list.clear();
    for (int i = 0; i < N; i++) list.add(i);
    return ms(() -> {
      for (int i = 0; i < N; i++) list.remove(0);
    });
  }

  private static long removeEnd(List<Integer> list) {
    list.clear();
    for (int i = 0; i < N; i++) list.add(i);
    return ms(() -> {
      for (int i = 0; i < N; i++) list.remove(list.size() - 1);
    });
  }

  private static void printRow(String op, long aMs, long lMs) {
    System.out.printf("%-22s | %14d | %14d%n", op, aMs, lMs);
  }

  @Test
  void compare_ArrayList_vs_LinkedList() {
    List<Integer> arr = new ArrayList<>();
    List<Integer> lnk = new LinkedList<>();

    // лёгкий прогрев
    addEnd(new ArrayList<>());
    addEnd(new LinkedList<>());

    long a1 = addEnd(arr), l1 = addEnd(lnk);
    long a2 = addStart(arr), l2 = addStart(lnk);
    long a3 = insertMiddle(arr), l3 = insertMiddle(lnk);
    long a4 = getByIndex(arr), l4 = getByIndex(lnk);
    long a5 = removeStart(arr), l5 = removeStart(lnk);
    long a6 = removeEnd(arr), l6 = removeEnd(lnk);

    System.out.println();
    System.out.println("Операция               |  ArrayList (мс) | LinkedList (мс)");
    System.out.println("-----------------------+-----------------+-----------------");
    printRow("Добавление в конец", a1, l1);
    printRow("Добавление в начало", a2, l2);
    printRow("Вставка в середину", a3, l3);
    printRow("Доступ по индексу", a4, l4);
    printRow("Удаление из начала", a5, l5);
    printRow("Удаление из конца", a6, l6);

    assertTrue(a1 >= 0 && l1 >= 0);
  }
}

