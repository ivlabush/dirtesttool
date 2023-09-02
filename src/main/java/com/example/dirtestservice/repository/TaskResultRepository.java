package com.example.dirtestservice.repository;

import com.example.dirtestservice.entity.TaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskResultRepository extends JpaRepository<TaskResultEntity, String> {

    Optional<List<TaskResultEntity>> findAllByTaskId(String id);

    void deleteAllByTaskId(String id);

}
