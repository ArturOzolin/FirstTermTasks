package com.mipt.arturozolin.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

  private final Password password = new Password();
  private final Jwt jwt = new Jwt();

  public Password getPassword() {
    return password;
  }

  public Jwt getJwt() {
    return jwt;
  }

  public static class Password {
    private String pepper = "";

    public String getPepper() {
      return pepper;
    }

    public void setPepper(String pepper) {
      this.pepper = pepper;
    }
  }

  public static class Jwt {
    private String secret;
    private long ttlSeconds = 900;
    private String issuer = "resilient-secure-http-gateway";

    public String getSecret() {
      return secret;
    }

    public void setSecret(String secret) {
      this.secret = secret;
    }

    public long getTtlSeconds() {
      return ttlSeconds;
    }

    public void setTtlSeconds(long ttlSeconds) {
      this.ttlSeconds = ttlSeconds;
    }

    public String getIssuer() {
      return issuer;
    }

    public void setIssuer(String issuer) {
      this.issuer = issuer;
    }
  }
}
