package com.mipt.arturozolin.service;

import com.mipt.arturozolin.model.TaskAttachment;
import com.mipt.arturozolin.repository.TaskAttachmentRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {

  private final TaskAttachmentRepository attachmentRepository;
  private final Path uploadDir = Paths.get("uploads");

  public AttachmentService(TaskAttachmentRepository attachmentRepository) {
    this.attachmentRepository = attachmentRepository;
    try { Files.createDirectories(uploadDir); } catch (Exception e) { throw new RuntimeException("Could not init folder"); }
  }

  public TaskAttachment storeAttachment(String taskId, MultipartFile file) {
    try {
      String originalFileName = file.getOriginalFilename();
      String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
      Path targetLocation = uploadDir.resolve(storedFileName);

      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
      }

      TaskAttachment attachment = new TaskAttachment();
      attachment.setTaskId(taskId);
      attachment.setFileName(originalFileName);
      attachment.setStoredFileName(storedFileName);
      attachment.setContentType(file.getContentType());
      attachment.setSize(file.getSize());

      return attachmentRepository.save(attachment);
    } catch (Exception e) {
      throw new RuntimeException("Failed to store file", e);
    }
  }

  public TaskAttachment getAttachment(Long attachmentId) {
    return attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("Attachment not found"));
  }

  public Resource loadAsResource(Long attachmentId) {
    try {
      TaskAttachment attachment = getAttachment(attachmentId);
      Path file = uploadDir.resolve(attachment.getStoredFileName());
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read file");
      }
    } catch (Exception e) {
      throw new RuntimeException("Could not read file", e);
    }
  }

  public void deleteAttachment(Long attachmentId) {
    TaskAttachment attachment = getAttachment(attachmentId);
    try {
      Files.deleteIfExists(uploadDir.resolve(attachment.getStoredFileName()));
      attachmentRepository.deleteById(attachmentId);
    } catch (Exception e) {
      throw new RuntimeException("Could not delete file", e);
    }
  }

  public List<TaskAttachment> getAttachmentsByTaskId(String taskId) {
    return attachmentRepository.findByTaskId(taskId);
  }
}