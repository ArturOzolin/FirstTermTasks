package com.mipt.arturozolin.dto;

import com.mipt.arturozolin.model.Priority;
import com.mipt.arturozolin.model.validation.OnUpdate;
import com.mipt.arturozolin.validation.DueDateNotBeforeCreation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Данные для обновления существующей задачи")
@DueDateNotBeforeCreation(groups = OnUpdate.class)
public class TaskUpdateDto {
  @Schema(description = "Новый заголовок задачи")
  @Size(min = 3, max = 100, groups = OnUpdate.class)
  private String title;

  @Schema(description = "Новое описание задачи")
  @Size(max = 500, groups = OnUpdate.class)
  private String description;

  @Schema(description = "Статус выполнения")
  private Boolean completed;

  @Schema(description = "Новый срок выполнения")
  @FutureOrPresent(groups = OnUpdate.class)
  private LocalDate dueDate;

  @Schema(description = "Новый приоритет")
  private Priority priority;

  @Schema(description = "Новый набор тегов")
  @Size(max = 5, groups = OnUpdate.class)
  private Set<String> tags;

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

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }
}