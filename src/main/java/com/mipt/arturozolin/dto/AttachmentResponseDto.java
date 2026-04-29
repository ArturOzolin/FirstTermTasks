package com.mipt.arturozolin.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Информация о вложении")
public class AttachmentResponseDto {
  @Schema(description = "ID вложения")
  private Long id;
  @Schema(description = "Имя файла")
  private String fileName;
  @Schema(description = "Размер файла в байтах")
  private long size;
  @Schema(description = "Дата и время загрузки")
  private LocalDateTime uploadedAt;

  public AttachmentResponseDto(Long id, String fileName, long size, LocalDateTime uploadedAt) {
    this.id = id;
    this.fileName = fileName;
    this.size = size;
    this.uploadedAt = uploadedAt;
  }

  public Long getId() {
    return id;
  }

  public String getFileName() {
    return fileName;
  }

  public long getSize() {
    return size;
  }

  public LocalDateTime getUploadedAt() {
    return uploadedAt;
  }
}