package com.mipt.arturozolin.exception;

import com.mipt.arturozolin.dto.ErrorResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
    Map<String, Object> details = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      details.put(error.getField(), error.getDefaultMessage());
    }
    ErrorResponse error = new ErrorResponse(400, "Bad Request", "Validation failed", request.getRequestURI(), details);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(TaskNotFoundException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(404, "Not Found", ex.getMessage(), request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler({
          ConstraintViolationException.class,
          MissingServletRequestParameterException.class,
          HttpMessageNotReadableException.class
  })
  public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(400, "Bad Request", ex.getMessage(), request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandler(NoHandlerFoundException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(404, "Not Found", "Endpoint not found", request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(401, "Unauthorized", "Invalid username or password", request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex, HttpServletRequest request) {
    ErrorResponse error = new ErrorResponse(401, "Unauthorized", ex.getMessage(), request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(RequestNotPermitted.class)
  public ResponseEntity<ErrorResponse> handleRateLimit(RequestNotPermitted ex, HttpServletRequest request) {
    log.warn("Rate limit exceeded: {}", ex.getMessage());
    ErrorResponse error = new ErrorResponse(429, "Too Many Requests", "Rate limit exceeded for external API", request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .header("Retry-After", "1")
            .body(error);
  }

  @ExceptionHandler(CallNotPermittedException.class)
  public ResponseEntity<ErrorResponse> handleCircuitOpen(CallNotPermittedException ex, HttpServletRequest request) {
    log.warn("Circuit breaker open: {}", ex.getMessage());
    ErrorResponse error = new ErrorResponse(503, "Service Unavailable", "External service circuit is open", request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
  }

  @ExceptionHandler(GatewayUnavailableException.class)
  public ResponseEntity<ErrorResponse> handleGatewayUnavailable(GatewayUnavailableException ex, HttpServletRequest request) {
    log.warn("Gateway fallback triggered: {}", ex.getMessage());
    ErrorResponse error = new ErrorResponse(503, "Service Unavailable", ex.getMessage(), request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
  }

  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<ErrorResponse> handleExternalApi(ExternalApiException ex, HttpServletRequest request) {
    int upstream = ex.getStatusCode();
    HttpStatus status = upstream >= 500 ? HttpStatus.BAD_GATEWAY : HttpStatus.valueOf(upstream);
    ErrorResponse error = new ErrorResponse(status.value(), status.getReasonPhrase(), ex.getMessage(), request.getRequestURI(), null);
    return ResponseEntity.status(status).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception", ex);
    ErrorResponse error = new ErrorResponse(500, "Internal Server Error", "An unexpected error occurred", request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}