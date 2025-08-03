package com.railse.hiring.workforcemgmt.model.enums;

import com.railse.hiring.workforcemgmt.common.model.enums.ReferenceType;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Task {
   ASSIGN_CUSTOMER_TO_SALES_PERSON(List.of(ReferenceType.ENTITY), "Assign customer to Sales person"),
   CREATE_INVOICE(List.of(ReferenceType.ORDER), "Create Invoice"),
   ARRANGE_PICKUP(List.of(ReferenceType.ORDER), "Arrange Pickup"),
   COLLECT_PAYMENT(List.of(ReferenceType.ORDER), "Collect Payment");

   private final List<ReferenceType> applicableReferenceTypes;
   private final String view;

   Task(List<ReferenceType> applicableReferenceTypes, String view) {
       this.applicableReferenceTypes = applicableReferenceTypes;
       this.view = view;
   }

   public static List<Task> getTasksByReferenceType(ReferenceType referenceType) {
       return Arrays.stream(Task.values())
               .filter(task -> task.getApplicableReferenceTypes().contains(referenceType))
               .collect(Collectors.toList());
   }
}