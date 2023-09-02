package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.entity.TaskResultEntity;
import com.example.dirtestservice.exceptions.TaskResultsNotFoundException;
import com.example.dirtestservice.repository.TaskResultRepository;
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
                .orElseThrow(() -> new TaskResultsNotFoundException("Task Results for taskId " + id + " wasn't found"));
    }

    @Override
    public TaskResultEntity getTaskResultById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskResultsNotFoundException("Task Results with id " + id + " wasn't found"));
    }

    @Override
    public List<TaskResultEntity> getTaskResultsByUrl(String url) {
        return repository.findAllByUrl(url)
                .orElseThrow(() -> new TaskResultsNotFoundException("Task Results by url " + url + " weren't found"));
    }

    @Override
    public List<TaskResultEntity> getTaskResultsByUrlContains(String url) {
        return repository.findAllByUrlContains(url)
                .orElseThrow(() -> new TaskResultsNotFoundException("Task Results by url " + url + " weren't found"));
    }

    @Override
    public void deleteTaskResultsByTaskId(String id) {
        repository.deleteAllByTaskId(id);
    }

    @Override
    public void deleteAllTaskResults() {
        repository.deleteAll();
    }

    @Override
    public void deleteAllTaskResultsByUrl(String url) {
        repository.deleteAllByUrl(url);
    }

    @Override
    public void deleteAllTaskResultsByUrlContains(String url) {
        repository.deleteAllByUrlContains(url);
    }
}
