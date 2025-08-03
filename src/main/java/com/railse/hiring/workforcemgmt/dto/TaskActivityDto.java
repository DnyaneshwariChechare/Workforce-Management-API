package com.railse.hiring.workforcemgmt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.hiring.workforcemgmt.model.enums.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskActivityDto {
    private Long id;
    private Long taskId;
    private ActivityType activityType;
    private String description;
    private Long userId;
    private LocalDateTime timestamp;
}
