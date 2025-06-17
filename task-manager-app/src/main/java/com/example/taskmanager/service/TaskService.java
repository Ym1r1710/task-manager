package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskDTO;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskList;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskListRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskListRepository taskListRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskListRepository = taskListRepository;
    }

    public List<TaskDTO> getAll() {
        return taskRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TaskDTO> getById(Long id) {
        return taskRepository.findById(id).map(this::toDTO);
    }

    public TaskDTO create(TaskDTO dto) {
        Task task = fromDTO(dto);
        return toDTO(taskRepository.save(task));
    }

    private Task fromDTO(TaskDTO dto) {
        User user = userRepository.findById(dto.getAssigneeId()).orElse(null);
        TaskList taskList = taskListRepository.findById(dto.getTaskListId()).orElse(null);
        return new Task(dto.getId(), dto.getTitle(), dto.getDescription(), dto.getStatus(), user, taskList);
    }

    private TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getAssignee() != null ? task.getAssignee().getId() : null,
                task.getTaskList() != null ? task.getTaskList().getId() : null
        );
    }
}