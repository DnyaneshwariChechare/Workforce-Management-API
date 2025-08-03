package com.railse.hiring.workforcemgmt.service;

import com.railse.hiring.workforcemgmt.dto.*;

import java.util.List;

public interface TaskManagementService {
   List<TaskManagementDto> createTasks(TaskCreateRequest request);
   List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
   String assignByReference(AssignByReferenceRequest request);
   List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
   TaskManagementDto findTaskById(Long id);
   TaskManagementDto updateTaskPriority(Long id, UpdateTaskPriorityRequest request);
   List<TaskManagementDto> findTasksByPriority(String priority);
   TaskManagementDto addCommentToTask(Long taskId, AddCommentRequest request);
   TaskManagementDto getTaskDetails(Long id);
}
