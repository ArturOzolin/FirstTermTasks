package com.mipt.arturozolin.patterns;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class MetricableDecorator implements DataService {
  private final DataService wrappee;
  private final MetricService metricService = new MetricService();

  public MetricableDecorator(DataService dataService) {
    this.wrappee = dataService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    Instant start = Instant.now();
    Optional<String> result = wrappee.findDataByKey(key);
    Instant finish = Instant.now();
    metricService.sendMetric(Duration.between(start, finish));
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    Instant start = Instant.now();
    wrappee.saveData(key, data);
    Instant finish = Instant.now();
    metricService.sendMetric(Duration.between(start, finish));
  }

  @Override
  public boolean deleteData(String key) {
    Instant start = Instant.now();
    boolean result = wrappee.deleteData(key);
    Instant finish = Instant.now();
    metricService.sendMetric(Duration.between(start, finish));
    return result;
  }

  public static class MetricService {
    public void sendMetric(Duration duration) {
      System.out.println("Метод выполнялся: " + duration.toString());
    }
  }
}
