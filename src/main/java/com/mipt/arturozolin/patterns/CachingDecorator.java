package com.mipt.arturozolin.patterns;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CachingDecorator implements DataService {
  private final DataService wrappee;
  private final Map<String, String> cache = new HashMap<>();

  public CachingDecorator(DataService dataService) {
    this.wrappee = dataService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    if (cache.containsKey(key)) {
      return Optional.of(cache.get(key));
    }
    Optional<String> data = wrappee.findDataByKey(key);
    data.ifPresent(value -> cache.put(key, value));
    return data;
  }

  @Override
  public void saveData(String key, String data) {
    cache.put(key, data);
    wrappee.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    cache.remove(key);
    return wrappee.deleteData(key);
  }
}
