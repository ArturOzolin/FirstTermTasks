package com.mipt.arturozolin.security;

public final class TokenMasker {

  private TokenMasker() {
  }

  public static String mask(String token) {
    if (token == null) {
      return "null";
    }
    if (token.length() <= 12) {
      return "****";
    }
    return token.substring(0, 6) + "..." + token.substring(token.length() - 6);
  }
}
