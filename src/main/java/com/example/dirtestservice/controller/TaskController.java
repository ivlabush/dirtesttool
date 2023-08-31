package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/tasks")
@Validated
public class TaskController {

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return null;
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable int id) {
        return null;
    }

    @PostMapping
    public TaskDto createTask(@Validated TaskDto task) {
        return null;
    }

    @PutMapping
    public TaskDto updateTask(@RequestBody @Validated TaskDto taskDto) {
        return null;
    }

    @DeleteMapping("/{id}")
    public TaskDto deleteTask(@PathVariable int id) {
        return null;
    }
}
