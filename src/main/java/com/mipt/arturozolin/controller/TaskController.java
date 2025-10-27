package com.mipt.arturozolin.controller;

import com.mipt.arturozolin.config.RequestScopedBean;
import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.model.TaskDto;
import com.mipt.arturozolin.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST контроллер для управления задачами.
 * Обрабатывает HTTP запросы клиентов и возвращает JSON ответы.
 */
@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;
  private final RequestScopedBean requestScopedBean;

  public TaskController(TaskService taskService, RequestScopedBean requestScopedBean) {
    this.taskService = taskService;
    this.requestScopedBean = requestScopedBean;
  }

  @GetMapping
  public ResponseEntity<List<Task>> getAllTasks() {
    log.info("Handling request to get all tasks. Request ID: {}", requestScopedBean.getRequestId());
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Task> getTaskById(@PathVariable String id) {
    Task task = taskService.getTask(id);
    return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDto taskDto) {
    Task task = new Task(null, taskDto.getTitle(), taskDto.getDescription(), taskDto.isCompleted());
    return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable String id, @Valid @RequestBody TaskDto taskDto) {
    try {
      Task task = new Task(id, taskDto.getTitle(), taskDto.getDescription(), taskDto.isCompleted());
      return ResponseEntity.ok(taskService.updateTask(id, task));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable String id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }
}