package com.mipt.arturozolin.hw_generics;

public class Calculator<T extends Number> {

  private static double toDouble(Number n) {
    return (n == null) ? Double.NaN : n.doubleValue();
  }

  public double sum(T a, T b) {
    double x = toDouble(a), y = toDouble(b);
    if (Double.isNaN(x) || Double.isNaN(y)) return Double.NaN;
    return x + y;
  }

  public double subtract(T a, T b) {
    double x = toDouble(a), y = toDouble(b);
    if (Double.isNaN(x) || Double.isNaN(y)) return Double.NaN;
    return x - y;
  }

  public double multiply(T a, T b) {
    double x = toDouble(a), y = toDouble(b);
    if (Double.isNaN(x) || Double.isNaN(y)) return Double.NaN;
    return x * y;
  }

  public double divide(T a, T b) {
    double x = toDouble(a), y = toDouble(b);
    if (Double.isNaN(x) || Double.isNaN(y) || y == 0.0) return Double.NaN;
    return x / y;
  }

  public static void main(String[] args) {
    // пример использования
    final Calculator<Integer> intCalc = new Calculator<>();
    final double result = intCalc.sum(5, 3); // 8.0

    final Calculator<Double> doubleCalc = new Calculator<>();
//    final double div = doubleCalc.divide(10.0, 4.0); // 2.5
    final double div = doubleCalc.divide(10.0, 0.0); // 2.5

    System.out.println(result);
    System.out.println(div);
  }
}
