package com.mipt.arturozolin.multithreading;

public class BankAccount {
  private final int id;
  private int balance;

  public BankAccount(int id, int initialBalance) {
    this.id = id;
    this.balance = initialBalance;
  }

  public int getId() {
    return id;
  }

  public int getBalance() {
    return balance;
  }

  public void withdraw(int amount) {
    this.balance -= amount;
  }

  public void deposit(int amount) {
    this.balance += amount;
  }

  @Override
  public String toString() {
    return "Account{id=" + id + ", balance=" + balance + '}';
  }
}
