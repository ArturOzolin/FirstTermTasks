package com.mipt.arturozolin.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DocsController {

  @GetMapping("/docs")
  public ResponseEntity<Map<String, Object>> docs() {
    Map<String, Object> body = Map.of(
            "title", "Internal Documentation",
            "sections", List.of(
                    Map.of("name", "Overview", "content", "Resilient Secure HTTP Gateway"),
                    Map.of("name", "Endpoints", "content", "/api/v1/tasks gateway with circuit breaker and rate limiter")));
    return ResponseEntity.ok(body);
  }
}
