package com.example.dirtestservice.service;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;

import java.util.List;

public interface TaskService {

    List<TaskEntity> getAllTasks();

    TaskEntity getTaskById(String id);

    TaskEntity createTask(TaskDto task);

    TaskEntity updateTask(String id, TaskDto task);

    TaskEntity deleteTask(String id);

    void startTask(String id);

}
