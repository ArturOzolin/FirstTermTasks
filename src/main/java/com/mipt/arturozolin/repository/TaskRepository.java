package com.mipt.arturozolin.repository;

import com.mipt.arturozolin.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Базовый интерфейс репозитория для работы с хранилищем задач.
 * Определяет стандартные CRUD операции.
 */public interface TaskRepository {
  Task save(Task task);

  Optional<Task> findById(String id);

  List<Task> findAll();

  void deleteById(String id);
}