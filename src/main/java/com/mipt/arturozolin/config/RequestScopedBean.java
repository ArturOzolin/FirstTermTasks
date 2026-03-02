package com.mipt.arturozolin.config;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Бин со scope request. Хранит информацию о текущем HTTP запросе.
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedBean {
  private final String requestId = UUID.randomUUID().toString();
  private final LocalDateTime startTime = LocalDateTime.now();

  public String getRequestId() {
    return requestId;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }
}