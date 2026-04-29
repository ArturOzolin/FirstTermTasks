package com.mipt.arturozolin.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PepperPasswordEncoder implements PasswordEncoder {

  private final PasswordEncoder delegate;
  private final String pepper;

  public PepperPasswordEncoder(PasswordEncoder delegate, String pepper) {
    this.delegate = delegate;
    this.pepper = pepper == null ? "" : pepper;
  }

  @Override
  public String encode(CharSequence rawPassword) {
    return delegate.encode(applyPepper(rawPassword));
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return delegate.matches(applyPepper(rawPassword), encodedPassword);
  }

  private String applyPepper(CharSequence rawPassword) {
    return rawPassword + pepper;
  }
}
