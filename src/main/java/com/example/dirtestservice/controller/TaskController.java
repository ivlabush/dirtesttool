package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.ErrorResponseDto;
import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.service.TaskExecutionService;
import com.example.dirtestservice.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tasks Controller", description = "REST API Controller to Operate Tasks")
public class TaskController {

    private final TaskService service;
    private final TaskExecutionService executionService;
    private final ModelMapper mapper;

    private static final Type type = new TypeToken<List<TaskDto>>() {
    }.getType();

    @Operation(summary = "Provides all existing Tasks in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Tasks in the system", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            })
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getAllTasks() {
        return mapper.map(service.getAllTasks(), type);
    }

    @Operation(summary = "Provides Task by Task Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Task by Task Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Task wasn't found by Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto getTaskById(@PathVariable @NotBlank String id) {
        return mapper.map(service.getTaskById(id), TaskDto.class);
    }

    @Operation(summary = "Provides all Tasks by URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Tasks by URL", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Tasks weren't found by URL", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasksByUrl(@RequestParam @NotBlank String url) {
        return mapper.map(service.getTaskByUrl(url), type);
    }

    @Operation(summary = "Provides all Tasks contains URL part")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Tasks by URL part", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Tasks weren't found by URL part", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasksByUrlContains(@RequestParam @NotBlank String url) {
        return mapper.map(service.getTasksByUrlContains(url), type);
    }

    @Operation(summary = "Create Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Create task", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Task body failed validation", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TaskDto createTask(@RequestBody @Valid TaskDto task) {
        return mapper.map(service.createTask(task), TaskDto.class);
    }

    @Operation(summary = "Update Task by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update task", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Task body failed validation", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTask(@PathVariable @NotBlank String id,
                              @RequestBody @Valid TaskDto taskDto) {
        return mapper.map(service.updateTask(id, taskDto), TaskDto.class);
    }

    @Operation(summary = "Delete all Tasks in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Tasks in the system has been deleted"),
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllTasks() {
        service.deleteAllTasks();
    }

    @Operation(summary = "Delete Task by Task Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task has been deleted")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable @NotBlank String id) {
        service.deleteTask(id);
    }

    @Operation(summary = "Delete all Errors by URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Tasks by URL has been deleted")
    })
    @DeleteMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTaskByUrl(@RequestParam @NotBlank String url) {
        service.deleteTaskByBaseUrl(url);
    }

    @Operation(summary = "Delete all Tasks by URL part")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors by URL part has been deleted")
    })
    @DeleteMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTaskByUrlContains(@RequestParam @NotBlank String url) {
        service.deleteTaskByBaseUrlContains(url);
    }

    @Operation(summary = "Start task execution by Task Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "All found Errors by URL part has been deleted", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Task wasn't found by Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            }),
            @ApiResponse(responseCode = "500", description = "Error while processing Task", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @PostMapping("/start/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String startTask(@PathVariable @NotBlank String id) {
        executionService.startTask(id);
        return "Execution of task " + id + " started at " + new Date();
    }
}
