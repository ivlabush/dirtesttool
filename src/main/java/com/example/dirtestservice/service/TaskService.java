package com.example.dirtestservice.service;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;

import java.util.List;

public interface TaskService {

    List<TaskEntity> getAllTasks();

    TaskEntity getTaskById(String id);

    List<TaskEntity> getTaskByUrl(String url);

    List<TaskEntity> getTasksByUrlContains(String url);

    TaskEntity createTask(TaskDto task);

    TaskEntity updateTask(String id, TaskDto task);

    TaskEntity save(TaskEntity entity);

    void deleteAllTasks();

    void deleteTask(String id);

    void deleteTaskByBaseUrl(String baseUrl);

    void deleteTaskByBaseUrlContains(String baseUrl);
}
