package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.ErrorDto;
import com.example.dirtestservice.dto.ErrorResponseDto;
import com.example.dirtestservice.service.ErrorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@RequestMapping("/errors")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Error Controller", description = "REST API Controller to Operate Errors")
public class ErrorController {

    private final ModelMapper mapper;
    private final ErrorService service;

    private static final Type type = new TypeToken<List<ErrorDto>>() {
    }.getType();

    @Operation(summary = "Provides all Errors in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All found Errors in the system", content = {
                @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorDto.class)
                )
        })
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrors() {
        return mapper.map(service.getAllErrors(), type);
    }

    @Operation(summary = "Provides all Errors by Task Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors by Task Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Errors weren't found by Id", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrorsByTaskId(@PathVariable @NotBlank String id) {
        return mapper.map(service.getErrorsByTaskId(id), type);
    }

    @Operation(summary = "Provides all Errors by Status Code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors by Status Code", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Errors weren't found by Status Code", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/statuscode/{code}")
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrorsByStatusCode(@PathVariable @NotNull Integer code) {
        return mapper.map(service.getErrorsByStatusCode(code), type);
    }

    @Operation(summary = "Provides all Errors by URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors by URL", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Errors weren't found by URL", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrorsByUrl(@RequestParam @NotBlank String url) {
        return mapper.map(service.getErrorsByUrl(url), type);
    }

    @Operation(summary = "Provides all Errors contains URL part")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors by URL part", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDto.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Errors weren't found by URL part", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            })
    })
    @GetMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public List<ErrorDto> getAllErrorsByUrlContains(@RequestParam @NotBlank String url) {
        return mapper.map(service.getErrorsByUrlContains(url), type);
    }

    @Operation(summary = "Delete all Errors in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors in the system has been deleted"),
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllErrors() {
        service.deleteAllErrors();
    }

    @Operation(summary = "Delete all Errors by Task Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors by Task Id has been deleted"),
            @ApiResponse(responseCode = "404", description = "Errors weren't found by by Task Id")
    })
    @DeleteMapping("/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllErrorsByTaskId(@PathVariable @NotBlank String id) {
        service.deleteErrorsByTaskId(id);
    }

    @Operation(summary = "Delete all Errors by URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors by URL has been deleted"),
            @ApiResponse(responseCode = "404", description = "Errors weren't found by URL")
    })
    @DeleteMapping("/url")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllErrorsByUrl(@RequestParam @NotBlank String url) {
        service.deleteErrorsByUrl(url);
    }

    @Operation(summary = "Delete all Errors by URL part")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All found Errors by URL part has been deleted"),
            @ApiResponse(responseCode = "404", description = "Errors weren't found by URL part")
    })
    @DeleteMapping("/url/contains")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllErrorsByUrlContains(@RequestParam @NotBlank String url) {
        service.deleteErrorsByUrlContains(url);
    }
}
