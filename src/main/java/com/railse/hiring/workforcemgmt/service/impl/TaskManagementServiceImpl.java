package com.railse.hiring.workforcemgmt.service.impl;

import com.railse.hiring.workforcemgmt.common.exception.ResourceNotFoundException;
import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.mapper.ITaskManagementMapper;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import com.railse.hiring.workforcemgmt.model.enums.Task;
import com.railse.hiring.workforcemgmt.model.enums.TaskStatus;
import com.railse.hiring.workforcemgmt.model.enums.Priority;
import com.railse.hiring.workforcemgmt.model.TaskActivity;
import com.railse.hiring.workforcemgmt.model.TaskComment;
import com.railse.hiring.workforcemgmt.model.enums.ActivityType;
import com.railse.hiring.workforcemgmt.repository.TaskActivityRepository;
import com.railse.hiring.workforcemgmt.repository.TaskCommentRepository;
import com.railse.hiring.workforcemgmt.repository.TaskRepository;

import java.time.LocalDateTime;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

   private final TaskRepository taskRepository;
   private final TaskActivityRepository activityRepository;
   private final TaskCommentRepository commentRepository;
   private final ITaskManagementMapper taskMapper;

   public TaskManagementServiceImpl(TaskRepository taskRepository, TaskActivityRepository activityRepository, TaskCommentRepository commentRepository, ITaskManagementMapper taskMapper) {
       this.taskRepository = taskRepository;
       this.activityRepository = activityRepository;
       this.commentRepository = commentRepository;
       this.taskMapper = taskMapper;
   }

   @Override
   public TaskManagementDto findTaskById(Long id) {
       TaskManagement task = taskRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
       return taskMapper.modelToDto(task);
   }

   @Override
   public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
       List<TaskManagement> createdTasks = new ArrayList<>();
       for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
           TaskManagement newTask = new TaskManagement();
           newTask.setReferenceId(item.getReferenceId());
           newTask.setReferenceType(item.getReferenceType());
           newTask.setTask(item.getTask());
           newTask.setAssigneeId(item.getAssigneeId());
           newTask.setPriority(item.getPriority());
           newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
           newTask.setStatus(TaskStatus.ASSIGNED);
           newTask.setDescription("New task created.");
           createdTasks.add(taskRepository.save(newTask));
       }
       return taskMapper.modelListToDtoList(createdTasks);
   }

   @Override
   public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
       List<TaskManagement> updatedTasks = new ArrayList<>();
       for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
           TaskManagement task = taskRepository.findById(item.getTaskId())
                   .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));

           if (item.getTaskStatus() != null) {
               task.setStatus(item.getTaskStatus());
           }
           if (item.getDescription() != null) {
               task.setDescription(item.getDescription());
           }
           updatedTasks.add(taskRepository.save(task));
       }
       return taskMapper.modelListToDtoList(updatedTasks);
   }

   @Override
   public String assignByReference(AssignByReferenceRequest request) {
       List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
       List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

       for (Task taskType : applicableTasks) {
           List<TaskManagement> tasksOfType = existingTasks.stream()
                   .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                   .collect(Collectors.toList());

           if (!tasksOfType.isEmpty()) {
               // ✅ FIXED BUG: Cancel previous assignments before reassigning
               for (TaskManagement taskToUpdate : tasksOfType) {
                   taskToUpdate.setStatus(TaskStatus.CANCELLED);
                   taskRepository.save(taskToUpdate);
               }
           }

           TaskManagement newTask = new TaskManagement();
           newTask.setReferenceId(request.getReferenceId());
           newTask.setReferenceType(request.getReferenceType());
           newTask.setTask(taskType);
           newTask.setAssigneeId(request.getAssigneeId());
           newTask.setStatus(TaskStatus.ASSIGNED);
           newTask.setDescription("Task reassigned to new user.");
           taskRepository.save(newTask);
       }
       return "Tasks reassigned successfully for reference " + request.getReferenceId();
   }

   @Override
   public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
       List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

       List<TaskManagement> filteredTasks = tasks.stream()
               .filter(task ->
                       task.getStatus() != TaskStatus.CANCELLED &&
                       (
                           (task.getTaskDeadlineTime() >= request.getStartDate() &&
                            task.getTaskDeadlineTime() <= request.getEndDate()) ||
                           (task.getTaskDeadlineTime() < request.getStartDate() &&
                            task.getStatus() == TaskStatus.ASSIGNED)
                       )
               )
               .collect(Collectors.toList());

       return taskMapper.modelListToDtoList(filteredTasks);
   }

   @Override
   public TaskManagementDto updateTaskPriority(Long id, UpdateTaskPriorityRequest request) {
       TaskManagement task = taskRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
       Priority oldPriority = task.getPriority();
       task.setPriority(request.getPriority());
       TaskManagement updatedTask = taskRepository.save(task);

       // Log activity
       TaskActivity activity = new TaskActivity();
       activity.setTask(updatedTask);
       activity.setActivityType(ActivityType.TASK_PRIORITY_CHANGED);
       activity.setDescription(String.format("Task priority changed from %s to %s", oldPriority, request.getPriority()));
       activity.setTimestamp(LocalDateTime.now());
       // Assuming a default user for now, or get from security context
       activity.setUserId(1L);
       activityRepository.save(activity);

       return taskMapper.modelToDto(updatedTask);
   }

   @Override
   public List<TaskManagementDto> findTasksByPriority(String priority) {
       List<TaskManagement> tasks = taskRepository.findByPriority(com.railse.hiring.workforcemgmt.model.enums.Priority.valueOf(priority.toUpperCase()));
       return taskMapper.modelListToDtoList(tasks);
   }

   @Override
   public TaskManagementDto addCommentToTask(Long taskId, AddCommentRequest request) {
       TaskManagement task = taskRepository.findById(taskId)
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

       TaskComment comment = new TaskComment();
       comment.setTask(task);
       comment.setCommentText(request.getCommentText());
       comment.setUserId(request.getUserId());
       comment.setTimestamp(LocalDateTime.now());
       commentRepository.save(comment);

       // Log activity
       TaskActivity activity = new TaskActivity();
       activity.setTask(task);
       activity.setActivityType(ActivityType.COMMENT_ADDED);
       activity.setDescription(String.format("Comment added by user %d: \"%s\"", request.getUserId(), request.getCommentText()));
       activity.setTimestamp(LocalDateTime.now());
       activity.setUserId(request.getUserId());
       activityRepository.save(activity);

       return taskMapper.modelToDto(task);
   }

   @Override
   public TaskManagementDto getTaskDetails(Long id) {
       TaskManagement task = taskRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
       TaskManagementDto dto = taskMapper.modelToDto(task);
       dto.setActivities(taskMapper.activityListToDtoList(activityRepository.findByTaskIdOrderByTimestampAsc(id)));
       dto.setComments(taskMapper.commentListToDtoList(commentRepository.findByTaskIdOrderByTimestampAsc(id)));
       return dto;
   }
}
