package com.mipt.arturozolin;

import com.mipt.arturozolin.dto.TaskCreateDto;
import com.mipt.arturozolin.dto.TaskResponseDto;
import com.mipt.arturozolin.mapper.TaskMapper;
import com.mipt.arturozolin.mapper.TaskMapperImpl;
import com.mipt.arturozolin.model.Priority;
import com.mipt.arturozolin.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TaskMapperImpl.class})
class TaskMapperTest {

  @Autowired
  private TaskMapper taskMapper;

  @Test
  void shouldMapTaskCreateDtoToEntity() {
    TaskCreateDto dto = new TaskCreateDto();
    dto.setTitle("Test Task");
    dto.setPriority(Priority.HIGH);
    dto.setDueDate(LocalDate.now().plusDays(1));

    Task entity = taskMapper.toEntity(dto);

    assertNotNull(entity);
    assertEquals(dto.getTitle(), entity.getTitle());
    assertEquals(dto.getPriority(), entity.getPriority());
    assertEquals(dto.getDueDate(), entity.getDueDate());
  }

  @Test
  void shouldMapEntityToResponseDto() {
    Task task = new Task(1L, "Title", "Desc", false);
    task.setPriority(Priority.LOW);

    TaskResponseDto response = taskMapper.toResponseDto(task);

    assertEquals(task.getId(), response.getId());
    assertEquals(task.getTitle(), response.getTitle());
    assertEquals(task.getPriority(), response.getPriority());
  }
}