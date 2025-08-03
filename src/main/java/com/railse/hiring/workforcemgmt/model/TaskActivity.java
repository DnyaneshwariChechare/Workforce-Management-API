package com.railse.hiring.workforcemgmt.model;

import com.railse.hiring.workforcemgmt.model.enums.ActivityType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "task_activity")
public class TaskActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskManagement task;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    private String description;

    private Long userId;

    private LocalDateTime timestamp;
}
