package com.mipt.arturozolin.external;

import com.mipt.arturozolin.dto.GatewayTaskCreateRequest;
import com.mipt.arturozolin.dto.GatewayTaskDto;
import com.mipt.arturozolin.dto.ProblemDetailsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/external/v1")
public class ExternalApiController {

  private static final Logger log = LoggerFactory.getLogger(ExternalApiController.class);

  private final Map<Long, GatewayTaskDto> store = new ConcurrentHashMap<>();
  private final AtomicLong sequence = new AtomicLong(1);

  @PostMapping("/tasks")
  public ResponseEntity<GatewayTaskDto> create(@RequestBody GatewayTaskCreateRequest request) {
    long id = sequence.getAndIncrement();
    GatewayTaskDto task = new GatewayTaskDto(id, request.getTitle(), request.getDescription(), request.isCompleted());
    store.put(id, task);
    URI location = URI.create("/external/v1/tasks/" + id);
    return ResponseEntity.created(location).body(task);
  }

  @GetMapping("/tasks/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    GatewayTaskDto task = store.get(id);
    if (task == null) {
      return notFound(id);
    }
    return ResponseEntity.ok(task);
  }

  @GetMapping("/tasks")
  public ResponseEntity<List<GatewayTaskDto>> list(
          @RequestParam(name = "completed", required = false) Boolean completed,
          @RequestParam(name = "limit", required = false, defaultValue = "50") int limit) {
    List<GatewayTaskDto> result = store.values().stream()
            .filter(t -> completed == null || t.isCompleted() == completed)
            .limit(Math.max(0, limit))
            .toList();
    return ResponseEntity.ok(result);
  }

  @PutMapping("/tasks/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody GatewayTaskCreateRequest request) {
    if (!store.containsKey(id)) {
      return notFound(id);
    }
    GatewayTaskDto updated = new GatewayTaskDto(id, request.getTitle(), request.getDescription(), request.isCompleted());
    store.put(id, updated);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    GatewayTaskDto removed = store.remove(id);
    if (removed == null) {
      return notFound(id);
    }
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/unstable")
  public ResponseEntity<?> unstable(@RequestParam("mode") String mode) {
    log.info("External /unstable invoked mode={}", mode);
    return switch (Objects.requireNonNullElse(mode, "")) {
      case "timeout" -> {
        try {
          Thread.sleep(5_000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        yield ResponseEntity.ok(Map.of("status", "late"));
      }
      case "500" -> ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON)
              .body(new ProblemDetailsDto("about:blank", "Internal Server Error", 500, "Boom"));
      case "429" -> ResponseEntity.status(429)
              .header(HttpHeaders.RETRY_AFTER, "5")
              .contentType(MediaType.APPLICATION_JSON)
              .body(new ProblemDetailsDto("about:blank", "Too Many Requests", 429, "Slow down"));
      case "html" -> ResponseEntity.status(502)
              .contentType(MediaType.TEXT_HTML)
              .body("<html><body><h1>502 Bad Gateway</h1></body></html>");
      default -> ResponseEntity.badRequest()
              .contentType(MediaType.APPLICATION_JSON)
              .body(new ProblemDetailsDto("about:blank", "Bad Request", 400, "Unknown mode: " + mode));
    };
  }

  private ResponseEntity<ProblemDetailsDto> notFound(long id) {
    ProblemDetailsDto pd = new ProblemDetailsDto(
            "about:blank",
            "Not Found",
            404,
            "Task " + id + " not found");
    return ResponseEntity.status(404)
            .contentType(MediaType.APPLICATION_JSON)
            .body(pd);
  }
}
