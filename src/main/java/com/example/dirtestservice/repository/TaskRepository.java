package com.example.dirtestservice.repository;

import com.example.dirtestservice.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, String> {

    List<TaskEntity> findAllByBaseUrl(String url);

    @Query("from TaskEntity as e where e.baseUrl like %?1%")
    List<TaskEntity> findTasksByUrlContains(String url);

    void deleteAllByBaseUrl(String baseUrl);

    @Query("delete from TaskEntity e where e.baseUrl like %?1%")
    @Modifying
    void deleteAllByBaseUrlContains(String baseUrl);
}
