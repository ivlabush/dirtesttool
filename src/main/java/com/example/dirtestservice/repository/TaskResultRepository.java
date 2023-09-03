package com.example.dirtestservice.repository;

import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.entity.TaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskResultRepository extends JpaRepository<TaskResultEntity, String> {

    Optional<List<TaskResultEntity>> findAllByTaskId(String id);

    Optional<List<TaskResultEntity>> findAllByUrl(String url);

    @Query("from TaskResultEntity e where e.url like %?1%")
    Optional<List<TaskResultEntity>> findAllByUrlContains(String url);

    void deleteAllByTask(TaskEntity entity);

    void deleteAllByUrl(String url);

    @Query("delete from TaskResultEntity e where e.url like %?1%")
    void deleteAllByUrlContains(String url);

}
