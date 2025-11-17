package com.mipt.arturozolin.patterns;

import java.util.Optional;

public class LoggingDecorator implements DataService {
  private final DataService wrappee;

  public LoggingDecorator(DataService dataService) {
    this.wrappee = dataService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    return wrappee.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    wrappee.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    return wrappee.deleteData(key);
  }
}