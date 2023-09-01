package com.example.dirtestservice.dto;

import com.example.dirtestservice.validator.Url;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {

    @Null(message = "Task id shouldn't be populated as it will be provided automatically")
    private String id;
    @Null(message = "Task name shouldn't be populated as it will be provided automatically")
    private String name;
    @NotBlank(message = "Base URL can't be blank")
    @Url(message = "Malformed URL provided as base URL")
    private String baseUrl;
}
