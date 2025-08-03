package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.common.model.enums.ReferenceType;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskManagement, Long> {
    List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);

    List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);
    List<TaskManagement> findByPriority(com.railse.hiring.workforcemgmt.model.enums.Priority priority);
}