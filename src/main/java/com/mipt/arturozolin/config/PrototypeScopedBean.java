package com.mipt.arturozolin.config;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Бин со scope "prototype".
 */
@Component
@Scope("prototype")
public class PrototypeScopedBean {
  public String generateId() {
    return UUID.randomUUID().toString();
  }
}