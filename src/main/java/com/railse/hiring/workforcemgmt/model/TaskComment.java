package com.railse.hiring.workforcemgmt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "task_comment")
public class TaskComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskManagement task;

    private String commentText;

    private Long userId;

    private LocalDateTime timestamp;
}
