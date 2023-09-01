package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.service.TaskExecutionService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TaskExecutionServiceImpl implements TaskExecutionService {

    @Override
    public String startTask(String id) {
        return "Execution of task " + id + " started at " + new Date();
    }
}
