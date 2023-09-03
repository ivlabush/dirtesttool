package com.example.dirtestservice.service;

import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.entity.TaskResultEntity;

import java.util.List;

public interface TaskResultService {

    List<TaskResultEntity> getAllTaskResults();

    List<TaskResultEntity> getAllTaskResultsByTaskId(String id);

    TaskResultEntity getTaskResultById(String id);

    List<TaskResultEntity> getTaskResultsByUrl(String url);

    List<TaskResultEntity> getTaskResultsByUrlContains(String url);

    TaskResultEntity createTaskResult(String url, TaskEntity task, int code);

    void deleteTaskResultsByTaskId(String id);

    void deleteAllTaskResults();

    void deleteAllTaskResultsByUrl(String url);

    void deleteAllTaskResultsByUrlContains(String url);
}
