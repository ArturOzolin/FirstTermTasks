package com.mipt.arturozolin.hw_collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

class CustomArrayListTest {

  @Test
  void add_and_size_and_get() {
    CustomList<Integer> list = new CustomArrayList<>();
    for (int i = 0; i < 100; i++) list.add(i);
    Assertions.assertEquals(100, list.size());
    Assertions.assertEquals(0, list.get(0));
    Assertions.assertEquals(99, list.get(99));
  }

  @Test
  void add_null_forbidden() {
    CustomList<String> list = new CustomArrayList<>();
    Assertions.assertThrows(NullPointerException.class, () -> list.add(null));
  }

  @Test
  void get_bounds() {
    CustomList<Integer> list = new CustomArrayList<>();
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    list.add(1);
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
  }

  @Test
  void remove_shifts_left() {
    CustomList<String> list = new CustomArrayList<>();
    list.add("a"); list.add("b"); list.add("c"); list.add("d");
    String removed = list.remove(1);
    Assertions.assertEquals("b", removed);
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals("a", list.get(0));
    Assertions.assertEquals("c", list.get(1));
    Assertions.assertEquals("d", list.get(2));
  }

  @Test
  void isEmpty_flag() {
    CustomList<Integer> list = new CustomArrayList<>();
    Assertions.assertTrue(list.isEmpty());
    list.add(7);
    Assertions.assertFalse(list.isEmpty());
  }

  @Test
  void iterator_order() {
    CustomList<Integer> list = new CustomArrayList<>();
    list.add(10); list.add(20); list.add(30);
    Iterator<Integer> it = list.iterator();
    int s = 0;
    while (it.hasNext()) s += it.next();
    Assertions.assertEquals(60, s);
  }
}
