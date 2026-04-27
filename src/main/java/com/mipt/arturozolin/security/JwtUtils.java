package com.mipt.arturozolin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtUtils {

  private final SecurityProperties properties;
  private final SecretKey signingKey;

  public JwtUtils(SecurityProperties properties) {
    this.properties = properties;
    byte[] keyBytes = resolveKeyBytes(properties.getJwt().getSecret());
    this.signingKey = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generate(String username, List<String> authorities) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(properties.getJwt().getTtlSeconds());
    return Jwts.builder()
            .issuer(properties.getJwt().getIssuer())
            .subject(username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .claim("authorities", authorities)
            .signWith(signingKey, Jwts.SIG.HS256)
            .compact();
  }

  public Jws<Claims> parseAndValidate(String token) {
    return Jwts.parser()
            .verifyWith(signingKey)
            .requireIssuer(properties.getJwt().getIssuer())
            .build()
            .parseSignedClaims(token);
  }

  public long getTtlSeconds() {
    return properties.getJwt().getTtlSeconds();
  }

  private static byte[] resolveKeyBytes(String secret) {
    if (secret == null || secret.isBlank()) {
      throw new IllegalStateException("app.security.jwt.secret must not be blank");
    }
    try {
      byte[] decoded = Decoders.BASE64.decode(secret);
      if (decoded.length >= 32) {
        return decoded;
      }
    } catch (RuntimeException ignored) {
    }
    byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
    if (raw.length < 32) {
      throw new IllegalStateException("JWT secret must be at least 32 bytes for HS256");
    }
    return raw;
  }
}
