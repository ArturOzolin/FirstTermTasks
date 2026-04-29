package com.mipt.arturozolin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.arturozolin.dto.TaskCreateDto;
import com.mipt.arturozolin.dto.TaskResponseDto;
import com.mipt.arturozolin.mapper.TaskMapper;
import com.mipt.arturozolin.model.Priority;
import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    void shouldCreateTaskAndReturn201WithJsonBody() throws Exception {
        TaskCreateDto requestDto = new TaskCreateDto();
        requestDto.setTitle("New task");
        requestDto.setDescription("Task description");
        requestDto.setPriority(Priority.HIGH);
        requestDto.setDueDate(LocalDate.now().plusDays(3));

        Task mappedEntity = new Task(null, "New task", "Task description", false);
        mappedEntity.setPriority(Priority.HIGH);

        Task savedEntity = new Task(101L, "New task", "Task description", false);
        savedEntity.setPriority(Priority.HIGH);

        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(101L);
        responseDto.setTitle("New task");
        responseDto.setDescription("Task description");
        responseDto.setCompleted(false);
        responseDto.setPriority(Priority.HIGH);

        when(taskMapper.toEntity(any(TaskCreateDto.class))).thenReturn(mappedEntity);
        when(taskService.createTask(any(Task.class))).thenReturn(savedEntity);
        when(taskMapper.toResponseDto(savedEntity)).thenReturn(responseDto);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.title").value("New task"))
                .andExpect(jsonPath("$.description").value("Task description"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void shouldReturn400WhenTitleIsTooShort() throws Exception {
        TaskCreateDto invalid = new TaskCreateDto();
        invalid.setTitle("Ab");
        invalid.setPriority(Priority.LOW);
        invalid.setDueDate(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details.title").exists());
    }

    @Test
    void shouldReturnExistingTaskByIdWith200() throws Exception {
        Long taskId = 7L;
        Task existing = new Task(taskId, "Existing task", "Existing description", true);
        existing.setPriority(Priority.MEDIUM);

        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(taskId);
        responseDto.setTitle("Existing task");
        responseDto.setDescription("Existing description");
        responseDto.setCompleted(true);
        responseDto.setPriority(Priority.MEDIUM);

        when(taskService.getTaskRequired(eq(taskId))).thenReturn(existing);
        when(taskMapper.toResponseDto(existing)).thenReturn(responseDto);

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("Existing task"))
                .andExpect(jsonPath("$.description").value("Existing description"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }
}
