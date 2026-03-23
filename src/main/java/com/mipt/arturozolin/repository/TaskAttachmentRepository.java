package com.mipt.arturozolin.repository;

import com.mipt.arturozolin.model.TaskAttachment;

import java.util.List;
import java.util.Optional;

public interface TaskAttachmentRepository {
  TaskAttachment save(TaskAttachment attachment);

  Optional<TaskAttachment> findById(Long id);

  List<TaskAttachment> findByTaskId(String taskId);

  void deleteById(Long id);
}