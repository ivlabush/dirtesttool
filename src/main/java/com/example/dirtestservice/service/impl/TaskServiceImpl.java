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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final ModelMapper mapper;

    private static final String TASK_NAME_PREFIX = "task-";

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
    public List<TaskEntity> getTaskByUrl(String url) {
        List<TaskEntity> result = repository.findAllByBaseUrl(url);
        if (result.isEmpty()) {
            throw new TaskNotFoundException("Tasks by url=" + url + " weren't found");
        }
        return result;
    }

    @Override
    public List<TaskEntity> getTasksByUrlContains(String url) {
        List<TaskEntity> result = repository.findTasksByUrlContains(url);
        if (result.isEmpty()) {
            throw new TaskNotFoundException("Tasks containing url=" + url + " weren't found");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TaskEntity createTask(TaskDto task) {
        String id = UUID.randomUUID().toString();
        task.setId(id);
        task.setName(TASK_NAME_PREFIX + id);
        return repository.save(mapper.map(task, TaskEntity.class));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TaskEntity updateTask(String id, TaskDto task) {
        TaskEntity entity = getTaskById(id);
        entity.setBaseUrl(task.getBaseUrl());
        return repository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TaskEntity save(TaskEntity entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAllTasks() {
        repository.deleteAll();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteTask(String id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteTaskByBaseUrl(String baseUrl) {
        repository.deleteAllByBaseUrl(baseUrl);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteTaskByBaseUrlContains(String baseUrl) {
        repository.deleteAllByBaseUrlContains(baseUrl);
    }
}
