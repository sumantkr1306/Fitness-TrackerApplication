# Fitness Management Backend API

A **Spring Boot-based RESTful backend application** for managing fitness-related data.
The project demonstrates secure API development using **JWT authentication, role-based access control (RBAC), and modern backend practices**.

---

## 🚀 Features

* RESTful API built with **Spring Boot**
* **JWT Authentication & Authorization**
* **Role-Based Access Control (RBAC)**
* **DTO & Builder Pattern implementation**
* **Spring Security integration**
* **Password encryption using BCrypt**
* **Database integration with JPA/Hibernate**
* **Input validation**
* **API documentation using Swagger**
* **Docker containerization**
* **Cloud deployment ready**
* Clean code using **Lombok**

---

## 🛠 Tech Stack

* **Java**
* **Spring Boot**
* **Spring Security**
* **JWT**
* **JPA / Hibernate**
* **MySQL / PostgreSQL**
* **Docker**
* **Swagger (OpenAPI)**
* **Lombok**
* **Maven**

---

## 📂 Project Structure

```
fitness-monolith
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.project.fitness
│   │   │       ├── controller
│   │   │       ├── service
│   │   │       ├── repository
│   │   │       ├── security
│   │   │       └── dto
│   │   └── resources
│   │       └── application.properties
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## ⚙️ Environment Variables

The application uses environment variables for database configuration:

```
DB_URL=your_database_url
DB_USER=your_database_username
DB_PWD=your_database_password
```

Example in `application.properties`:

```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PWD}
```

---

## ▶️ Running the Application

### 1. Clone the Repository

```
git clone https://github.com/sumantkr/fitness-monolith.git
cd fitness-monolith
```

### 2. Run using Maven

```
./mvnw spring-boot:run
```

For Windows:

```
.\mvnw.cmd spring-boot:run
```

---

## 🐳 Running with Docker

Build the Docker image:

```
docker build -t fitness-monolith .
```

Run the container:

```
docker run -p 8080:8080 fitness-monolith
```

---

## 📄 API Documentation

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## 🔐 Security Features

* JWT-based authentication
* Secure password hashing with BCrypt
* Role-based authorization
* Protected API endpoints

---

## 👨‍💻 Author

**Sumant Kumar**

GitHub:
https://github.com/sumantkr1306

---

## 📜 License

This project is for **learning and educational purposes**.
