package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.entity.TaskResultEntity;
import com.example.dirtestservice.repository.TaskResultRepository;
import com.example.dirtestservice.exceptions.TaskResultsNotFound;
import com.example.dirtestservice.service.TaskResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskResultServiceImpl implements TaskResultService {

    private final TaskResultRepository repository;

    @Override
    public List<TaskResultEntity> getAllTaskResults() {
        return repository.findAll();
    }

    @Override
    public List<TaskResultEntity> getAllTaskResultsByTaskId(String id) {
        return repository.findAllByTaskId(id)
                .orElseThrow(() -> new TaskResultsNotFound("Task Results for taskId " + id + " wasn't found"));
    }

    @Override
    public TaskResultEntity getTaskResultById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskResultsNotFound("Task Results with id " + id + " wasn't found"));
    }

    @Override
    public void clearTaskResultsByTaskId(String id) {
        repository.deleteAllByTaskId(id);
    }

    @Override
    public void clearAllTAskResults() {
        repository.deleteAll();
    }
}
