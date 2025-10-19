package com.mipt.arturozolin.hw_collections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Параметризованная реализция
 * Рост capacity: множитель 1.5.
 *
 * @param <A> тип элементов (null запрещён для аргументов)
 */
public class CustomArrayList<A> implements CustomList<A> {

  private Object[] data = new Object[0];
  private int size = 0;

  /**
   * Добавляет элемент в конец.
   *
   * @param value элемент (не null)
   * @return true
   * @throws NullPointerException если value == null
   */
  @Override
  public boolean add(A value) {
    Objects.requireNonNull(value, "null not allowed");
    ensureCapacity(size + 1);
    data[size++] = value;
    return true;
  }

  /**
   * Возвращает элемент по индексу.
   *
   * @param index 0..size-1
   * @return элемент
   * @throws IndexOutOfBoundsException если индекс вне диапазона
   */
  @Override
  @SuppressWarnings("unchecked")
  public A get(int index) {
    rangeCheck(index);
    return (A) data[index];
  }

  /**
   * Удаляет элемент по индексу со сдвигом влево.
   *
   * @param index 0..size-1
   * @return удалённый элемент
   * @throws IndexOutOfBoundsException если индекс вне диапазона
   */
  @Override
  @SuppressWarnings("unchecked")
  public A remove(int index) {
    rangeCheck(index);
    A old = (A) data[index];
    int moved = size - index - 1;
    if (moved > 0) {
      System.arraycopy(data, index + 1, data, index, moved);
    }
    data[--size] = null;
    return old;
  }

  /**
   * Текущее число элементов.
   *
   * @return размер
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Признак пустоты.
   *
   * @return true, если список пуст
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Итератор по элементам в порядке индексов.
   *
   * @return итератор
   */
  @Override
  public Iterator<A> iterator() {
    return new Itr();
  }

  private void rangeCheck(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
    }
  }

  private void ensureCapacity(int minCapacity) {
    if (minCapacity <= data.length) return;
    int oldCap = Math.max(1, data.length);
    int grown = (int) Math.ceil(oldCap * 1.5);
    int newCap = Math.max(minCapacity, grown);
    Object[] nd = new Object[newCap];
    System.arraycopy(data, 0, nd, 0, size);
    data = nd;
  }

  private final class Itr implements Iterator<A> {
    private int cursor = 0;

    @Override
    public boolean hasNext() {
      return cursor < size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public A next() {
      if (cursor >= size) throw new NoSuchElementException();
      return (A) data[cursor++];
    }
  }
}
