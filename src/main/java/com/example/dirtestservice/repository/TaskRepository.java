package com.example.dirtestservice.repository;

import com.example.dirtestservice.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, String> {

    Optional<List<TaskEntity>> findAllByBaseUrl(String url);

    @Query("from TaskEntity as e where e.baseUrl like %?1%")
    Optional<List<TaskEntity>> findTasksByUrlContains(String url);

    void deleteAllByBaseUrl(String baseUrl);

    @Query("delete from TaskEntity e where e.baseUrl like %?1%")
    @Modifying
    void deleteAllByBaseUrlContains(String baseUrl);
}
