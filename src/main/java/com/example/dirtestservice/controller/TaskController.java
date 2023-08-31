package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/tasks")
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
    public TaskDto createTask() {
        return null;
    }

    @PutMapping
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {
        return null;
    }

    @DeleteMapping("/{id}")
    public TaskDto deleteTask(@PathVariable int id) {
        return null;
    }
}
