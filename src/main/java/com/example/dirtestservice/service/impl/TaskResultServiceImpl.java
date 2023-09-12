package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.entity.TaskResultEntity;
import com.example.dirtestservice.exceptions.TaskNotFoundException;
import com.example.dirtestservice.exceptions.TaskResultsNotFoundException;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.repository.TaskResultRepository;
import com.example.dirtestservice.service.TaskResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskResultServiceImpl implements TaskResultService {

    private final TaskResultRepository repository;
    private final TaskRepository taskRepository;

    @Override
    public List<TaskResultEntity> getAllTaskResults() {
        return repository.findAll();
    }

    @Override
    public List<TaskResultEntity> getAllTaskResultsByTaskId(String id) {
        List<TaskResultEntity> result = repository.findAllByTaskId(id);
        if (result.isEmpty()) {
            throw new TaskResultsNotFoundException("Task Results for taskId " + id + " weren't found");
        }
        return result;
    }

    @Override
    public TaskResultEntity getTaskResultById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskResultsNotFoundException("Task Results with id " + id + " wasn't found"));
    }

    @Override
    public List<TaskResultEntity> getTaskResultsByUrl(String url) {
        List<TaskResultEntity> result = repository.findAllByUrl(url);
        if (result.isEmpty()) {
            throw new TaskResultsNotFoundException("Task Results by url " + url + " weren't found");
        }
        return result;
    }

    @Override
    public List<TaskResultEntity> getTaskResultsByUrlContains(String url) {
        List<TaskResultEntity> result = repository.findAllByUrlContains(url);
        if (result.isEmpty()) {
            throw new TaskResultsNotFoundException("Task Results by url " + url + " weren't found");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TaskResultEntity createTaskResult(String url, TaskEntity task, int code) {
        TaskResultEntity entity = new TaskResultEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setUrl(url);
        entity.setTask(task);
        entity.setStatusCode(code);
        return repository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteTaskResultsByTaskId(String id) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id=" + id + " wasn't found"));
        repository.deleteAllByTask(entity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAllTaskResults() {
        repository.deleteAll();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAllTaskResultsByUrl(String url) {
        repository.deleteAllByUrl(url);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAllTaskResultsByUrlContains(String url) {
        repository.deleteAllByUrlContains(url);
    }
}
