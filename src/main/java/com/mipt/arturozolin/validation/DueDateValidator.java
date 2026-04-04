package com.mipt.arturozolin.validation;

import com.mipt.arturozolin.dto.TaskUpdateDto;
import com.mipt.arturozolin.model.Task;
import com.mipt.arturozolin.repository.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

public class DueDateValidator implements ConstraintValidator<DueDateNotBeforeCreation, TaskUpdateDto> {

    private final TaskRepository taskRepository;
    private final HttpServletRequest request;

    public DueDateValidator(TaskRepository taskRepository, HttpServletRequest request) {
        this.taskRepository = taskRepository;
        this.request = request;
    }

    @Override
    public boolean isValid(TaskUpdateDto dto, ConstraintValidatorContext context) {
        if (dto.getDueDate() == null) return true;
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables == null || !pathVariables.containsKey("id")) return true;
        String idStr = pathVariables.get("id");
        try {
            Long id = Long.parseLong(idStr);
            Task task = taskRepository.findById(id).orElse(null);
            if (task == null || task.getCreatedAt() == null) return true;
            return !dto.getDueDate().isBefore(task.getCreatedAt().toLocalDate());

        } catch (NumberFormatException e) {
            return true;
        }
    }
}