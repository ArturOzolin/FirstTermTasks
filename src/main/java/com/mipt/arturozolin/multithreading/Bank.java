package com.mipt.arturozolin.multithreading;

public class Bank {

  public void sendToAccountDeadlock(BankAccount from, BankAccount to, int amount) {
    validateAmount(amount);

    System.out.printf("Поток %s пытается захватить %d%n", Thread.currentThread().getName(), from.getId());
    synchronized (from) {
      System.out.printf("Поток %s захватил %d%n", Thread.currentThread().getName(), from.getId());

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      System.out.printf("Поток %s пытается захватить %d%n", Thread.currentThread().getName(), to.getId());
      synchronized (to) {
        System.out.printf("Поток %s захватил %d%n", Thread.currentThread().getName(), to.getId());
        if (from.getBalance() >= amount) {
          from.withdraw(amount);
          to.deposit(amount);
        }
      }
    }
  }

  public void sendToAccount(BankAccount from, BankAccount to, int amount) {
    checkValidation(from, to, amount);

    BankAccount firstLock = from.getId() < to.getId() ? from : to;
    BankAccount secondLock = from.getId() < to.getId() ? to : from;

    synchronized (firstLock) {
      synchronized (secondLock) {
        if (from.getBalance() >= amount) {
          from.withdraw(amount);
          to.deposit(amount);
        } else {
          throw new IllegalArgumentException("Недостаточно средств на счете " + from.getId());
        }
      }
    }
  }

  private void checkValidation(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Счета не могут быть null");
    }
    if (from.getId() == to.getId()) {
      throw new IllegalArgumentException("Нельзя перевести деньги самому себе");
    }
    validateAmount(amount);
  }

  private void validateAmount(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Сумма перевода должна быть положительной");
    }
  }
}
