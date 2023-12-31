package com.example.dirtestservice.repository;

import com.example.dirtestservice.entity.ErrorEntity;
import com.example.dirtestservice.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ErrorRepository extends JpaRepository<ErrorEntity, String> {

    List<ErrorEntity> findAllByTaskId(String id);

    List<ErrorEntity> findAllByStatusCode(Integer statusCode);

    List<ErrorEntity> findAllByUrl(String url);

    @Query("from ErrorEntity as e where e.url like %?1%")
    List<ErrorEntity> findAllByUrlContains(String url);

    void deleteAllByTask(TaskEntity entity);

    void deleteAllByUrl(String url);

    @Query("delete from ErrorEntity e where e.url like %?1% ")
    @Modifying
    void deleteAllByUrlContains(String url);
}
