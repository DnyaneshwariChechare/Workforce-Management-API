INSERT INTO task_management (reference_id, reference_type, task, description, status, assignee_id, task_deadline_time, priority) VALUES
(101, 'ORDER', 'CREATE_INVOICE', 'Create invoice for order 101', 'ASSIGNED', 1, 1735689600000, 'HIGH'),
(101, 'ORDER', 'ARRANGE_PICKUP', 'Arrange pickup for order 101', 'COMPLETED', 1, 1735689600000, 'HIGH'),
(102, 'ORDER', 'CREATE_INVOICE', 'Create invoice for order 102', 'ASSIGNED', 2, 1735776000000, 'MEDIUM'),
(201, 'ENTITY', 'ASSIGN_CUSTOMER_TO_SALES_PERSON', 'Assign customer 201 to sales person', 'ASSIGNED', 2, 1735862400000, 'LOW'),
(201, 'ENTITY', 'ASSIGN_CUSTOMER_TO_SALES_PERSON', 'Assign customer 201 to sales person', 'ASSIGNED', 3, 1735862400000, 'LOW'),
(103, 'ORDER', 'COLLECT_PAYMENT', 'Collect payment for order 103', 'CANCELLED', 1, 1735948800000, 'MEDIUM');
