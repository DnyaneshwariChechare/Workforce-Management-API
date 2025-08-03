
package com.railse.hiring.workforcemgmt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railse.hiring.workforcemgmt.common.model.enums.ReferenceType;
import com.railse.hiring.workforcemgmt.common.exception.ResourceNotFoundException;
import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.model.enums.Priority;
import com.railse.hiring.workforcemgmt.model.enums.Task;
import com.railse.hiring.workforcemgmt.model.enums.TaskStatus;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskManagementService taskManagementService;

    @Test
    void getTaskById_shouldReturnTask_whenFound() throws Exception {
        // Arrange
        TaskManagementDto taskDto = new TaskManagementDto();
        taskDto.setId(1L);
        taskDto.setReferenceId(101L);
        taskDto.setTask(Task.CREATE_INVOICE);
        taskDto.setStatus(TaskStatus.ASSIGNED);
        when(taskManagementService.findTaskById(1L)).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(get("/task-mgmt/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.reference_id").value(101L))
                .andExpect(jsonPath("$.data.task").value("CREATE_INVOICE"));
    }

    @Test
    void getTaskById_shouldReturnNotFound_whenDoesNotExist() throws Exception {
        // Arrange
        when(taskManagementService.findTaskById(99L)).thenThrow(new ResourceNotFoundException("Task not found with id: 99"));

        // Act & Assert
        mockMvc.perform(get("/task-mgmt/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status.code").value(404))
                .andExpect(jsonPath("$.status.message").value("Task not found with id: 99"));
    }

    @Test
    void createTasks_shouldReturnCreatedTasks() throws Exception {
        // Arrange
        TaskCreateRequest.RequestItem requestItem = new TaskCreateRequest.RequestItem();
        requestItem.setReferenceId(101L);
        requestItem.setReferenceType(ReferenceType.ORDER);
        requestItem.setTask(Task.CREATE_INVOICE);
        requestItem.setAssigneeId(1L);
        requestItem.setPriority(Priority.HIGH);

        TaskCreateRequest createRequest = new TaskCreateRequest();
        createRequest.setRequests(Collections.singletonList(requestItem));

        TaskManagementDto createdDto = new TaskManagementDto();
        createdDto.setId(1L);
        createdDto.setReferenceId(101L);
        createdDto.setStatus(TaskStatus.ASSIGNED);

        when(taskManagementService.createTasks(any(TaskCreateRequest.class))).thenReturn(Collections.singletonList(createdDto));

        // Act & Assert
        mockMvc.perform(post("/task-mgmt/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].status").value("ASSIGNED"));
    }

    @Test
    void updateTasks_shouldReturnUpdatedTasks() throws Exception {
        // Arrange
        UpdateTaskRequest.RequestItem requestItem = new UpdateTaskRequest.RequestItem();
        requestItem.setTaskId(1L);
        requestItem.setTaskStatus(TaskStatus.COMPLETED);
        requestItem.setDescription("Task is done");

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setRequests(Collections.singletonList(requestItem));

        TaskManagementDto updatedDto = new TaskManagementDto();
        updatedDto.setId(1L);
        updatedDto.setStatus(TaskStatus.COMPLETED);
        updatedDto.setDescription("Task is done");

        when(taskManagementService.updateTasks(any(UpdateTaskRequest.class))).thenReturn(Collections.singletonList(updatedDto));

        // Act & Assert
        mockMvc.perform(post("/task-mgmt/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$.data[0].description").value("Task is done"));
    }

    @Test
    void assignByReference_shouldReturnSuccessMessage() throws Exception {
        // Arrange
        AssignByReferenceRequest assignRequest = new AssignByReferenceRequest();
        assignRequest.setReferenceId(201L);
        assignRequest.setReferenceType(ReferenceType.ENTITY);
        assignRequest.setAssigneeId(5L);

        String successMessage = "Tasks reassigned successfully for reference 201";
        when(taskManagementService.assignByReference(any(AssignByReferenceRequest.class))).thenReturn(successMessage);

        // Act & Assert
        mockMvc.perform(post("/task-mgmt/assign-by-ref")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data").value(successMessage));
    }

    @Test
    void fetchByDate_shouldReturnFilteredTasks() throws Exception {
        // Arrange
        TaskFetchByDateRequest fetchRequest = new TaskFetchByDateRequest();
        fetchRequest.setAssigneeIds(Collections.singletonList(1L));
        fetchRequest.setStartDate(1735689000000L);
        fetchRequest.setEndDate(1735689900000L);

        TaskManagementDto taskDto = new TaskManagementDto();
        taskDto.setId(1L);
        taskDto.setAssigneeId(1L);
        taskDto.setTaskDeadlineTime(1735689600000L);

        when(taskManagementService.fetchTasksByDate(any(TaskFetchByDateRequest.class))).thenReturn(List.of(taskDto));

        // Act & Assert
        mockMvc.perform(post("/task-mgmt/fetch-by-date/v2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fetchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].assignee_id").value(1L));
    }

    @Test
    void updateTaskPriority_shouldReturnUpdatedTask() throws Exception {
        // Arrange
        Long taskId = 1L;
        UpdateTaskPriorityRequest request = new UpdateTaskPriorityRequest();
        request.setPriority(Priority.HIGH);

        TaskManagementDto updatedDto = new TaskManagementDto();
        updatedDto.setId(taskId);
        updatedDto.setPriority(Priority.HIGH);

        when(taskManagementService.updateTaskPriority(any(Long.class), any(UpdateTaskPriorityRequest.class)))
                .thenReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(put("/task-mgmt/" + taskId + "/priority")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data.id").value(taskId))
                .andExpect(jsonPath("$.data.priority").value("HIGH"));
    }

    @Test
    void getTasksByPriority_shouldReturnFilteredTasks() throws Exception {
        // Arrange
        String priority = "HIGH";
        TaskManagementDto taskDto = new TaskManagementDto();
        taskDto.setId(1L);
        taskDto.setPriority(Priority.HIGH);

        when(taskManagementService.findTasksByPriority(priority)).thenReturn(List.of(taskDto));

        // Act & Assert
        mockMvc.perform(get("/task-mgmt/priority/" + priority))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].priority").value("HIGH"));
    }

    @Test
    void addCommentToTask_shouldReturnUpdatedTask() throws Exception {
        // Arrange
        Long taskId = 1L;
        AddCommentRequest request = new AddCommentRequest();
        request.setCommentText("Test comment");
        request.setUserId(1L);

        TaskManagementDto updatedDto = new TaskManagementDto();
        updatedDto.setId(taskId);
        updatedDto.setComments(Collections.singletonList(new TaskCommentDto())); // Mock a comment being added

        when(taskManagementService.addCommentToTask(any(Long.class), any(AddCommentRequest.class)))
                .thenReturn(updatedDto);

        // Act & Assert
        mockMvc.perform(post("/task-mgmt/" + taskId + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data.id").value(taskId))
                .andExpect(jsonPath("$.data.comments").isArray());
    }

    @Test
    void getTaskDetails_shouldReturnTaskWithDetails() throws Exception {
        // Arrange
        Long taskId = 1L;
        TaskManagementDto taskDto = new TaskManagementDto();
        taskDto.setId(taskId);
        taskDto.setActivities(Collections.singletonList(new TaskActivityDto()));
        taskDto.setComments(Collections.singletonList(new TaskCommentDto()));

        when(taskManagementService.getTaskDetails(taskId)).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(get("/task-mgmt/" + taskId + "/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200))
                .andExpect(jsonPath("$.data.id").value(taskId))
                .andExpect(jsonPath("$.data.activities").isArray())
                .andExpect(jsonPath("$.data.comments").isArray());
    }
}
