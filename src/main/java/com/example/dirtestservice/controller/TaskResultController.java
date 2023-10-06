package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.ErrorResponseDto;
import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.dto.TaskResultDto;
import com.example.dirtestservice.service.TaskResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Provides all existing Tasks Results in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Tasks Results in the system", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            })
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResultDto> getAllResults() {
        return mapper.map(service.getAllTaskResults(), type);
    }

    @Operation(summary = "Provides Task Results by Task Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Task Results by Task Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Task Results weren't found by Task Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResultDto> getAllByTaskId(@PathVariable @NotBlank String id) {
        return mapper.map(service.getAllTaskResultsByTaskId(id), type);
    }

    @Operation(summary = "Provides Task Results by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Task Results by Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Task Result wasn't found by Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResultDto getTaskResultById(@PathVariable @NotBlank String id) {
        return mapper.map(service.getTaskResultById(id), TaskResultDto.class);
    }

    @Operation(summary = "Provides all Tasks Results by URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Tasks Results by URL", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Tasks Results weren't found by URL", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasksByUrl(@RequestParam @NotBlank String url) {
        return mapper.map(service.getTaskResultsByUrl(url), type);
    }

    @Operation(summary = "Provides all Tasks Results contains URL part")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Tasks Results by URL part", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Tasks Results weren't found by URL part", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasksByUrlContains(@RequestParam @NotBlank String url) {
        return mapper.map(service.getTaskResultsByUrlContains(url), type);
    }

    @Operation(summary = "Delete Task Result by Task Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Task Result has been deleted")
    })
    @DeleteMapping("/task/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTaskResultsByTaskId(@PathVariable @NotBlank String id) {
        service.deleteTaskResultsByTaskId(id);
    }

    @Operation(summary = "Delete all Tasks Results in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "All found Tasks Results in the system has been deleted"),
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAllTaskResults() {
        service.deleteAllTaskResults();
    }

    @Operation(summary = "Delete all Tasks Results by URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "All found Tasks Results by URL has been deleted")
    })
    @DeleteMapping("/url")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTaskResultsByUrl(@RequestParam @NotBlank String url) {
        service.deleteAllTaskResultsByUrl(url);
    }

    @Operation(summary = "Delete all Tasks Results by URL part")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "All found Task Results by URL part has been deleted")
    })
    @DeleteMapping("/url/contains")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteTaskResultsByUrlContains(@RequestParam @NotBlank String url) {
        service.deleteAllTaskResultsByUrlContains(url);
    }
}
