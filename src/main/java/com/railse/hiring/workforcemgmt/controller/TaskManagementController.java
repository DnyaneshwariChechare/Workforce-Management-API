package com.railse.hiring.workforcemgmt.controller;

import com.railse.hiring.workforcemgmt.common.model.response.Response;
import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {

   private final TaskManagementService taskManagementService;

   public TaskManagementController(TaskManagementService taskManagementService) {
       this.taskManagementService = taskManagementService;
   }

   @GetMapping("/{id}")
   public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {
       return new Response<>(taskManagementService.findTaskById(id));
   }

   @PostMapping("/create")
   public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
       return new Response<>(taskManagementService.createTasks(request));
   }

   @PostMapping("/update")
   public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
       return new Response<>(taskManagementService.updateTasks(request));
   }

   @PostMapping("/assign-by-ref")
   public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
       return new Response<>(taskManagementService.assignByReference(request));
   }

      @PostMapping("/fetch-by-date/v2")
   public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
       return new Response<>(taskManagementService.fetchTasksByDate(request));
   }

   @PutMapping("/{id}/priority")
   public Response<TaskManagementDto> updateTaskPriority(@PathVariable Long id, @RequestBody UpdateTaskPriorityRequest request) {
       return new Response<>(taskManagementService.updateTaskPriority(id, request));
   }

   @GetMapping("/priority/{priority}")
   public Response<List<TaskManagementDto>> getTasksByPriority(@PathVariable String priority) {
       return new Response<>(taskManagementService.findTasksByPriority(priority));
   }

   @PostMapping("/{id}/comment")
   public Response<TaskManagementDto> addCommentToTask(@PathVariable Long id, @RequestBody AddCommentRequest request) {
       return new Response<>(taskManagementService.addCommentToTask(id, request));
   }

   @GetMapping("/{id}/details")
   public Response<TaskManagementDto> getTaskDetails(@PathVariable Long id) {
       return new Response<>(taskManagementService.getTaskDetails(id));
   }
}
