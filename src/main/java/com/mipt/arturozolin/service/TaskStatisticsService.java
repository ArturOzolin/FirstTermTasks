package com.mipt.arturozolin.service;

import com.mipt.arturozolin.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Сервис для сбора статистики и демонстрации работы @Qualifier.
 */
@Slf4j
@Service
public class TaskStatisticsService {

  private final TaskRepository primaryRepo;
  private final TaskRepository stubRepo;

  public TaskStatisticsService(
          TaskRepository primaryRepo,
          @Qualifier("stubTaskRepository") TaskRepository stubRepo) {
    this.primaryRepo = primaryRepo;
    this.stubRepo = stubRepo;
  }

  public void compareRepositories() {
    log.info("Primary repo size: {}", primaryRepo.findAll().size());
    log.info("Stub repo size: {}", stubRepo.findAll().size());
  }
}