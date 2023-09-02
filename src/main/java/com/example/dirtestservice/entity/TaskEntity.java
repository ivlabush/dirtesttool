package com.example.dirtestservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "tasks")
public class TaskEntity {

    @Id
    private String id;
    private String name;
    private String baseUrl;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "taskId")
    private Set<TaskResultEntity> taskResults = new HashSet<>();
}
