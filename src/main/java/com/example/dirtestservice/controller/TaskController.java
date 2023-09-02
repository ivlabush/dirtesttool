package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
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
import java.util.List;

@RestController
@RequestMapping("/tasks")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {

    private final TaskService service;
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto deleteTask(@PathVariable @NotBlank String id) {
        return mapper.map(service.deleteTask(id), TaskDto.class);
    }

    @PostMapping("/start/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String startTask(@PathVariable @NotBlank String id) {
        return service.startTask(id);
    }
}
