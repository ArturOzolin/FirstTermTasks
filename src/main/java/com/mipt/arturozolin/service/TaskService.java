package com.mipt.arturozolin.service;

import com.mipt.arturozolin.config.PrototypeScopedBean;
import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервисный слой для управления задачами.
 * Содержит основную бизнес-логику, работает с репозиторием и внутренним кэшем.
 */
@Slf4j
@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final ObjectProvider<PrototypeScopedBean> idGeneratorProvider;
  private final Map<String, Task> taskCache = new ConcurrentHashMap<>();

  @Value("${app.name:DefaultApp}")
  private String appName;

  @Value("${app.version:1.0}")
  private String appVersion;

  public TaskService(TaskRepository taskRepository, ObjectProvider<PrototypeScopedBean> idGeneratorProvider) {
    this.taskRepository = taskRepository;
    this.idGeneratorProvider = idGeneratorProvider;
  }

  @PostConstruct
  public void initCache() {
    log.info("Initializing app: {} v{}", appName, appVersion);
    Task defaultTask = new Task("1", "Learn Spring", "Read Longread 3", false);
    taskRepository.save(defaultTask);
    taskCache.put(defaultTask.getId(), defaultTask);
    log.info("Cache initialized with {} tasks.", taskCache.size());
  }

  @PreDestroy
  public void cleanUp() {
    log.info("Application shutting down. Tasks in cache before destroy: {}", taskCache.size());
    taskCache.clear();
  }

  public Task createTask(Task task) {
    task.setId(idGeneratorProvider.getObject().generateId());
    Task saved = taskRepository.save(task);
    taskCache.put(saved.getId(), saved);
    return saved;
  }

  public Task getTask(String id) {
    return taskCache.getOrDefault(id, taskRepository.findById(id).orElse(null));
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Task updateTask(String id, Task updatedTask) {
    return taskRepository.findById(id).map(existing -> {
      existing.setTitle(updatedTask.getTitle());
      existing.setDescription(updatedTask.getDescription());
      existing.setCompleted(updatedTask.isCompleted());
      taskRepository.save(existing);
      taskCache.put(existing.getId(), existing);
      return existing;
    }).orElseThrow(() -> new RuntimeException("Task not found"));
  }

  public void deleteTask(String id) {
    taskRepository.deleteById(id);
    taskCache.remove(id);
  }
}