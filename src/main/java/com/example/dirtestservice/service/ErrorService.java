package com.example.dirtestservice.service;

import com.example.dirtestservice.entity.ErrorEntity;

import java.util.List;

public interface ErrorService {

    List<ErrorEntity> getAllErrors();

    List<ErrorEntity> getErrorsByTaskId(String id);

    List<ErrorEntity> getErrorsByStatusCode(Integer statusCode);

    List<ErrorEntity> getErrorsByUrl(String url);

    List<ErrorEntity> getErrorsByUrlContains(String url);

    void deleteAllErrors();

    void deleteErrorsByTaskId(String id);

    void deleteErrorsByUrl(String url);

    void deleteErrorsByUrlContains(String url);
}
