package com.mipt.arturozolin.exception;

public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(String message) { super(message); }
}