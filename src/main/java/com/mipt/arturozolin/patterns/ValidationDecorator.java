package com.mipt.arturozolin.patterns;

import java.util.Optional;

public class ValidationDecorator implements DataService {
  private final DataService wrappee;

  public ValidationDecorator(DataService dataService) {
    this.wrappee = dataService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    validateKey(key);
    return wrappee.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    validateKey(key);
    if (data == null || data.isEmpty()) {
      throw new IllegalArgumentException("данные не могут быть пустыми");
    }
    wrappee.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    validateKey(key);
    return wrappee.deleteData(key);
  }

  private void validateKey(String key) {
    if (key == null || key.isEmpty()) {
      throw new IllegalArgumentException("ключ не null, ключ не пустой");
    }
  }
}