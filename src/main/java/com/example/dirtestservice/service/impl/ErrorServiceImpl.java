package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.entity.ErrorEntity;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.exceptions.ErrorNotFoundException;
import com.example.dirtestservice.repository.ErrorRepository;
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
        if (e instanceof RestClientResponseException) {
            entity.setStatusCode(((RestClientResponseException) e).getStatusCode().value());
        } else {
            entity.setStatusCode(0);
        }
        entity.setMessage(e.getMessage());
        entity.setStacktrace(ExceptionUtils.getStackTrace(e));
        return repository.save(entity);
    }

    @Override
    public void deleteAllErrors() {
        repository.deleteAll();
    }

    @Override
    public void deleteErrorsByTaskId(String id) {
        repository.deleteAllByTaskId(id);
    }

    @Override
    public void deleteErrorsByUrl(String url) {
        repository.deleteAllByUrl(url);
    }

    @Override
    public void deleteErrorsByUrlContains(String url) {
        repository.deleteAllByUrlContains(url);
    }
}
