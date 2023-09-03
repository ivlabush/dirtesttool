package com.example.dirtestservice.repository;

import com.example.dirtestservice.entity.ErrorEntity;
import com.example.dirtestservice.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ErrorRepository extends JpaRepository<ErrorEntity, String> {

    Optional<List<ErrorEntity>> findAllByTaskId(String id);

    Optional<List<ErrorEntity>> findAllByStatusCode(Integer statusCode);

    Optional<List<ErrorEntity>> findAllByUrl(String url);

    @Query("from ErrorEntity as e where e.url like %?1%")
    Optional<List<ErrorEntity>> findAllByUrlContains(String url);

    void deleteAllByTask(TaskEntity entity);

    void deleteAllByUrl(String url);

    @Query("delete from ErrorEntity e where e.url like %?1% ")
    void deleteAllByUrlContains(String url);
}
