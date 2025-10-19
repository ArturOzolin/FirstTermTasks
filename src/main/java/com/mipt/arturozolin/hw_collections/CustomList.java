package com.mipt.arturozolin.hw_collections;

import java.util.Iterator;

/**
 * Интерфейс списка с доступом по индексу.
 *
 * @param <A> тип элементов (null запрещён)
 */
public interface CustomList<A> extends Iterable<A> {
  /**
   * Добавляет элемент в конец.
   *
   * @param value элемент (не null)
   * @return true
   * @throws NullPointerException если value == null
   */
  boolean add(A value);

  /**
   * Возвращает элемент по индексу.
   *
   * @param index 0..size-1
   * @return элемент
   * @throws IndexOutOfBoundsException если индекс вне диапазона
   */
  A get(int index);

  /**
   * Удаляет элемент по индексу со сдвигом влево.
   *
   * @param index 0..size-1
   * @return удалённый элемент
   * @throws IndexOutOfBoundsException если индекс вне диапазона
   */
  A remove(int index);

  /**
   * Текущее число элементов.
   *
   * @return размер
   */
  int size();

  /**
   * Признак пустоты.
   *
   * @return true, если список пуст
   */
  boolean isEmpty();

  /**
   * Итератор по элементам в порядке индексов.
   *
   * @return итератор
   */
  @Override
  Iterator<A> iterator();
}
