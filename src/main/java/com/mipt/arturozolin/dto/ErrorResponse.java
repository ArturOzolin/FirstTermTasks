package com.mipt.arturozolin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

@Schema(description = "Объект ответа при возникновении ошибки")
public class ErrorResponse {
  @Schema(description = "Метка времени ошибки")
  private Instant timestamp;
  @Schema(description = "HTTP статус код")
  private int status;
  @Schema(description = "Краткое описание ошибки")
  private String error;
  @Schema(description = "Подробное сообщение об ошибке")
  private String message;
  @Schema(description = "Путь по которому произошла ошибка")
  private String path;
  @Schema(description = "Детальные ошибки")
  private Map<String, Object> details;

  public ErrorResponse(int status, String error, String message, String path, Map<String, Object> details) {
    this.timestamp = Instant.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
    this.details = details;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public String getPath() {
    return path;
  }

  public Map<String, Object> getDetails() {
    return details;
  }
}