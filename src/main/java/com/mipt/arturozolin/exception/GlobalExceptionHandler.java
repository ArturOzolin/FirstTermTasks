package com.mipt.arturozolin.exception;

import com.mipt.arturozolin.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
    log.error("Unhandled exception", ex);
    ErrorResponse error = new ErrorResponse(500, "Internal Server Error", "An unexpected error occurred", request.getRequestURI(), null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}