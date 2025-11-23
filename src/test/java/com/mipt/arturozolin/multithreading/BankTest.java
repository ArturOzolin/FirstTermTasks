package com.mipt.arturozolin.multithreading;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

  private final Bank bank = new Bank();

  @Test
  void testConcurrentTransferSafety() throws InterruptedException {
    BankAccount acc1 = new BankAccount(1, 10000);
    BankAccount acc2 = new BankAccount(2, 10000);
    int initialTotal = acc1.getBalance() + acc2.getBalance();

    int threadsCount = 100;
    ExecutorService service = Executors.newFixedThreadPool(threadsCount);
    CountDownLatch latch = new CountDownLatch(threadsCount);

    for (int i = 0; i < threadsCount; i++) {
      service.submit(() -> {
        try {
          if (Math.random() > 0.5) {
            bank.sendToAccount(acc1, acc2, 10);
          } else {
            bank.sendToAccount(acc2, acc1, 10);
          }
        } catch (Exception e) {

        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    service.shutdown();

    int finalTotal = acc1.getBalance() + acc2.getBalance();

    assertEquals(initialTotal, finalTotal);
  }

  @Test
  void testValidation() {
    BankAccount acc1 = new BankAccount(1, 100);
    BankAccount acc2 = new BankAccount(2, 100);

    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(null, acc2, 50));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(acc1, acc2, -50));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(acc1, acc2, 5000));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(acc1, acc1, 10));
  }

  @Test
  void testDeadlockReproduction() throws InterruptedException {
    BankAccount acc1 = new BankAccount(1, 1000);
    BankAccount acc2 = new BankAccount(2, 1000);

    Thread t1 = new Thread(() -> {
      bank.sendToAccountDeadlock(acc1, acc2, 10);
    }, "Thread-1");

    Thread t2 = new Thread(() -> {
      bank.sendToAccountDeadlock(acc2, acc1, 10);
    }, "Thread-2");

    t1.start();
    t2.start();

    t1.join(2000);
    t2.join(2000);

    boolean isDeadlocked = t1.isAlive() && t2.isAlive();

    assertTrue(isDeadlocked);
  }
}
