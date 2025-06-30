# track-expenses-app-backend

<div align="center">
  <img src="docs/images/placeholder-150.png" alt="Track Expenses app logo" />
</div>

* [About project](#about-project)
* [Prerequisites](#prerequisites)
* [How to run](#how-to-run)
* [Tech stack](#tech-stack)
  - [Development](#development)
  
## About project

The main aim for this project is for it to allow a user to track their own expenses.
In the application the [_REST_](https://pl.wikipedia.org/wiki/Representational_state_transfer) architecture and [_Minimum
Viable Product (MVP)_](https://www.parp.gov.pl/component/content/article/52414:minimum-viable-product) model is used.

## Prerequisites
The following tools are required to start the application:
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) / [VSC](https://code.visualstudio.com/) / [Eclipse](https://www.eclipse.org/)
- [Java 17 LTS](https://openjdk.org/projects/jdk/17/)
- [MySql Workbench](https://www.mysql.com/products/workbench/) / [DBeaver](https://dbeaver.io/)
- [Git Bash](https://git-scm.com/downloads)
- [Maven 3.x](https://maven.apache.org/download.cgi)
- [Postman](https://www.postman.com/)
- [Docker](https://docs.docker.com/get-docker/) - please refer to [Setting up Docker]()

## How to run

### 1. Clone the repository
Please clone the repository by https or ssh (below we use the https method).
```
git clone https://github.com/harshitagupta022/trackexpensesappbackend.git
```

### 2. Run the database
You need a working mysql server to run this application.
You can use your local server installation or use the docker compose file from this project.

**Remember:** if you are using your local server instance, change parameters for the database connection.

To run only mysql server from our docker compose configuration please enter to the directory
```
cd track-expenses-app-backend
```

and run
```
docker-compose up -d database
```
Wait until the database server starts completely, it may take a while.

## Tech stack

### Development
- [Java 17 LTS](https://openjdk.org/projects/jdk/17/)
- [Spring Boot 2](https://spring.io/projects/spring-boot)
- [Spring Data](https://spring.io/projects/spring-data)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger / OpenAPI](https://swagger.io/specification/)
- [MySql (docker)](https://www.mysql.com/)
- [Liquibase](https://www.liquibase.org/)
- [Maven 3.x](https://maven.apache.org/)



