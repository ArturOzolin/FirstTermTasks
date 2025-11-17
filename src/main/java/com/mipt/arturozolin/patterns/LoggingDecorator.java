package com.mipt.arturozolin.patterns;

import java.util.Optional;

public class LoggingDecorator implements DataService {
  private final DataService wrappee;

  public LoggingDecorator(DataService dataService) {
    this.wrappee = dataService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    System.out.println("Finding data by key: " + key);
    return wrappee.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    System.out.println("Saving data with key: " + key);
    wrappee.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    System.out.println("Deleting data with key: " + key);
    return wrappee.deleteData(key);
  }
}
