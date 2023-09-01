# Dir Test Service

Automation service for testing directories of Web Applications.

## Prerequisites
1. Java 17+
2. PostgreSQL 15
3. Maven 3.8.1+

## Migration
1. Update conf/migration.conf file, add username and password for root user
2. Run ```mvn clean flyway:migrate -Dflyway.configFiles=migration.conf``` on running PostgreSQL DB with existing schema ```????```

## REST APIs
Service expose REST APIs to manipulate configuration and runtime of the task adn obtain tasks results

### Tasks API
```GET /tasks/``` provide all existing tasks in the system
```GET /task/{id}``` provide task by id
```POST /tasks```

