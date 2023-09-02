package com.example.dirtestservice.service.impl;

import com.example.dirtestservice.service.TaskExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskExecutionServiceImpl implements TaskExecutionService {

    private final RestTemplate template;

    @Override
    public void startTask(String id) {

    }
}
