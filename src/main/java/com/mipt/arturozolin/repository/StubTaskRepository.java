package com.mipt.arturozolin.repository;

import com.mipt.arturozolin.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Заглушка для репозитория задач.
 * Имитирует работу с хранилищем, возвращая заранее заданные данные
 * для методов чтения и игнорируя операции изменения/удаления.
 */
public class StubTaskRepository implements TaskRepository {
  @Override
  public Task save(Task task) {
    return task;
  }

  @Override
  public Optional<Task> findById(String id) {
    return Optional.of(new Task("stub-1", "Stub Task", "Stub Desc", false));
  }

  @Override
  public List<Task> findAll() {
    return List.of(new Task("stub-1", "Stub Task", "Stub Desc", false));
  }

  @Override
  public void deleteById(String id) {
  }
}