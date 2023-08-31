package com.example.dirtestservice.service;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;

import java.util.List;

public interface TaskService {

    List<TaskEntity> getAllTasks();

    TaskEntity getTaskById(int id);

    TaskEntity createTask();

    TaskEntity updateTask(TaskDto task);

    TaskEntity deleteTask(int id);

}
