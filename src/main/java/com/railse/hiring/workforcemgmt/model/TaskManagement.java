
package com.railse.hiring.workforcemgmt.model;

import com.railse.hiring.workforcemgmt.common.model.enums.ReferenceType;
import com.railse.hiring.workforcemgmt.model.enums.Priority;
import com.railse.hiring.workforcemgmt.model.enums.Task;
import com.railse.hiring.workforcemgmt.model.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "task_management")
public class TaskManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long referenceId;
    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;
    @Enumerated(EnumType.STRING)
    private Task task;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private Long assigneeId;
    private Long taskDeadlineTime;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskActivity> activities;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskComment> comments;
}
