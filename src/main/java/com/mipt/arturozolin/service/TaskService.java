package com.mipt.arturozolin.service;

import com.mipt.arturozolin.config.PrototypeScopedBean;
import com.mipt.arturozolin.exception.TaskNotFoundException;
import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ObjectProvider<PrototypeScopedBean> idGeneratorProvider;

    @Value("${app.name:DefaultApp}")
    private String appName;

    @Value("${app.version:1.0}")
    private String appVersion;

    public TaskService(TaskRepository taskRepository, ObjectProvider<PrototypeScopedBean> idGeneratorProvider) {
        this.taskRepository = taskRepository;
        this.idGeneratorProvider = idGeneratorProvider;
    }

    @PostConstruct
    public void initCache() {
        log.info("Initializing app: {} v{}", appName, appVersion);
        /*
        Task defaultTask = new Task("1", "Learn Spring", "Read Longread 3", false);
        taskRepository.save(defaultTask);
        taskCache.put(defaultTask.getId(), defaultTask);
        log.info("Cache initialized with {} tasks.", taskCache.size());
        */
    }

    @PreDestroy
    public void cleanUp() {
        /*
        log.info("Application shutting down. Tasks in cache before destroy: {}", taskCache.size());
        taskCache.clear();
        */
    }

    public Task createTask(Task task) {
        // task.setId(idGeneratorProvider.getObject().generateId());
        return taskRepository.save(task);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAllWithAttachments();
    }

    @Transactional
    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(existing -> {
            existing.setTitle(updatedTask.getTitle());
            existing.setDescription(updatedTask.getDescription());
            existing.setCompleted(updatedTask.isCompleted());
            existing.setDueDate(updatedTask.getDueDate());
            existing.setPriority(updatedTask.getPriority());
            existing.setTags(updatedTask.getTags());
            return taskRepository.save(existing);
        }).orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    public Task getTaskRequired(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = TaskNotFoundException.class
    )
    public void bulkCompleteTasks(List<Long> ids) {
        for (Long id : ids) {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found. Rollback all!"));
            task.setCompleted(true);
            taskRepository.save(task);
        }
    }
}