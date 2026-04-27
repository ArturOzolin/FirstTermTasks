package com.mipt.arturozolin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.external-api")
public class ExternalApiProperties {

  private String baseUrl = "http://localhost:8080/external";
  private String userAgent = "resilient-secure-http-gateway/1.0";
  private long connectTimeoutMs = 1000;
  private long readTimeoutMs = 1500;

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public long getConnectTimeoutMs() {
    return connectTimeoutMs;
  }

  public void setConnectTimeoutMs(long connectTimeoutMs) {
    this.connectTimeoutMs = connectTimeoutMs;
  }

  public long getReadTimeoutMs() {
    return readTimeoutMs;
  }

  public void setReadTimeoutMs(long readTimeoutMs) {
    this.readTimeoutMs = readTimeoutMs;
  }
}
