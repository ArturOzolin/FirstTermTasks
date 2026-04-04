package com.mipt.arturozolin.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DueDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DueDateNotBeforeCreation {
  String message() default "Due date cannot be before creation date";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}