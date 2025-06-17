package com.example.taskmanager.model;

import jakarta.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String status;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "task_list_id")
    private TaskList taskList;

    public Task() {}

    public Task(Long id, String title, String description, String status, User assignee, TaskList taskList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignee = assignee;
        this.taskList = taskList;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public User getAssignee() { return assignee; }
    public void setAssignee(User assignee) { this.assignee = assignee; }
    public TaskList getTaskList() { return taskList; }
    public void setTaskList(TaskList taskList) { this.taskList = taskList; }
}