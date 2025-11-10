package com.mipt.arturozolin;

import java.util.regex.Pattern;
import java.lang.reflect.Field;

public class Validator {
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

  public static ValidationResult validate(Object object) {
    ValidationResult result = new ValidationResult();
    final Class<?> objectClass = object.getClass();
    final Field[] allFields = objectClass.getDeclaredFields();
    for (Field field : allFields) {
      field.setAccessible(true);
      java.lang.annotation.Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
      try {
        Object fieldValue = field.get(object);
        for (java.lang.annotation.Annotation annotation : fieldAnnotations) {
          if (annotation instanceof NotNull) {
            NotNull notNullAnnotation = (NotNull) annotation;
            if (fieldValue == null) {
              result.addError(field.getName() + ": " + notNullAnnotation.message());
            }
          }
          if (annotation instanceof Size) {
            Size sizeAnnotation = (Size) annotation;
            if (fieldValue != null && fieldValue instanceof String) {
              String stringValue = (String) fieldValue;
              int length = stringValue.length();
              if (length < sizeAnnotation.min() || length > sizeAnnotation.max()) {
                result.addError(field.getName() + ": " + sizeAnnotation.message());
              }
            }
          }
          if (annotation instanceof Range) {
            Range rangeAnnotation = (Range) annotation;
            if (fieldValue != null) {
              if (fieldValue instanceof Number) {
                long value = ((Number) fieldValue).longValue();
                if (value < rangeAnnotation.min() || value > rangeAnnotation.max()) {
                  result.addError(field.getName() + ": " + rangeAnnotation.message());
                }
              }
            }
          }
          if (annotation instanceof Email) {
            Email emailAnnotation = (Email) annotation;
            if (fieldValue != null && fieldValue instanceof String) {
              String emailValue = (String) fieldValue;
              if (!EMAIL_PATTERN.matcher(emailValue).matches()) {
                result.addError(field.getName() + ": " + emailAnnotation.message());
              }
            }
          }
        }
      } catch (IllegalAccessException e) {
        result.addError(field.getName() + ": " + e.getMessage());
      }
    }
    return result;
  }
}
