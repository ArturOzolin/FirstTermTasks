package com.mipt.arturozolin.controller;

import com.mipt.arturozolin.dto.TaskCreateDto;
import com.mipt.arturozolin.dto.TaskResponseDto;
import com.mipt.arturozolin.dto.TaskUpdateDto;
import com.mipt.arturozolin.mapper.TaskMapper;
import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.model.validation.OnCreate;
import com.mipt.arturozolin.model.validation.OnUpdate;
import com.mipt.arturozolin.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "Операции для управления задачами")
public class TaskController {

  private final TaskService taskService;
  private final TaskMapper taskMapper;

  public TaskController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @Operation(summary = "Получить все задачи", description = "Возвращает список всех существующих задач")
  @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
  @GetMapping
  public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
    List<Task> tasks = taskService.getAllTasks();
    List<TaskResponseDto> response = tasks.stream().map(taskMapper::toResponseDto).collect(Collectors.toList());
    return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(response.size()))
            .body(response);
  }

  @Operation(summary = "Получить задачу по ID", description = "Возвращает данные конкретной задачи по её идентификатору")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Задача найдена"),
          @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  @GetMapping("/{id}")
  public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
    Task task = taskService.getTaskRequired(id);
    return ResponseEntity.ok(taskMapper.toResponseDto(task));
  }

  @Operation(summary = "Создать новую задачу", description = "Создает новую задачу на основе переданных данных")
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
          @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных")
  })
  @PostMapping
  public ResponseEntity<TaskResponseDto> createTask(@Validated(OnCreate.class) @RequestBody TaskCreateDto taskDto) {
    Task task = taskMapper.toEntity(taskDto);
    Task saved = taskService.createTask(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toResponseDto(saved));
  }

  @Operation(summary = "Обновить задачу", description = "Частичное обновление полей задачи по её ID")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
          @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных"),
          @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  @PutMapping("/{id}")
  public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody TaskUpdateDto taskDto) {
    Task existing = taskService.getTaskRequired(id);
    Task updated = taskMapper.updateEntity(taskDto, existing);
    taskService.updateTask(id, updated);
    return ResponseEntity.ok(taskMapper.toResponseDto(updated));
  }

  @Operation(summary = "Удалить задачу", description = "Удаляет задачу по её идентификатору")
  @ApiResponse(responseCode = "204", description = "Задача успешно удалена")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }
}