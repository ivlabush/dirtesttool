package com.example.dirtestservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDto {

    private int id;
    private String name;
    @NotBlank(message = "Base URL can't be blank")
    private String baseUrl;
}
