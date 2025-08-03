package com.railse.hiring.workforcemgmt.dto;

import lombok.Data;

@Data
public class AddCommentRequest {
    private String commentText;
    private Long userId;
}
