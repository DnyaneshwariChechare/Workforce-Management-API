# Workforce Management API

[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen)](https://github.com/your-username/your-repo/actions)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A robust Spring Boot application designed to help managers efficiently create, assign, track, and prioritize tasks for their staff. This API provides a comprehensive backend solution for workforce management, focusing on task lifecycle, communication, and historical tracking.

## Table of Contents

- [Workforce Management API](#workforce-management-api)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Technologies Used](#technologies-used)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Cloning the Repository](#cloning-the-repository)
    - [Running the Application Locally](#running-the-application-locally)
    - [Running Tests](#running-tests)
  - [API Endpoints](#api-endpoints)
    - [Task Management](#task-management)
    - [Task Priority](#task-priority)
    - [Task Comments & Activity History](#task-comments--activity-history)
  - [Demo Data & Testing with Postman/Insomnia](#demo-data--testing-with-postmaninsomnia)
    - [1. Create Tasks (POST)](#1-create-tasks-post)
    - [2. Get Task by ID (GET)](#2-get-task-by-id-get)
    - [3. Update Tasks (POST)](#3-update-tasks-post)
    - [4. Assign by Reference (POST)](#4-assign-by-reference-post)
    - [5. Fetch Tasks by Date (POST)](#5-fetch-tasks-by-date-post)
    - [6. Update Task Priority (PUT)](#6-update-task-priority-put)
    - [7. Get Tasks by Priority (GET)](#7-get-tasks-by-priority-get)
    - [8. Add Comment to Task (POST)](#8-add-comment-to-task-post)
    - [9. Get Task Details (GET)](#9-get-task-details-get)
  - [Project Structure](#project-structure)
  - [Bugs Solved](#bugs-solved)
  - [Future Enhancements](#future-enhancements)
  - [License](#license)

## Features

*   **Task Creation & Management:** Create, update, and retrieve tasks with details like reference ID, type, assignee, and deadline.
*   **Task Reassignment:** Efficiently reassign tasks, ensuring old tasks are properly cancelled to avoid duplicates.
*   **Smart Daily Task View:** Fetch tasks for a specific date range, including active tasks that started before the range but are still open.
*   **Task Priority Management:** Assign and update task priorities (HIGH, MEDIUM, LOW) and filter tasks based on priority.
*   **Task Comments:** Allow users to add free-text comments to any task.
*   **Activity History:** Automatically log key events for each task (e.g., creation, status changes, priority changes, comments added).
*   **Detailed Task View:** Retrieve a single task's details, including its complete activity history and all associated comments, sorted chronologically.
*   **In-Memory Database:** Utilizes H2 for lightweight, in-memory data storage, ideal for development and testing.

## Technologies Used

*   **Language:** Java 17
*   **Framework:** Spring Boot 3.3.0
*   **Build Tool:** Gradle
*   **Database:** H2 Database (in-memory)
*   **Object Mapping:** MapStruct (for DTO to Entity and vice-versa conversion)
*   **Boilerplate Reduction:** Lombok
*   **Testing:** JUnit 5, Mockito, Spring Boot Test, MockMvc

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

*   Java Development Kit (JDK) 17 or higher
*   Gradle (usually bundled with Spring Boot projects, so `gradlew` will work)
*   A code editor or IDE (e.g., IntelliJ IDEA, VS Code, Eclipse)
*   An API testing tool (e.g., Postman, Insomnia, curl)

### Cloning the Repository

```bash
git clone https://github.com/DnyaneshwariChechare/Workforce-Management-API.git
cd your-repo
```
### Running the Application Locally

You can run the Spring Boot application using Gradle:

```bash
./gradlew bootRun
```
(On Windows, use `gradlew.bat bootRun`)

The application will start on `http://localhost:8080` by default.

### Running Tests

To run all unit and integration tests:

```bash
./gradlew test
```
(On Windows, use `gradlew.bat test`)

## API Endpoints

The API is accessible at `http://localhost:8080/task-mgmt`.

### Task Management

*   **`GET /task-mgmt/{id}`**
    *   **Description:** Retrieve a single task by its unique ID.
    *   **Response:** `Response<TaskManagementDto>`
*   **`POST /task-mgmt/create`**
    *   **Description:** Create one or more new tasks.
    *   **Request Body:** `List<TaskCreateRequest>`
    *   **Response:** `Response<List<TaskManagementDto>>`
*   **`POST /task-mgmt/update`**
    *   **Description:** Update details of one or more existing tasks.
    *   **Request Body:** `List<UpdateTaskRequest>`
    *   **Response:** `Response<List<TaskManagementDto>>`
*   **`POST /task-mgmt/assign-by-ref`**
    *   **Description:** Reassign tasks associated with a specific reference to a new assignee.
    *   **Request Body:** `AssignByReferenceRequest`
    *   **Response:** `Response<String>` (success message)
*   **`POST /task-mgmt/fetch-by-date/v2`**
    *   **Description:** Fetch tasks for given assignees within a specified date range, including active tasks that started before the range but are still open.
    *   **Request Body:** `TaskFetchByDateRequest`
    *   **Response:** `Response<List<TaskManagementDto>>`

### Task Priority

*   **`PUT /task-mgmt/{id}/priority`**
    *   **Description:** Update the priority of a specific task.
    *   **Request Body:** `UpdateTaskPriorityRequest` (e.g., `{"priority": "HIGH"}`)
    *   **Response:** `Response<TaskManagementDto>`
*   **`GET /task-mgmt/priority/{priority}`**
    *   **Description:** Retrieve all tasks filtered by a specific priority (e.g., `HIGH`, `MEDIUM`, `LOW`).
    *   **Response:** `Response<List<TaskManagementDto>>`

### Task Comments & Activity History

*   **`POST /task-mgmt/{id}/comment`**
    *   **Description:** Add a new comment to a specific task.
    *   **Request Body:** `AddCommentRequest` (e.g., `{"commentText": "This is a new comment.", "userId": 1}`)
    *   **Response:** `Response<TaskManagementDto>` (the updated task with new comment)
*   **`GET /task-mgmt/{id}/details`**
    *   **Description:** Retrieve a task's full details, including its complete activity history and all associated comments, sorted chronologically.
    *   **Response:** `Response<TaskManagementDto>` (includes `activities` and `comments` lists)

## Demo Data & Testing with Postman/Insomnia

Once the application is running (`./gradlew bootRun`), you can use your preferred API client to test the endpoints.

### 1. Create Tasks (POST)
**URL:** `http://localhost:8080/task-mgmt/create`
**Body (JSON):**
```json
[
  {
    "referenceId": 101,
    "referenceType": "ENTITY",
    "task": "ASSIGN_CUSTOMER_TO_SALES_PERSON",
    "assigneeId": 1,
    "priority": "HIGH",
    "taskDeadlineTime": 1754208000000,
    "description": "Assign customer ABC to Salesperson John Doe"
  },
  {
    "referenceId": 201,
    "referenceType": "ORDER",
    "task": "CREATE_INVOICE",
    "assigneeId": 2,
    "priority": "MEDIUM",
    "taskDeadlineTime": 1754294400000,
    "description": "Create invoice for Order #123"
  }
]
```

### 2. Get Task by ID (GET)
**URL:** `http://localhost:8080/task-mgmt/1` (Replace `1` with an actual task ID)

### 3. Update Tasks (POST)
**URL:** `http://localhost:8080/task-mgmt/update`
**Body (JSON):**
```json
[
  {
    "taskId": 1,
    "taskStatus": "COMPLETED",
    "description": "Customer assigned and task completed."
  }
]
```

### 4. Assign by Reference (POST)
**URL:** `http://localhost:8080/task-mgmt/assign-by-ref`
**Body (JSON):**
```json
{
  "referenceId": 101,
  "referenceType": "ENTITY",
  "assigneeId": 3
}
```

### 5. Fetch Tasks by Date (POST)
**URL:** `http://localhost:8080/task-mgmt/fetch-by-date/v2`
**Body (JSON):**
```json
{
  "assigneeIds": [1, 2],
  "startDate": 1754121600000,
  "endDate": 1756713600000
}
```
*(Note: `startDate` and `endDate` are Unix timestamps in milliseconds. The example values correspond to August 1, 2025 and August 31, 2025.)*

### 6. Update Task Priority (PUT)
**URL:** `http://localhost:8080/task-mgmt/1/priority` (Replace `1` with an actual task ID)
**Body (JSON):**
```json
{
  "priority": "LOW"
}
```

### 7. Get Tasks by Priority (GET)
**URL:** `http://localhost:8080/task-mgmt/priority/HIGH` (Replace `HIGH` with `MEDIUM` or `LOW`)

### 8. Add Comment to Task (POST)
**URL:** `http://localhost:8080/task-mgmt/1/comment` (Replace `1` with an actual task ID)
**Body (JSON):**
```json
{
  "commentText": "This task is critical and needs immediate attention.",
  "userId": 1
}
```

### 9. Get Task Details (GET)
**URL:** `http://localhost:8080/task-mgmt/1/details` (Replace `1` with an actual task ID)

## Project Structure

```
D:\GradleWorkspace\
├───.gradle/
├───.metadata/
├───.vscode/
├───build/
├───gradle/
├───src/
│   ├───main/
│   │   ├───java/
│   │   │   └───com/railse/hiring/workforcemgmt/
│   │   │       ├───common/
│   │   │       ├───controller/
│   │   │       ├───dto/
│   │   │       ├───mapper/
│   │   │       ├───model/
│   │   │       │   └───enums/
│   │   │       ├───repository/
│   │   │       └───service/
│   │   │           └───impl/
│   │   └───resources/
│   └───test/
│       └───java/
│           └───com/railse/hiring/workforcemgmt/
│               └───controller/
├───build.gradle.kts
├───gradle.properties
├───gradlew
├───gradlew.bat
├───requirements.txt
└───settings.gradle.kts
```

## Bugs Solved

1.  **Task Re-assignment Creates Duplicates:**
    *   **Issue:** Reassigning a task would create a new task without cancelling the old one, leading to duplicate entries.
    *   **Solution:** Modified the `assignByReference` logic to explicitly mark the previous task as `CANCELLED` when a new assignment occurs for the same reference.
2.  **Cancelled Tasks Clutter the View:**
    *   **Issue:** Task fetching endpoints were returning cancelled tasks, making the task list less relevant for active work.
    *   **Solution:** Updated the `fetchTasksByDate` method to filter out tasks with `CANCELLED` status, ensuring only relevant tasks are returned.

## Future Enhancements

*   **Authentication and Authorization:** Implement Spring Security for user authentication and role-based authorization.
*   **Persistent Database:** Migrate from H2 in-memory to a persistent database (e.g., PostgreSQL, MySQL) for production readiness.
*   **User Management:** Add full CRUD operations for staff/users.
*   **Notifications:** Implement a notification system for task assignments, status changes, and comments.
*   **Pagination & Sorting:** Enhance API endpoints to support pagination and sorting for large datasets.
*   **More Robust Error Handling:** Implement more granular and user-friendly error responses.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
