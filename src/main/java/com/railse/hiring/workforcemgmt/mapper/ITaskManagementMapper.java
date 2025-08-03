package com.railse.hiring.workforcemgmt.mapper;

import com.railse.hiring.workforcemgmt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.railse.hiring.workforcemgmt.dto.TaskActivityDto;
import com.railse.hiring.workforcemgmt.dto.TaskCommentDto;
import com.railse.hiring.workforcemgmt.model.TaskActivity;
import com.railse.hiring.workforcemgmt.model.TaskComment;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ITaskManagementMapper {
   

   TaskManagementDto modelToDto(TaskManagement model);

   TaskManagement dtoToModel(TaskManagementDto dto);

   List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models);

   TaskActivityDto activityToDto(TaskActivity activity);
   List<TaskActivityDto> activityListToDtoList(List<TaskActivity> activities);

   TaskCommentDto commentToDto(TaskComment comment);
   List<TaskCommentDto> commentListToDtoList(List<TaskComment> comments);
}
