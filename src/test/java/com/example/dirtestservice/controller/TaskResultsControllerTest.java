package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.entity.TaskResultEntity;
import com.example.dirtestservice.repository.TaskResultRepository;
import com.example.dirtestservice.service.TaskResultService;
import com.example.dirtestservice.service.TaskService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TaskResultsControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("postgres")
            .withUsername("root")
            .withPassword("toor");

    private final String url = "http://localhost:8000/";

    @Autowired
    private TaskResultRepository taskResultRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskResultService taskResultService;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        taskResultRepository.deleteAll();
    }

    private final int STATUS_CODE = 200;

    @Test
    public void testGetAllTaskResults() {
        TaskResultEntity entity = createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/taskresults")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getTask().getId()))
                .body("[0].status", equalTo(STATUS_CODE));
    }

    @Test
    public void testGetAllErrorResultsByTaskId() {
        TaskResultEntity entity = createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/taskresults/task/" + entity.getTask().getId())
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getTask().getId()))
                .body("[0].status", equalTo(STATUS_CODE));
    }

    @Test
    public void testGetAllTaskResultsByTaskIdEntityNotFound() {
        createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/taskresults/task/123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Task Results for taskId 123 weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testGetTaskResultById() {
        TaskResultEntity result = createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/taskresults/" + result.getId())
                .then()
                .statusCode(200)
                .body("id", equalTo(result.getId()))
                .body("url", equalTo(result.getUrl()))
                .body("taskId", equalTo(result.getTask().getId()))
                .body("status", equalTo(result.getStatusCode()));
    }

    @Test
    public void testGetTaskResultByIdEntityNotFound() {
        createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/taskresults/123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Task Results with id 123 wasn't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testGetTaskResultsByUrl() {
        TaskResultEntity entity = createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .param("url", url + "1")
                .when()
                .get("/taskresults/url")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getTask().getId()))
                .body("[0].status", equalTo(STATUS_CODE));
    }

    @Test
    public void testGetTaskResultsByUrlEntityNotFound() {
        createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .param("url", "aaa")
                .when()
                .get("/taskresults/url")
                .then()
                .statusCode(404)
                .body("message", equalTo("Task Results by url aaa weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testGetTaskResultsByUrlContains() {
        TaskResultEntity entity = createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .param("url", url + "1")
                .when()
                .get("/taskresults/url/contains")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].id", is(notNullValue()))
                .body("[0].url", equalTo(url + "1"))
                .body("[0].taskId", equalTo(entity.getTask().getId()))
                .body("[0].status", equalTo(STATUS_CODE));
    }

    @Test
    public void testGetTaskResultsByUrlContainsEntityNotFound() {
        createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .param("url", "aaa")
                .when()
                .get("/taskresults/url/contains")
                .then()
                .statusCode(404)
                .body("message", equalTo("Task Results by url aaa weren't found"))
                .body("date", is(notNullValue()));
    }

    @Test
    public void testDeleteByTaskIdTaskNotFound() {
        createTaskWithTaskResults();

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/taskresults/task/123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Task with id=123 wasn't found"))
                .body("date", is(notNullValue()));
    }

    private TaskResultEntity createTaskWithTaskResults() {
        TaskDto dto = new TaskDto();
        dto.setBaseUrl(url);

        TaskEntity task = taskService.createTask(dto);

        return taskResultService.createTaskResult(url + "1", task, STATUS_CODE);
    }
}
