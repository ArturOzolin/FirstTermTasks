package com.mipt.arturozolin.hw_collections;

import java.util.*;

public final class StudentsMaps {

  public static HashMap<Integer, Student> createHashMap(Iterable<Student> students) {
    HashMap<Integer, Student> map = new HashMap<>();
    for (Student s : students) map.put(s.getId(), s);
    return map;
  }

  public static TreeMap<Integer, Student> createTreeMapDesc(Iterable<Student> students) {
    TreeMap<Integer, Student> map = new TreeMap<>(Comparator.reverseOrder());
    for (Student s : students) map.put(s.getId(), s);
    return map;
  }

  public static List<Student> findStudentsByGradeRange(Map<Integer, Student> map, double minGrade, double maxGrade) {
    List<Student> res = new ArrayList<>();
    for (Student s : map.values()) {
      double g = s.getGrade();
      if (g >= minGrade && g <= maxGrade) res.add(s);
    }
    return res;
  }

  public static List<Student> getTopNStudents(TreeMap<Integer, Student> map, int n) {
    List<Student> res = new ArrayList<>();
    if (n <= 0) return res;
    for (Map.Entry<Integer, Student> e : map.entrySet()) {
      res.add(e.getValue());
      if (res.size() == n) break;
    }
    return res;
  }
}

