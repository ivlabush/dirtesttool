package com.example.dirtestservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Entity
@Data
@Table(name = "task_results")
public class TaskResultEntity {

    @Id
    private String id;
    private String url;
    private String taskId;
    private HttpStatus status;
}
