package com.mipt.arturozolin.mapper;

import com.mipt.arturozolin.dto.TaskCreateDto;
import com.mipt.arturozolin.dto.TaskResponseDto;
import com.mipt.arturozolin.dto.TaskUpdateDto;
import com.mipt.arturozolin.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskCreateDto dto);

    Task updateEntity(TaskUpdateDto dto, @MappingTarget Task task);

    TaskResponseDto toResponseDto(Task task);
}