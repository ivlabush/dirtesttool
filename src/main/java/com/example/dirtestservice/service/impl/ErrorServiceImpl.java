package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.entity.ErrorEntity;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.exceptions.ErrorNotFoundException;
import com.example.dirtestservice.exceptions.TaskNotFoundException;
import com.example.dirtestservice.repository.ErrorRepository;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErrorServiceImpl implements ErrorService {

    private final ErrorRepository repository;
    private final TaskRepository taskRepository;

    @Override
    public List<ErrorEntity> getAllErrors() {
        return repository.findAll();
    }

    @Override
    public List<ErrorEntity> getErrorsByTaskId(String id) {
        return repository.findAllByTaskId(id)
                .orElseThrow(() -> new ErrorNotFoundException("Errors for task id=" + id + " weren't found"));
    }

    @Override
    public List<ErrorEntity> getErrorsByStatusCode(Integer statusCode) {
        return repository.findAllByStatusCode(statusCode)
                .orElseThrow(() -> new ErrorNotFoundException("Errors by status code " + statusCode + " weren't found"));
    }

    @Override
    public List<ErrorEntity> getErrorsByUrl(String url) {
        return repository.findAllByUrl(url)
                .orElseThrow(() -> new ErrorNotFoundException("Errors by url " + url + " weren't found"));
    }

    @Override
    public List<ErrorEntity> getErrorsByUrlContains(String url) {
        return repository.findAllByUrlContains(url)
                .orElseThrow(() -> new ErrorNotFoundException("Errors by url contains " + url + " weren't found"));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ErrorEntity createError(TaskEntity task, String url, Throwable e) {
        ErrorEntity entity = new ErrorEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setTask(task);
        entity.setUrl(url);

        Throwable cause = e.getCause();

        if (cause instanceof RestClientResponseException) {
            entity.setStatusCode(((RestClientResponseException) cause).getStatusCode().value());
        } else {
            entity.setStatusCode(0);
        }

        String message = cause.getMessage();
        if (message.length() > 255) message = message.substring(0, 255);
        entity.setMessage(message);

        String stacktrace = ExceptionUtils.getStackTrace(cause);
        if (stacktrace.length() > 255) stacktrace = stacktrace.substring(0, 255);

        entity.setStacktrace(stacktrace);
        return repository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAllErrors() {
        repository.deleteAll();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteErrorsByTaskId(String id) {
        TaskEntity entity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id=" + id + " wasn't found"));
        repository.deleteAllByTask(entity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteErrorsByUrl(String url) {
        repository.deleteAllByUrl(url);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteErrorsByUrlContains(String url) {
        repository.deleteAllByUrlContains(url);
    }
}
