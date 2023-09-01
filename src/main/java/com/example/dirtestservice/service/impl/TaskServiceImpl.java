package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.exceptions.TaskNotFoundException;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final ModelMapper mapper;

    @Override
    public List<TaskEntity> getAllTasks() {
        return repository.findAll();
    }

    @Override
    public TaskEntity getTaskById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id=" + id + " wasn't found"));
    }

    @Override
    public TaskEntity createTask(TaskDto task) {
        String id = UUID.randomUUID().toString();
        task.setId(id);
        task.setName("task-" + id);
        return repository.save(mapper.map(task, TaskEntity.class));
    }

    @Override
    public TaskEntity updateTask(TaskDto task) {
        return repository.save(mapper.map(task, TaskEntity.class));
    }

    @Override
    public TaskEntity deleteTask(String id) {
        TaskEntity entity = getTaskById(id);
        repository.deleteById(id);
        return entity;
    }
}
