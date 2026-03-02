package com.mipt.arturozolin.config;

import com.mipt.arturozolin.repository.StubTaskRepository;
import com.mipt.arturozolin.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс приложения.
 */
@Configuration
public class AppConfig {

  @Bean(name = "stubTaskRepository")
  public TaskRepository stubTaskRepository() {
    return new StubTaskRepository();
  }
}