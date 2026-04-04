package com.mipt.arturozolin;

import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.model.TaskAttachment;
import com.mipt.arturozolin.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveTaskWithAttachmentAndFindIt() {
        Task task = new Task(null, "Test Task", "Desc", false);
        task.setDueDate(LocalDate.now().plusDays(2));
        entityManager.persistAndFlush(task);
        entityManager.clear();
        List<Task> dueTasks = taskRepository.findTasksDueInDays(LocalDate.now().plusDays(7));
        assertThat(dueTasks).hasSize(1);
    }

    @Test
    void testBulkCompleteTasksLogic() {
        Task task1 = taskRepository.save(new Task(null, "Task 1", "D1", false));
        Task task2 = taskRepository.save(new Task(null, "Task 2", "D2", false));

        task1.setCompleted(true);
        task2.setCompleted(true);
        taskRepository.saveAll(List.of(task1, task2));

        assertThat(taskRepository.findById(task1.getId()).get().isCompleted()).isTrue();
        assertThat(taskRepository.findById(task2.getId()).get().isCompleted()).isTrue();
    }
}