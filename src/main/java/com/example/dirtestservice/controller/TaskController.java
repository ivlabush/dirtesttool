package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.service.TaskExecutionService;
import com.example.dirtestservice.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {

    private final TaskService service;
    private final TaskExecutionService executionService;
    private final ModelMapper mapper;

    private static final Type type = new TypeToken<List<TaskDto>>() {
    }.getType();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getAllTasks() {
        return mapper.map(service.getAllTasks(), type);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto getTaskById(@PathVariable @NotBlank String id) {
        return mapper.map(service.getTaskById(id), TaskDto.class);
    }

    @GetMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasksByUrl(@RequestParam @NotBlank String url) {
        return mapper.map(service.getTaskByUrl(url), type);
    }

    @GetMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasksByUrlContains(@RequestParam @NotBlank String url) {
        return mapper.map(service.getTasksByUrlContains(url), type);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskDto createTask(@RequestBody @Valid TaskDto task) {
        return mapper.map(service.createTask(task), TaskDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTask(@PathVariable @NotBlank String id,
                              @RequestBody @Valid TaskDto taskDto) {
        return mapper.map(service.updateTask(id, taskDto), TaskDto.class);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllTasks() {
        service.deleteAllTasks();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable @NotBlank String id) {
        service.deleteTask(id);
    }

    @DeleteMapping("/url/{url}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTaskByUrl(@PathVariable @NotBlank String url) {
        service.deleteTaskByBaseUrl(url);
    }

    @DeleteMapping("/url/contains/{url}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTaskByUrlContains(@PathVariable @NotBlank String url) {
        service.deleteTaskByBaseUrlContains(url);
    }

    @PostMapping("/start/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String startTask(@PathVariable @NotBlank String id) {
        executionService.startTask(id);
        return "Execution of task " + id + " started at " + new Date();
    }
}
