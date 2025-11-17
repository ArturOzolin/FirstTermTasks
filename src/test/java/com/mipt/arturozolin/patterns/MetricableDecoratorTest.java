package com.mipt.arturozolin.patterns;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MetricableDecoratorTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private DataService mockService;
  private MetricableDecorator metricableDecorator;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
    mockService = mock(DataService.class);
    metricableDecorator = new MetricableDecorator(mockService);
  }

  @AfterEach
  void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  void findDataByKey_shouldSendMetric() {
    metricableDecorator.findDataByKey("testKey");
    assertTrue(outContent.toString().contains("Метод выполнялся:"));
    verify(mockService).findDataByKey("testKey");
  }

  @Test
  void saveData_shouldSendMetric() {
    metricableDecorator.saveData("testKey", "testData");
    assertTrue(outContent.toString().contains("Метод выполнялся:"));
    verify(mockService).saveData("testKey", "testData");
  }

  @Test
  void deleteData_shouldSendMetric() {
    metricableDecorator.deleteData("testKey");
    assertTrue(outContent.toString().contains("Метод выполнялся:"));
    verify(mockService).deleteData("testKey");
  }
}
