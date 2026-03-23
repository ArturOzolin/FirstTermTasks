package com.mipt.arturozolin.repository;

import com.mipt.arturozolin.model.Task;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация репозитория задач, хранящая данные в оперативной памяти.
 * Помечен аннотацией @Primary, поэтому Spring внедряет эту реализацию по умолчанию,
 * если при инжекции интерфейса TaskRepository явно не указан @Qualifier.
 */
@Repository
@Primary
public class InMemoryTaskRepository implements TaskRepository {
  private final Map<String, Task> storage = new ConcurrentHashMap<>();

  @Override
  public Task save(Task task) {
    storage.put(task.getId(), task);
    return task;
  }

  @Override
  public Optional<Task> findById(String id) {

    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public List<Task> findAll() {

    return new ArrayList<>(storage.values());
  }

  @Override
  public void deleteById(String id) {
    storage.remove(id);
  }
}