package com.example.dirtestservice.repository;

import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.entity.TaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskResultRepository extends JpaRepository<TaskResultEntity, String> {

    List<TaskResultEntity> findAllByTaskId(String id);

    List<TaskResultEntity> findAllByUrl(String url);

    @Query("from TaskResultEntity e where e.url like %?1%")
    List<TaskResultEntity> findAllByUrlContains(String url);

    void deleteAllByTask(TaskEntity entity);

    void deleteAllByUrl(String url);

    @Query("delete from TaskResultEntity e where e.url like %?1%")
    @Modifying
    void deleteAllByUrlContains(String url);

}
