package com.mipt.arturozolin.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.arturozolin.dto.GatewayTaskCreateRequest;
import com.mipt.arturozolin.dto.GatewayTaskDto;
import com.mipt.arturozolin.dto.ProblemDetailsDto;
import com.mipt.arturozolin.exception.ExternalApiException;
import com.mipt.arturozolin.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ExternalTasksClient {

  private static final Logger log = LoggerFactory.getLogger(ExternalTasksClient.class);
  private static final int LOG_BODY_LIMIT = 512;

  private final RestClient restClient;
  private final ObjectMapper objectMapper;

  public ExternalTasksClient(@Qualifier("externalRestClient") RestClient restClient,
                             ObjectMapper objectMapper) {
    this.restClient = restClient;
    this.objectMapper = objectMapper;
  }

  public CreatedTask create(GatewayTaskCreateRequest request) {
    try {
      ResponseEntity<GatewayTaskDto> entity = restClient.post()
              .uri("/v1/tasks")
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .body(request)
              .retrieve()
              .toEntity(GatewayTaskDto.class);
      URI location = entity.getHeaders().getLocation();
      return new CreatedTask(entity.getBody(), location);
    } catch (RestClientResponseException e) {
      throw mapClientError(e, null);
    } catch (ResourceAccessException e) {
      log.warn("External API unreachable while creating task: {}", e.getMessage());
      throw new ExternalApiException("External API unreachable: " + e.getMessage(), 503, e);
    }
  }

  public GatewayTaskDto getById(long id) {
    try {
      return restClient.get()
              .uri("/v1/tasks/{id}", id)
              .accept(MediaType.APPLICATION_JSON)
              .retrieve()
              .body(GatewayTaskDto.class);
    } catch (RestClientResponseException e) {
      throw mapClientError(e, id);
    } catch (ResourceAccessException e) {
      log.warn("External API unreachable while fetching task id={}: {}", id, e.getMessage());
      throw new ExternalApiException("External API unreachable: " + e.getMessage(), 503, e);
    }
  }

  public List<GatewayTaskDto> list(Boolean completed, Integer limit) {
    try {
      return restClient.get()
              .uri(uriBuilder -> {
                uriBuilder.path("/v1/tasks");
                if (completed != null) {
                  uriBuilder.queryParam("completed", completed);
                }
                if (limit != null) {
                  uriBuilder.queryParam("limit", limit);
                }
                return uriBuilder.build();
              })
              .accept(MediaType.APPLICATION_JSON)
              .retrieve()
              .body(new ParameterizedTypeReference<List<GatewayTaskDto>>() {});
    } catch (RestClientResponseException e) {
      throw mapClientError(e, null);
    } catch (ResourceAccessException e) {
      log.warn("External API unreachable while listing tasks: {}", e.getMessage());
      throw new ExternalApiException("External API unreachable: " + e.getMessage(), 503, e);
    }
  }

  public void delete(long id) {
    try {
      restClient.delete()
              .uri("/v1/tasks/{id}", id)
              .retrieve()
              .toBodilessEntity();
    } catch (RestClientResponseException e) {
      throw mapClientError(e, id);
    } catch (ResourceAccessException e) {
      log.warn("External API unreachable while deleting task id={}: {}", id, e.getMessage());
      throw new ExternalApiException("External API unreachable: " + e.getMessage(), 503, e);
    }
  }

  private RuntimeException mapClientError(RestClientResponseException e, Long taskId) {
    int status = e.getStatusCode().value();
    HttpHeaders headers = e.getResponseHeaders();
    MediaType contentType = headers != null ? headers.getContentType() : null;
    byte[] body = e.getResponseBodyAsByteArray();

    if (status == 404) {
      ProblemDetailsDto pd = tryParseProblem(body, contentType);
      String detail = pd != null && pd.getDetail() != null
              ? pd.getDetail()
              : "Task not found" + (taskId != null ? " id=" + taskId : "");
      return new TaskNotFoundException(detail);
    }

    if (status >= 500) {
      String safeBody = safeBodyPreview(body, contentType);
      log.warn("External 5xx status={} contentType={} body={}", status, contentType, safeBody);
      return new ExternalApiException("External API server error: " + status, status, e);
    }

    String safeBody = safeBodyPreview(body, contentType);
    log.warn("External 4xx status={} contentType={} body={}", status, contentType, safeBody);
    return new ExternalApiException("External API client error: " + status, status, e);
  }

  private ProblemDetailsDto tryParseProblem(byte[] body, MediaType contentType) {
    if (body == null || body.length == 0) {
      return null;
    }
    if (contentType != null && !contentType.includes(MediaType.APPLICATION_JSON)
            && !"application/problem+json".equalsIgnoreCase(contentType.toString())) {
      log.warn("Unexpected Content-Type for ProblemDetails: {} body={}", contentType,
              safeBodyPreview(body, contentType));
      return null;
    }
    try {
      return objectMapper.readValue(body, ProblemDetailsDto.class);
    } catch (Exception ex) {
      log.warn("Failed to parse ProblemDetails: {}", ex.getMessage());
      return null;
    }
  }

  private static String safeBodyPreview(byte[] body, MediaType contentType) {
    if (body == null || body.length == 0) {
      return "";
    }
    int limit = Math.min(body.length, LOG_BODY_LIMIT);
    String preview = new String(body, 0, limit, StandardCharsets.UTF_8);
    return contentType + ":" + preview + (body.length > limit ? "...(truncated)" : "");
  }

  public record CreatedTask(GatewayTaskDto body, URI location) {
  }
}
