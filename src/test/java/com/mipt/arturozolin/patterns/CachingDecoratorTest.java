package com.mipt.arturozolin.patterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.mockito.Mockito.*;

class CachingDecoratorTest {

  private DataService mockService;
  private CachingDecorator cachingDecorator;

  @BeforeEach
  void setUp() {
    mockService = Mockito.mock(SimpleDataService.class);
    cachingDecorator = new CachingDecorator(mockService);
  }

  @Test
  void findDataByKey_shouldCacheResult() {
    when(mockService.findDataByKey("key1")).thenReturn(Optional.of("data1"));

    cachingDecorator.findDataByKey("key1");
    cachingDecorator.findDataByKey("key1");

    verify(mockService, times(1)).findDataByKey("key1");
  }

  @Test
  void saveData_shouldUpdateCache() {
    when(mockService.findDataByKey("key1")).thenReturn(Optional.of("oldData"));
    cachingDecorator.findDataByKey("key1");

    cachingDecorator.saveData("key1", "newData");
    cachingDecorator.findDataByKey("key1");

    verify(mockService, times(1)).findDataByKey("key1");
  }

  @Test
  void deleteData_shouldInvalidateCache() {
    when(mockService.findDataByKey("key1")).thenReturn(Optional.of("data1"));

    cachingDecorator.findDataByKey("key1");
    cachingDecorator.deleteData("key1");
    cachingDecorator.findDataByKey("key1");

    verify(mockService, times(2)).findDataByKey("key1");
  }
}