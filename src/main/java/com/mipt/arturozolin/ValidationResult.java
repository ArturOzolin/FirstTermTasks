package com.mipt.arturozolin;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
public class ValidationResult {
  private boolean isValid;
  private List<String> errors;

  public ValidationResult() {
    this.isValid = true;
    this.errors = new ArrayList<>();
  }

  public void addError(String errorMessage) {
    this.errors.add(errorMessage);
    this.isValid = false;
  }
}
