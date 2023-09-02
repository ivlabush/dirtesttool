package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.entity.ErrorEntity;
import com.example.dirtestservice.exceptions.ErrorNotFoundException;
import com.example.dirtestservice.repository.ErrorRepository;
import com.example.dirtestservice.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
