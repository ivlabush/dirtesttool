package com.example.dirtestservice.service;

import com.example.dirtestservice.entity.TaskResultEntity;

import java.util.List;

public interface TaskResultService {

    List<TaskResultEntity> getAllTaskResults();

    List<TaskResultEntity> getAllTaskResultsByTaskId(String id);

    TaskResultEntity getTaskResultById(String id);

    void clearTaskResultsByTaskId(String id);

    void clearAllTAskResults();
}
