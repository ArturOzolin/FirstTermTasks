package com.mipt.arturozolin.hw_collections;

import java.util.Objects;

public final class Student {
  private final int id;
  private final String name;
  private final double grade;

  public Student(int id, String name, double grade) {
    this.id = id;
    this.name = Objects.requireNonNull(name);
    this.grade = grade;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getGrade() {
    return grade;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Student)) return false;
    Student s = (Student) o;
    return id == s.id;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(id);
  }
}
