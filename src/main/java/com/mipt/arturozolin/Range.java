package com.mipt.arturozolin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Range {
  long min() default 2;
  long max() default 50;
  String message() default "Value must be between 2 and 50 characters";
}
