package com.mipt.arturozolin.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Доменная модель задачи (Entity/Model).
 * Представляет собой основную бизнес-сущность приложения, хранящую информацию о задаче:
 * идентификатор, заголовок, описание и статус выполнения.
 */
public class Task {
  private String id;
  private String title;
  private String description;
  private boolean completed;
  private LocalDateTime createdAt;
  private LocalDate dueDate;
  private Priority priority;
  private Set<String> tags = new HashSet<>();

  public Task() {
  }

  public Task(String id, String title, String description, boolean completed) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public boolean isCompleted() { return completed; }
  public void setCompleted(boolean completed) { this.completed = completed; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
  public LocalDate getDueDate() { return dueDate; }
  public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
  public Priority getPriority() { return priority; }
  public void setPriority(Priority priority) { this.priority = priority; }
  public Set<String> getTags() { return tags; }
  public void setTags(Set<String> tags) { this.tags = tags; }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return completed == task.completed && Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, completed);
  }

  @Override
  public String toString() {
    return "Task{" + "id='" + id + '\'' + ", title='" + title + '\'' + ", description='" + description + '\'' + ", completed=" + completed + '}';
  }
}