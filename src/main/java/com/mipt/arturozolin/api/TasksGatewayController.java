package com.mipt.arturozolin.api;

import com.mipt.arturozolin.client.ExternalTasksClient;
import com.mipt.arturozolin.dto.GatewayTaskCreateRequest;
import com.mipt.arturozolin.dto.GatewayTaskDto;
import com.mipt.arturozolin.service.TasksGatewayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TasksGatewayController {

  private final TasksGatewayService gatewayService;

  public TasksGatewayController(TasksGatewayService gatewayService) {
    this.gatewayService = gatewayService;
  }

  @PostMapping
  public ResponseEntity<GatewayTaskDto> create(@Valid @RequestBody GatewayTaskCreateRequest request) {
    ExternalTasksClient.CreatedTask created = gatewayService.create(request);
    URI location = created.location();
    ResponseEntity.BodyBuilder builder = ResponseEntity.status(201);
    if (location != null) {
      builder.header(HttpHeaders.LOCATION, location.toString());
    }
    return builder.body(created.body());
  }

  @GetMapping("/{id}")
  public ResponseEntity<GatewayTaskDto> get(@PathVariable long id) {
    return ResponseEntity.ok(gatewayService.get(id));
  }

  @GetMapping
  public ResponseEntity<List<GatewayTaskDto>> list(
          @RequestParam(name = "completed", required = false) Boolean completed,
          @RequestParam(name = "limit", required = false) Integer limit) {
    return ResponseEntity.ok(gatewayService.list(completed, limit));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable long id) {
    gatewayService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
