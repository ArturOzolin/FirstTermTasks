package com.mipt.arturozolin.service;

import com.mipt.arturozolin.model.Priority;
import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {TaskService.class},
        properties = {
                "app.name=todo-test",
                "app.version=test"
        }
)
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    void shouldUpdateExistingTaskStatusToCompleted() {
        Long taskId = 42L;
        Task existing = new Task(taskId, "Buy milk", "2 liters", false);
        existing.setPriority(Priority.LOW);
        existing.setDueDate(LocalDate.now().plusDays(1));

        Task incoming = new Task(null, "Buy milk", "2 liters", true);
        incoming.setPriority(Priority.LOW);
        incoming.setDueDate(LocalDate.now().plusDays(1));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task result = taskService.updateTask(taskId, incoming);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.isCompleted()).isTrue();

        ArgumentCaptor<Task> savedCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(savedCaptor.capture());
        verify(taskRepository, never()).deleteById(any());
        verifyNoMoreInteractions(taskRepository);

        Task savedTask = savedCaptor.getValue();
        assertThat(savedTask.getId()).isEqualTo(taskId);
        assertThat(savedTask.isCompleted()).isTrue();
        assertThat(savedTask.getTitle()).isEqualTo("Buy milk");
        assertThat(savedTask.getDescription()).isEqualTo("2 liters");
    }
}
