package com.mipt.arturozolin.service;

import com.mipt.arturozolin.client.ExternalTasksClient;
import com.mipt.arturozolin.dto.GatewayTaskCreateRequest;
import com.mipt.arturozolin.dto.GatewayTaskDto;
import com.mipt.arturozolin.exception.ExternalApiException;
import com.mipt.arturozolin.exception.GatewayUnavailableException;
import com.mipt.arturozolin.exception.TaskNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksGatewayService {

  private static final Logger log = LoggerFactory.getLogger(TasksGatewayService.class);
  private static final String INSTANCE = "externalApi";

  private final ExternalTasksClient client;

  public TasksGatewayService(ExternalTasksClient client) {
    this.client = client;
  }

  @RateLimiter(name = INSTANCE)
  @CircuitBreaker(name = INSTANCE, fallbackMethod = "createFallback")
  public ExternalTasksClient.CreatedTask create(GatewayTaskCreateRequest request) {
    return client.create(request);
  }

  @RateLimiter(name = INSTANCE)
  @CircuitBreaker(name = INSTANCE, fallbackMethod = "getFallback")
  public GatewayTaskDto get(long id) {
    return client.getById(id);
  }

  @RateLimiter(name = INSTANCE)
  @CircuitBreaker(name = INSTANCE, fallbackMethod = "listFallback")
  public List<GatewayTaskDto> list(Boolean completed, Integer limit) {
    return client.list(completed, limit);
  }

  @RateLimiter(name = INSTANCE)
  @CircuitBreaker(name = INSTANCE, fallbackMethod = "deleteFallback")
  public void delete(long id) {
    client.delete(id);
  }

  @SuppressWarnings("unused")
  public ExternalTasksClient.CreatedTask createFallback(GatewayTaskCreateRequest request, Throwable t) {
    rethrowIfTerminal(t);
    log.warn("createFallback engaged: {}", t.toString());
    throw new GatewayUnavailableException("External tasks service is temporarily unavailable", t);
  }

  @SuppressWarnings("unused")
  public GatewayTaskDto getFallback(long id, Throwable t) {
    rethrowIfTerminal(t);
    log.warn("getFallback engaged for id={}: {}", id, t.toString());
    return new GatewayTaskDto(id, "[degraded] task " + id, "External service unavailable, returning stub", false);
  }

  @SuppressWarnings("unused")
  public List<GatewayTaskDto> listFallback(Boolean completed, Integer limit, Throwable t) {
    rethrowIfTerminal(t);
    log.warn("listFallback engaged: {}", t.toString());
    return List.of();
  }

  @SuppressWarnings("unused")
  public void deleteFallback(long id, Throwable t) {
    rethrowIfTerminal(t);
    log.warn("deleteFallback engaged for id={}: {}", id, t.toString());
    throw new GatewayUnavailableException("External tasks service is temporarily unavailable", t);
  }

  private static void rethrowIfTerminal(Throwable t) {
    if (t instanceof RequestNotPermitted rnp) {
      throw rnp;
    }
    if (t instanceof TaskNotFoundException tnf) {
      throw tnf;
    }
    if (t instanceof ExternalApiException eae && eae.getStatusCode() >= 400 && eae.getStatusCode() < 500) {
      throw eae;
    }
  }
}
