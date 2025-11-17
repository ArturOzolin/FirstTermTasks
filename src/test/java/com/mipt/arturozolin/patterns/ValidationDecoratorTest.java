package com.mipt.arturozolin.patterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ValidationDecoratorTest {

  private DataService mockService;
  private ValidationDecorator validationDecorator;

  @BeforeEach
  void setUp() {
    mockService = mock(DataService.class);
    validationDecorator = new ValidationDecorator(mockService);
  }

  @Test
  void findDataByKey_shouldThrowExceptionForNullKey() {
    assertThrows(IllegalArgumentException.class, () -> validationDecorator.findDataByKey(null));
  }

  @Test
  void findDataByKey_shouldThrowExceptionForEmptyKey() {
    assertThrows(IllegalArgumentException.class, () -> validationDecorator.findDataByKey(""));
  }

  @Test
  void saveData_shouldThrowExceptionForNullKey() {
    assertThrows(IllegalArgumentException.class, () -> validationDecorator.saveData(null, "data"));
  }

  @Test
  void saveData_shouldThrowExceptionForEmptyData() {
    assertThrows(IllegalArgumentException.class, () -> validationDecorator.saveData("key", ""));
  }

  @Test
  void saveData_shouldCallWrappedMethodForValidData() {
    validationDecorator.saveData("key", "data");
    verify(mockService, times(1)).saveData("key", "data");
  }

  @Test
  void deleteData_shouldThrowExceptionForEmptyKey() {
    assertThrows(IllegalArgumentException.class, () -> validationDecorator.deleteData(""));
  }
}