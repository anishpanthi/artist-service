# Artist Service

This project is a microservice for managing artists, built with Java and Spring Boot. It includes configurations for Docker and Kubernetes.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Distributed Tracing with OpenTelemetry & Jaeger](#distributed-tracing-with-opentelemetry-jaeger)
- [Distributed Tracing with Grafana Stack (Grafana, Loki, Tempo & Prometheus)](#distributed-tracing-with-grafana-stack)
- [Docker Setup](#docker-setup)
- [Endpoints](#endpoints) WIP...
- [Asynchronous Logging with Logback](#asynchronous-logging-with-logback)

## Prerequisites

- Java 21+
- Gradle
- Docker 
- OIDC Configuration - Make sure to replace with your OIDC configuration in `application.yml` file.

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

## Distributed Tracing with OpenTelemetry & Jaeger

This guide explains how to set up distributed tracing for your application using OpenTelemetry and Jaeger. Follow the steps below to configure and run the tracing.

### Prerequisites

- Docker
- Docker Compose

### Setup

#### 1. Build Configuration

The `build.gradle` file is already provided with the default setup which includes the necessary dependencies for tracing.

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.3.0'
    id 'io.spring.dependency-management' version '1.1.5'
}

.....
.....

dependencies {
    .....
    .....

    implementation 'io.micrometer:micrometer-tracing-bridge-otel'
    implementation 'io.opentelemetry:opentelemetry-exporter-otlp'
    implementation 'net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.5'

    .....
    .....
}

tasks.named('test') {
    useJUnitPlatform()
}

```
#### 2. Docker Setup

The `docker-compose.yml` file is already provided with the default setup which includes the necessary services for tracing. Make sure the `postgres` and `jaeger-tracing` containers are running. You don't need to run rest of the containers for now. Those are required later. Start your docker containers by running the following command:

```yaml
services:
  postgres:
    image: 'postgres:latest'
    container_name: 'postgres'
    restart: unless-stopped
    environment:
      - 'POSTGRES_DB=postgres'
      - 'POSTGRES_PASSWORD=postgres'
      - 'POSTGRES_USER=postgres'
    ports:
      - '5432:5432'

  jaeger-tracing:
    image: 'jaegertracing/all-in-one:latest'
    container_name: 'jaeger'
    restart: unless-stopped
    ports:
      - '16686:16686'
      - '4317:4317'
      - '4318:4318'
      - '5775:5775'
      - '6831:6831/udp'
      - '6832:6832/udp'
      - '5778:5778'
      - '14250:14250'
      - '14268:14268'
      - '14269:14269'
      - '9411:9411'
    environment:
      - COLLECTOR_ZIPKIN_HTTP_PORT=9411
      - COLLECTOR_OTLP_ENABLED=true
```


```bash
docker-compose up -d postgres jaeger-tracing
```
Run the application and generate some traces. Go to http://localhost:16686 for Jaeger dashboard and explore your traces.

## Distributed Tracing with Grafana Stack (Grafana, Loki, Tempo & Prometheus)

This guide explains how to set up distributed tracing for your application using Grafana, Loki, Tempo & Prometheus. Follow the steps below to configure and run the tracing.

### Prerequisites

- Docker
- Docker Compose

### Setup

#### 1. Build Configuration

The `build.gradle` file is already provided with the default setup which includes the necessary dependencies for tracing.

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.3.0'
    id 'io.spring.dependency-management' version '1.1.5'
}

.....
.....

dependencies {
    .....
    .....

    implementation 'io.micrometer:micrometer-tracing-bridge-otel'
    implementation 'io.opentelemetry:opentelemetry-exporter-otlp'
    implementation 'net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.5'

   // For Grafana Usages
    implementation 'io.micrometer:micrometer-registry-prometheus'
    implementation 'com.github.loki4j:loki-logback-appender:1.5.2'
    implementation 'io.zipkin.reporter2:zipkin-reporter-brave'

    .....
    .....
}

tasks.named('test') {
    useJUnitPlatform()
}

```
#### 2. Docker Setup

The `docker-compose.yml` file is already provided with the necessary services for tracing. Make sure the `postgres`, `prometheus`, `grafana`, `loki` & `tempo` containers are running. You can comment the rest.

```yaml
services:
  postgres:
    image: 'postgres:latest'
    container_name: 'postgres'
    restart: unless-stopped
    environment:
      - 'POSTGRES_DB=postgres'
      - 'POSTGRES_PASSWORD=postgres'
      - 'POSTGRES_USER=postgres'
    ports:
      - '5432:5432'

  prometheus:
     image: prom/prometheus:main
     container_name: prometheus
     command:
        - --enable-feature=exemplar-storage
        - --config.file=/etc/prometheus/prometheus.yml
     volumes:
        - ./docker-config/prometheus.yml:/etc/prometheus/prometheus.yml:ro
     ports:
        - "9090:9090"

  grafana:
     image: grafana/grafana:main
     container_name: grafana
     volumes:
        - ./docker-config:/etc/grafana/provisioning/datasources:ro
     environment:
        - GF_AUTH_ANONYMOUS_ENABLED=true
        - GF_AUTH_ANONYMOUS_ORG_ROLE=Admin
        - GF_AUTH_DISABLE_LOGIN_FORM=true
     ports:
        - "3000:3000"

  loki:
     image: grafana/loki:main
     container_name: loki
     command: [ "-config.file=/etc/loki/local-config.yaml" ]
     ports:
        - "3100:3100"

  tempo:
     image: grafana/tempo:latest
     container_name: tempo
     command: [ "-config.file=/etc/tempo.yaml", "-reporting.enabled=false" ]
     volumes:
        - ./docker-config/tempo.yml:/etc/tempo.yaml:ro
        - ./docker-config/tempo-data:/tmp/tempo
     ports:
        - "3110:3100"  # Tempo
        - "9411:9411" # zipkin
```

Start your docker containers by running the following command:
```bash
docker-compose up -d postgres prometheus grafana loki tempo
```
Run the application and generate some traces. Go to http://localhost:3000 for Grafana dashboard and explore your logs and traces. 


# Asynchronous Logging with Logback
Feel free to check out the branch `Enable-Async-Logger-Using-Logback` to see how to enable asynchronous logging using Logback. Make sure to check logback-spring.xml file for the configuration.
