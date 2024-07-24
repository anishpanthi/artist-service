# Artist Service

This project is a microservice for managing artists, built with Java and Spring Boot. It includes configurations for Docker and Kubernetes.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Docker Setup](#docker-setup)
- [Endpoints](#endpoints)

## Prerequisites

- Java 21+
- Gradle
- Docker (optional)

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/anishpanthi/artist-service.git
    cd artist-service
    ```

2. Build the project:
    ```bash
    ./gradlew build
    ```

## Usage

1. Run the application:
    ```bash
    ./gradlew bootRun
    ```

2. Access the application at `http://localhost:8081`.

## Docker Setup

1. Build the Docker image:
    ```bash
    docker build -t artist-service .
    ```

2. Run the Docker container:
    ```bash
    docker run -p 8080:8080 artist-service
    ```

## Endpoints

- `GET /artists` - Get all artists
- `GET /artists/{id}` - Get artist by ID
- `POST /artists` - Create a new artist
- `PUT /artists/{id}` - Update an artist
- `DELETE /artists/{id}` - Delete an artist
