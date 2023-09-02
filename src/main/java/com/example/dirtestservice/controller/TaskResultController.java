package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskResultDto;
import com.example.dirtestservice.service.TaskResultService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/taskresults")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class TaskResultController {

    private final TaskResultService service;
    private final ModelMapper mapper;

    private static final Type type = new TypeToken<List<TaskResultDto>>() {
    }.getType();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResultDto> getAllResults() {
        return mapper.map(service.getAllTaskResults(), type);
    }

    @GetMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResultDto> getAllByTaskId(@PathVariable @NotBlank String id) {
        return mapper.map(service.getAllTaskResultsByTaskId(id), type);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResultDto getTaskResultById(@PathVariable @NotBlank String id) {
        return mapper.map(service.getTaskResultById(id), TaskResultDto.class);
    }

    @DeleteMapping("/task/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTaskResultsByTaskId(@PathVariable @NotBlank String id) {
        service.clearTaskResultsByTaskId(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAllTaskResults() {
        service.clearAllTAskResults();
    }
}
