package com.mipt.arturozolin;

import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.model.TaskDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void testGetAllTasks_Positive() {
    ResponseEntity<Task[]> response = restTemplate.getForEntity("/api/tasks", Task[].class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void testGetTaskById_Positive() {
    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/1", Task.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getTitle()).isEqualTo("Learn Spring");
  }

  @Test
  void testGetTaskById_Negative() {
    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/999", Task.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testCreateTask_Positive() {
    TaskDto dto = new TaskDto();
    dto.setTitle("Test Title");
    dto.setDescription("Test Desc");
    dto.setCompleted(false);

    ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", dto, Task.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody().getId()).isNotNull();
  }

  @Test
  void testCreateTask_Negative_ValidationFail() {
    TaskDto dto = new TaskDto();
    dto.setTitle("");

    ResponseEntity<String> response = restTemplate.postForEntity("/api/tasks", dto, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testUpdateTask_Positive() {
    TaskDto dto = new TaskDto();
    dto.setTitle("Updated Title");

    HttpEntity<TaskDto> requestUpdate = new HttpEntity<>(dto);
    ResponseEntity<Task> response = restTemplate.exchange("/api/tasks/1", HttpMethod.PUT, requestUpdate, Task.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getTitle()).isEqualTo("Updated Title");
  }

  @Test
  void testUpdateTask_Negative_NotFound() {
    TaskDto dto = new TaskDto();
    dto.setTitle("Updated Title");

    HttpEntity<TaskDto> requestUpdate = new HttpEntity<>(dto);
    ResponseEntity<Task> response = restTemplate.exchange("/api/tasks/nonexistent", HttpMethod.PUT, requestUpdate, Task.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testDeleteTask_Positive() {
    ResponseEntity<Void> response = restTemplate.exchange("/api/tasks/1", HttpMethod.DELETE, null, Void.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}