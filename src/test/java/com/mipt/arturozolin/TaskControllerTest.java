package com.mipt.arturozolin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.arturozolin.controller.TaskController;
import com.mipt.arturozolin.dto.TaskCreateDto;
import com.mipt.arturozolin.mapper.TaskMapper;
import com.mipt.arturozolin.model.Priority;
import com.mipt.arturozolin.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @MockBean
  private TaskMapper taskMapper;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldReturnTotalCountHeader() throws Exception {
    when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andExpect(header().string("X-Total-Count", "0"))
            .andExpect(header().exists("X-API-Version"));
  }

  @Test
  void shouldReturn400WhenTitleIsTooShort() throws Exception {
    TaskCreateDto invalidDto = new TaskCreateDto();
    invalidDto.setTitle("Ab");
    invalidDto.setPriority(Priority.LOW);

    mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.details.title").exists());
  }
}