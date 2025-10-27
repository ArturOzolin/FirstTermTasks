package com.mipt.arturozolin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {

  private TestUser user;

  private TestUser getValidUser() {
    return new TestUser("ValidName", "valid@example.com", 25, "secure123");
  }

  @BeforeEach
  void setUp() {
    user = getValidUser();
  }

  @Test
  void testValidObjectSuccess() {
    ValidationResult result = Validator.validate(user);
    assertTrue(result.isValid());
    assertEquals(0, result.getErrors().size());
  }

  @Test
  void testNotNullAndSizeErrors() {
    user.setName(null);
    user.setPassword("s");
    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertEquals(2, result.getErrors().size());
    assertTrue(result.getErrors().contains("name: Имя не может быть null"));
    assertTrue(result.getErrors().contains("password: Пароль должен быть от 6 до 20 символов"));
  }

  @Test
  void testRangeAndEmailErrors() {
    user.setAge(200);
    user.setEmail("sadsdda");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertEquals(2, result.getErrors().size());
    assertTrue(result.getErrors().contains("age: Возраст должен быть от 0 до 150"));
    assertTrue(result.getErrors().contains("email: Некорректный формат email"));
  }

  @Test
  void testBoundaryCaseSize() {
    user.setName("A");
    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertEquals(1, result.getErrors().size());
    assertTrue(result.getErrors().contains("name: Имя должно быть от 2 до 50 символов"));
  }

  @Test
  void nullObject_ShouldThrowException() {
    assertThrows(NullPointerException.class, () -> {
      Validator.validate(null);
    });
  }
}