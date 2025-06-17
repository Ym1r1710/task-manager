package com.example.taskmanager.dto;

public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long assigneeId;
    private Long taskListId;

    public TaskDTO() {}

    public TaskDTO(Long id, String title, String description, String status, Long assigneeId, Long taskListId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assigneeId = assigneeId;
        this.taskListId = taskListId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }
    public Long getTaskListId() { return taskListId; }
    public void setTaskListId(Long taskListId) { this.taskListId = taskListId; }
}