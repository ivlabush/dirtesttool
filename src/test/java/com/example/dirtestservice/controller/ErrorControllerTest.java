package com.example.dirtestservice.controller;

import com.example.dirtestservice.dto.TaskDto;
import com.example.dirtestservice.entity.TaskEntity;
import com.example.dirtestservice.repository.TaskRepository;
import com.example.dirtestservice.service.ErrorService;
import com.example.dirtestservice.service.TaskService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ErrorControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("postgres")
            .withUsername("root")
            .withPassword("toor");

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ErrorService errorService;

    private final String url = "http://localhost:8000/";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        taskRepository.deleteAll();
    }

    @Test
    public void testGetAllErrors() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    public void testGetErrorsByTaskId() {
        TaskEntity entity = createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/task/" + entity.getId())
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    public void testGetErrorsByTaskIdEntityNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/task/123")
                .then()
                .statusCode(404)
                .body("message", equalTo("Errors for task id=123 weren't found"));
    }

    @Test
    public void testGetErrorsByStatusCode() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/statuscode/400")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    public void testGetErrorsByStatusCodeEntityNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/statuscode/100")
                .then()
                .statusCode(404)
                .body("message", equalTo("Errors by status code 100 weren't found"));
    }

    @Test
    public void testGetErrorsByUrl() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .param("url", url + "1")
                .when()
                .get("/errors/url")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    public void testGetErrorsByUrlEntityNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .param("url", "aaa")
                .when()
                .get("/errors/url")
                .then()
                .statusCode(404)
                .body("message", equalTo("Errors by url aaa weren't found"));
    }

    @Test
    public void testGetErrorsByUrlContains() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .param("url", url)
                .when()
                .get("/errors/url/contains")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    public void testGetErrorsByUrlContainsEntityNotFound() {
        createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .param("url", "aaa")
                .when()
                .get("/errors/url/contains")
                .then()
                .statusCode(404)
                .body("message", equalTo("Errors by url contains aaa weren't found"));
    }

    private TaskEntity createTaskWithError() {
        TaskDto dto = new TaskDto();
        dto.setBaseUrl(url);

        TaskEntity task = taskService.createTask(dto);

        HttpClientErrorException cause = HttpClientErrorException
                .create("Message1",
                        HttpStatusCode.valueOf(400), "Status Text1", new HttpHeaders(), new byte[]{}, null);
        StackTraceElement[] stackTrace = new StackTraceElement[]{new StackTraceElement("Class Loader Name", "Module Name", "1",
                "Declaring Class", "Method Name", "File name", 1)};
        cause.setStackTrace(stackTrace);

        errorService.createError(task, url + "1", new RuntimeException(cause));

        return task;
    }
}
