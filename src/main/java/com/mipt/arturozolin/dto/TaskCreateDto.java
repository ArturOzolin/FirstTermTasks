package com.mipt.arturozolin.dto;

import com.mipt.arturozolin.model.Priority;
import com.mipt.arturozolin.model.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Данные для создания новой задачи")
public class TaskCreateDto {
  @Schema(description = "Заголовок задачи")
  @NotBlank(groups = OnCreate.class, message = "Title cannot be blank")
  @Size(min = 3, max = 100, groups = OnCreate.class)
  private String title;

  @Schema(description = "Описание задачи")
  @Size(max = 500, groups = OnCreate.class)
  private String description;

  @Schema(description = "Срок выполнения")
  @FutureOrPresent(groups = OnCreate.class)
  private LocalDate dueDate;

  @Schema(description = "Приоритет задачи")
  @NotNull(groups = OnCreate.class)
  private Priority priority;

  @Schema(description = "Набор тегов")
  @Size(max = 5, groups = OnCreate.class)
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