# FRISTasManager

[![Build Status](https://img.shields.io/travis/your_username/your_repository.svg?style=flat-square)](https://travis-ci.org/your_username/your_repository)

A powerful and efficient task manager backend built with Java, Dropwizard, MySQL, Docker, Terraform, JUnit, Mockito, Hibernate, and Maven.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Tests](#tests)
- [Contributing](#contributing)
- [License](#license)

## Overview

This project is a microservice created with Dropwizard that implements a CRUD REST API for managing tasks. The microservice allows performing basic operations of creating, reading, updating, and deleting tasks. Each task consists of a title and an associated date.

The main goal of this microservice is to provide a simple and efficient interface for task management, enabling users to conveniently create, retrieve, update, and delete tasks. It utilizes a MySQL database in a Docker container to store and retrieve task data.

Some of the technologies and tools used in this project include Java, Dropwizard, MySQL, Docker, JUnit, Mockito, Hibernate, and Maven. The microservice has been developed following good design practices and unit testing to ensure its quality and robustness.

## Features

- CRUD operations: Perform Create, Read, Update, and Delete operations on tasks through the REST API.
- Task management: Create tasks with a title and associated date, allowing for efficient organization and tracking.
- RESTful API: Utilize a well-designed and intuitive API for seamless integration with frontend or other systems.
- Database storage: Persist task data using a MySQL database, ensuring reliable and scalable storage.
- Docker containerization: Run the microservice within a Docker container, enabling easy deployment and portability across environments.
- Terraform deployment: Utilize Terraform to automate the deployment process, making it easier to provision and manage infrastructure resources.
- Testing with JUnit and Mockito: Write unit tests using JUnit and Mockito to ensure the reliability and correctness of the microservice.
- ORM with Hibernate: Utilize Hibernate as the Object-Relational Mapping (ORM) tool to simplify database operations and enhance development productivity.
- Build automation with Maven: Use Maven as the build tool to manage dependencies, compile source code, and generate deployable artifacts.

These features collectively provide a robust and efficient task management solution with a RESTful API, seamless integration, reliable data storage, containerized deployment, infrastructure automation with Terraform, comprehensive testing, and streamlined development processes using Hibernate and Maven.

## Getting Started

### Prerequisites

- Java JDK 8 or higher
- Docker
- Terraform
- Docker compose

### Usage

1. Clone the repository:

```shell
git clone https://github.com/fernandoris/FRISTaskManager.git
```

2. Access to the proyect folder.

```shell
cd FRISTaskManager
```
3. Export environment variables.

```shell
export $(cat .env | xargs)
```
4. Deploy locally.

    - Using Terraform
      1. Access to terraform_local folder
        ```shell
          cd terraform_local
        ```
      2. Init terraform
        ```shell
          terraform init
        ```
      3. Launch containers
        ```shell
          terraform apply -auto-approve
        ```

    - Using docker compose
    ```shell
      docker compose up -d
    ```

5. Use postman or curl to CRUD resources:
   
   Postman collection file ./FRISTaskManager.postman_collection.json
      - Create.
        ```shell
        curl --location --request POST 'http://localhost:8080/task' --header 'Content-Type: application/json' --data '{"tittle" : "New task","date" : "2023-06-17"}'
        ```
      - Read.
        ```shell
        curl --location --request GET 'http://localhost:8080/task'
        ```
        ```shell
        curl --location --request GET 'http://localhost:8080/task/1'
        ```
      - Update.
        ```shell
        curl --location --request PUT 'http://localhost:8080/task/1' --header 'Content-Type: application/json' --data '{"tittle" : "Updated task","date" : "2023-06-17"}'
        ```
      - Delete.
        ```shell
        curl --location --request DELETE 'http://localhost:8080/task/1'
        ```

## Build the project:

   Access to the maven project folder.
   ```shell
   cd FrisTaskManagerBackend
   ```
   Build
   ```shell
   mvn clean install
   ```
   Build docker image. Keep in mind that if the docker user were different to "fernandoris", the terraform and docker compose files would have to be modified so that the application works with the new changes.
   ```shell
   docker build -t ferandoris/fris_task_manager_backend:0.0.1-SNAPSHOT .
   ```
   Publish Docker image.
   ```shell
   docker push fernandoris/fris_task_manager_backend:0.0.1-SNAPSHOT
   ```

## Tests

To run the tests, execute the following command:

```shell
mvn clean test surefire-report:report
```
You will get a report of the test execution in html at the following path: FrisTaskManagerBackend/target/site/surefire-report.html.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open a new issue or submit a pull request.

## License

[MIT License](LICENSE)
