  package com.mipt.arturozolin.repository;

  import com.mipt.arturozolin.model.TaskAttachment;
  import org.springframework.stereotype.Repository;
  import java.util.List;
  import java.util.Map;
  import java.util.Optional;
  import java.util.concurrent.ConcurrentHashMap;
  import java.util.concurrent.atomic.AtomicLong;
  import java.util.stream.Collectors;

  @Repository
  public class InMemoryTaskAttachmentRepository implements TaskAttachmentRepository {
    private final Map<Long, TaskAttachment> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public TaskAttachment save(TaskAttachment attachment) {
      if (attachment.getId() == null) {
        attachment.setId(idGenerator.getAndIncrement());
      }
      storage.put(attachment.getId(), attachment);
      return attachment;
    }

    @Override
    public Optional<TaskAttachment> findById(Long id) {
      return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<TaskAttachment> findByTaskId(String taskId) {
      return storage.values().stream()
              .filter(a -> a.getTaskId().equals(taskId))
              .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
      storage.remove(id);
    }
  }