package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.dto.TaskResultDto;
import com.example.dirtestservice.service.TaskResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Task Results Controller", description = "REST API Controller to Operate Task Results")
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

    @GetMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasksByUrl(@RequestParam @NotBlank String url) {
        return mapper.map(service.getTaskResultsByUrl(url), type);
    }

    @GetMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasksByUrlContains(@RequestParam @NotBlank String url) {
        return mapper.map(service.getTaskResultsByUrlContains(url), type);
    }

    @DeleteMapping("/task/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTaskResultsByTaskId(@PathVariable @NotBlank String id) {
        service.deleteTaskResultsByTaskId(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAllTaskResults() {
        service.deleteAllTaskResults();
    }

    @DeleteMapping("/url")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTaskResultsByUrl(@RequestParam @NotBlank String url) {
        service.deleteAllTaskResultsByUrl(url);
    }

    @DeleteMapping("/url/contains")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTaskResultsByUrlContains(@RequestParam @NotBlank String url) {
        service.deleteAllTaskResultsByUrlContains(url);
    }
}
