package com.example.dirtestservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tasks")
public class TaskEntity {

    @Id
    private String id;
    private String name;
    private String baseUrl;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskResultEntity> taskResults = new ArrayList<>();
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<ErrorEntity> errors = new ArrayList<>();
}
