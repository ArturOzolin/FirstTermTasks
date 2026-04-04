package com.mipt.arturozolin.controller;

import com.mipt.arturozolin.dto.AttachmentResponseDto;
import com.mipt.arturozolin.model.TaskAttachment;
import com.mipt.arturozolin.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Attachment Management", description = "Операции для работы с вложениями задач")
public class AttachmentController {

  private final AttachmentService attachmentService;

  public AttachmentController(AttachmentService attachmentService) {
    this.attachmentService = attachmentService;
  }

  @Operation(summary = "Загрузить файл", description = "Прикрепляет файл к конкретной задаче")
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Файл успешно загружен"),
          @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  @PostMapping(value = "/tasks/{taskId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<AttachmentResponseDto> upload(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) {
    TaskAttachment attachment = attachmentService.storeAttachment(taskId, file);
    return ResponseEntity.status(201).body(new AttachmentResponseDto(attachment.getId(), attachment.getFileName(), attachment.getSize(), attachment.getUploadedAt()));
  }

  @Operation(summary = "Скачать файл", description = "Загружает прикрепленный файл по его ID")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Файл успешно передан"),
          @ApiResponse(responseCode = "404", description = "Вложение не найдено")
  })
  @GetMapping("/attachments/{id}")
  public ResponseEntity<Resource> download(@PathVariable Long id) {
    Resource resource = attachmentService.loadAsResource(id);
    TaskAttachment attachment = attachmentService.getAttachment(id);
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
  }

  @Operation(summary = "Удалить файл", description = "Удаляет прикрепленный файл")
  @ApiResponse(responseCode = "204", description = "Файл успешно удален")
  @DeleteMapping("/attachments/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    attachmentService.deleteAttachment(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Получить список вложений задачи", description = "Возвращает метаданные всех файлов, прикрепленных к задаче")
  @ApiResponse(responseCode = "200", description = "Список вложений успешно получен")
  @GetMapping("/tasks/{taskId}/attachments")
  public ResponseEntity<List<AttachmentResponseDto>> getAttachments(@PathVariable Long taskId) {
    List<AttachmentResponseDto> response = attachmentService.getAttachmentsByTaskId(taskId).stream()
            .map(a -> new AttachmentResponseDto(a.getId(), a.getFileName(), a.getSize(), a.getUploadedAt()))
            .collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }
}