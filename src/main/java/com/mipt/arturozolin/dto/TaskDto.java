package com.mipt.arturozolin.dto;


import jakarta.validation.constraints.NotBlank;

/**
 * Объект передачи данных.
 * Используется на слое контроллеров для приема данных от клиента.
 * Содержит аннотации валидации для проверки корректности входящих запросов до того, как они попадут в сервисный слой.
 */
public class TaskDto {
  private Long id;
  @NotBlank(message = "Title cannot be empty")
  private String title;
  private String description;
  private boolean completed;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }
}