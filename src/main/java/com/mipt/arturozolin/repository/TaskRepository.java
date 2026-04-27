package com.mipt.arturozolin.repository;

import com.mipt.arturozolin.model.Priority;
import com.mipt.arturozolin.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Базовый интерфейс репозитория для работы с хранилищем задач.
 * Определяет стандартные CRUD операции.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompletedAndPriority(boolean completed, Priority priority);

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN CURRENT_DATE AND :endDate")
    List<Task> findTasksDueInDays(LocalDate endDate);

    @EntityGraph(attributePaths = {"attachments", "tags"})
    @Query("SELECT t FROM Task t")
    List<Task> findAllWithAttachments();

    @EntityGraph(attributePaths = {"attachments", "tags"})
    Optional<Task> findWithAttachmentsById(Long id);
}