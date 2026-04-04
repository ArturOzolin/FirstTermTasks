package com.mipt.arturozolin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Главный класс приложения.
 * Является точкой входа для запуска Spring Boot приложения.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class TodoListApplication {
  public static void main(String[] args) {
    SpringApplication.run(TodoListApplication.class, args);
  }
}