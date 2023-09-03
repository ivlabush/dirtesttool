package com.example.dirtestservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "errors")
@Data
public class ErrorEntity {

    @Id
    private String id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;
    private Integer statusCode;
    private String message;
    private String stacktrace;
}
