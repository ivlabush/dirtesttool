package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController("/tasks")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {

    private final TaskService service;
    private final ModelMapper mapper;

    private static final Type type = new TypeToken<List<TaskDto>>() {}.getType();

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<TaskDto> getAllTasks() {
        return mapper.map(service.getAllTasks(), type);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskDto getTaskById(@PathVariable int id) {
        return mapper.map(service.getTaskById(id), TaskDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public TaskDto createTask(@Valid TaskDto task) {
        return mapper.map(service.createTask(task), TaskDto.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskDto updateTask(@RequestBody @Valid TaskDto taskDto) {
        return mapper.map(service.updateTask(taskDto), TaskDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskDto deleteTask(@PathVariable int id) {
        return mapper.map(service.deleteTask(id), TaskDto.class);
    }
}
