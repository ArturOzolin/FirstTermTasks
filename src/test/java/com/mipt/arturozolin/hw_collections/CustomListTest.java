package com.mipt.arturozolin.hw_collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class CustomListTest {

  private <T> CustomList<T> newList() {
    return new CustomArrayList<>();
  }

  @Test
  void add_size_isEmpty() {
    CustomList<Integer> list = newList();
    assertTrue(list.isEmpty());
    assertEquals(0, list.size());
    assertTrue(list.add(1));
    assertTrue(list.add(2));
    assertFalse(list.isEmpty());
    assertEquals(2, list.size());
  }

  @Test
  void add_null_forbidden() {
    CustomList<String> list = newList();
    assertThrows(NullPointerException.class, () -> list.add(null));
  }

  @Test
  void get_byIndex() {
    CustomList<String> list = newList();
    list.add("a"); list.add("b"); list.add("c");
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
    assertEquals("c", list.get(2));
  }

  @Test
  void get_outOfBounds() {
    CustomList<Integer> list = newList();
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    list.add(7);
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
  }

  @Test
  void remove_byIndex_shiftsLeft() {
    CustomList<String> list = newList();
    list.add("a"); list.add("b"); list.add("c"); list.add("d");
    String removed = list.remove(1);
    assertEquals("b", removed);
    assertEquals(3, list.size());
    assertEquals("a", list.get(0));
    assertEquals("c", list.get(1));
    assertEquals("d", list.get(2));
  }

  @Test
  void remove_first_and_last() {
    CustomList<Integer> list = newList();
    list.add(10); list.add(20); list.add(30);
    assertEquals(10, list.remove(0));
    assertEquals(2, list.size());
    assertEquals(30, list.remove(1));
    assertEquals(1, list.size());
    assertEquals(20, list.get(0));
  }

  @Test
  void iterator_empty_and_order() {
    CustomList<Integer> list = newList();
    Iterator<Integer> it = list.iterator();
    assertFalse(it.hasNext());
    assertThrows(NoSuchElementException.class, it::next);

    list.add(1); list.add(2); list.add(3);
    int sum = 0;
    for (int x : list) sum += x;
    assertEquals(6, sum);
  }

  @Test
  void dynamic_growth_contract() {
    CustomList<Integer> list = newList();
    for (int i = 0; i < 100; i++) list.add(i);
    assertEquals(100, list.size());
    for (int i = 0; i < 100; i++) assertEquals(i, list.get(i));
  }
}
