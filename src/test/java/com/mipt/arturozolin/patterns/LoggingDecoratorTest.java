package com.mipt.arturozolin.patterns;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoggingDecoratorTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private DataService mockService;
  private LoggingDecorator loggingDecorator;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
    mockService = mock(DataService.class);
    loggingDecorator = new LoggingDecorator(mockService);
  }

  @AfterEach
  void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  void findDataByKey_shouldLogAction() {
    loggingDecorator.findDataByKey("testKey");
    assertTrue(outContent.toString().contains("Finding data by key: testKey"));
    verify(mockService).findDataByKey("testKey");
  }

  @Test
  void saveData_shouldLogAction() {
    loggingDecorator.saveData("testKey", "testData");
    assertTrue(outContent.toString().contains("Saving data with key: testKey"));
    verify(mockService).saveData("testKey", "testData");
  }

  @Test
  void deleteData_shouldLogAction() {
    loggingDecorator.deleteData("testKey");
    assertTrue(outContent.toString().contains("Deleting data with key: testKey"));
    verify(mockService).deleteData("testKey");
  }
}