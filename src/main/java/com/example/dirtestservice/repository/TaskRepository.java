package com.example.dirtestservice.repository;

import com.example.dirtestservice.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {


}
