package com.mipt.arturozolin.exception;

public class GatewayUnavailableException extends RuntimeException {

  public GatewayUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }
}
