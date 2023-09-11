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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ErrorControllerTest {

    @LocalServerPort
    private Integer port;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("postgres")
            .withUsername("root")
            .withPassword("toor");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

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
    public void testGetErrorsByStatusCode() {
        TaskEntity entity = createTaskWithError();

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/errors/statuscode/400")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
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
