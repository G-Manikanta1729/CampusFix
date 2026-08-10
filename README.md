# CampusFix - Campus Issue Reporting and Management System

## 📌 Project Overview

CampusFix is a web-based campus issue reporting and management system developed using Spring Boot.

The application provides a platform where students can report problems occurring on campus, such as broken projectors, unclean classrooms, washroom issues, maintenance problems, and other infrastructure-related issues.

The system provides separate access for **Students** and **Administrators**.

Students can:

- Register an account
- Log in securely
- Report campus issues
- View reported issues
- Receive notifications
- Get notified when they report an issue that already exists

Administrators can:

- Log in using an administrator account
- View reported campus issues
- Manage reported issues
- Update issue information/status
- Delete issues when required
- View duplicate-issue notifications
- Mark notifications as read

The application uses **JWT-based authentication and role-based authorization** to protect the different parts of the system.

---

# 🎯 Objectives

The main objectives of CampusFix are:

1. Provide a centralized platform for reporting campus problems.
2. Allow students to submit issues easily.
3. Allow administrators to manage reported issues.
4. Prevent duplicate issue reports.
5. Notify users when a duplicate issue is detected.
6. Provide secure authentication using JWT.
7. Implement role-based authorization for Students and Administrators.
8. Store issue and notification information in a MySQL database.
9. Provide a simple HTML-based user interface using Thymeleaf.
10. Demonstrate the implementation of a complete Spring Boot application.

---

# ✨ Key Features

## 1. User Registration

New users can register through the registration page.

The registration process accepts:

- Username
- Password
- Role

Passwords are encrypted using `BCryptPasswordEncoder` before being stored in the database.

### Registration Flow

```text
User
  ↓
Registration Page
  ↓
RegisterRequest
  ↓
UserController
  ↓
UserService
  ↓
Password Encryption
  ↓
MySQL Database
````

---

# 2. User Authentication

Users can log in using their username and password.

The application uses Spring Security and JWT for authentication.

### Authentication Flow

```text
Username + Password
        ↓
AuthenticationController
        ↓
AuthenticationService
        ↓
AuthenticationManager
        ↓
UserRepository
        ↓
JWT Token Generated
        ↓
AuthenticationResponse
```

After successful authentication, the server returns a JWT token.

The token contains information required to identify and authorize the user.

---

# 3. JWT Authentication

CampusFix uses JSON Web Tokens for stateless authentication.

The application contains a custom:

```text
JwtAuthenticationFilter
```

The filter checks incoming requests for an HTTP `Authorization` header.

Expected format:

```text
Authorization: Bearer <JWT_TOKEN>
```

The filter:

1. Reads the Authorization header.
2. Checks whether it starts with `Bearer`.
3. Extracts the JWT token.
4. Extracts the username.
5. Loads the user details.
6. Validates the token.
7. Places the authenticated user into the Spring Security context.

### JWT Flow

```text
Login
  ↓
Username + Password
  ↓
AuthenticationService
  ↓
JWT Generated
  ↓
Client receives Token
  ↓
Client sends Token with requests
  ↓
JwtAuthenticationFilter
  ↓
Validate JWT
  ↓
Identify User
  ↓
Spring Security Context
```

---

# 4. Role-Based Authorization

CampusFix contains different permissions for different users.

The main roles are:

```text
STUDENT
ADMIN
```

Spring Security is used to restrict access based on roles.

### Student

Students can:

* Access the Student Portal
* Create issues
* View issues
* View their notifications
* Mark permitted notifications as read

### Administrator

Administrators can:

* Access the Admin Portal
* View campus issues
* Manage issues
* Update issues
* Delete issues
* View admin notifications
* Mark notifications as read

---

# 5. Issue Reporting

Students can report a new issue through the Student Portal.

The issue contains information such as:

* Issue ID
* Title
* Location
* Status

Example:

```text
Title: Projector not working
Location: Block A - Room 204
Status: OPEN
```

When a student submits an issue, the request is processed by:

```text
PageController
      ↓
IssueService
      ↓
IssueRepository
      ↓
MySQL Database
```

---

# 6. Duplicate Issue Detection

One of the main features that makes CampusFix different from a simple CRUD application is **duplicate issue detection**.

Before creating a new issue, the application checks whether an existing open issue already has the same:

```text
Title
+
Location
+
OPEN status
```

The repository performs the search using:

```text
findByTitleAndLocationAndStatus(...)
```

### Duplicate Detection Flow

```text
Student reports issue
        ↓
IssueService
        ↓
Check existing OPEN issue
        ↓
       / \
     Yes  No
      ↓    ↓
Duplicate  Create
Detected   New Issue
      ↓
Notification
```

If the issue already exists:

* A new duplicate issue is not created.
* The student receives a notification.
* An administrator receives a notification.
* The response identifies the existing issue.

Example message:

```text
This issue has already been reported.
```

---

# 7. Notification System

CampusFix contains a notification system for communicating important events.

Notifications contain:

* ID
* Recipient
* Message
* Type
* Read status

Example notification:

```text
Message:
This issue has already been reported.

Type:
DUPLICATE_ISSUE

Status:
Unread
```

Notifications are stored in the MySQL database.

---

# 8. Student Notifications

Students can view notifications related to their activities.

The Student Portal displays notifications and their read status.

A notification can be changed from:

```text
Unread
```

to:

```text
Read
```

using the notification read operation.

---

# 9. Admin Notifications

Administrators can view notifications related to campus issue management.

For example, when a student reports an issue that already exists, the administrator receives:

```text
A student reported an already existing issue.
```

The administrator can mark the notification as read.

---

# 10. Notification Read Operation

The application provides the following endpoint:

```text
PUT /notifications/{id}/read
```

When the request is received:

1. The notification is searched by ID.
2. If the notification does not exist, an exception is raised.
3. The notification's `read` value is changed to `true`.
4. The updated notification is saved.
5. A response is returned.

---

# 11. Admin Issue Management

The Admin Portal provides administrators with a list of campus issues.

Administrators can manage reported issues.

The issue management operations include:

### View all issues

```text
GET /issues
```

### View an issue by ID

```text
GET /issues/{id}
```

### Create an issue

```text
POST /issues
```

### Update an issue

```text
PUT /issues/{id}
```

### Delete an issue

```text
DELETE /issues/{id}
```

The application protects administrative operations using Spring Security.

---

# 12. HTML User Interface

CampusFix uses HTML pages with Thymeleaf for the frontend.

The pages are stored inside:

```text
src/main/resources/templates/
```

The main pages include:

```text
index.html
login.html
register.html
student.html
admin.html
```

### Home Page

The home page provides access to the main CampusFix application.

### Login Page

Allows users to authenticate.

### Registration Page

Allows new users to create an account.

### Student Page

Provides:

* Issue reporting form
* Reported issue list
* Notification section

### Admin Page

Provides:

* Campus issue list
* Issue management
* Admin notification list
* Notification read operation

---

# 🏗️ Application Architecture

CampusFix follows a layered Spring Boot architecture.

```text
                    ┌──────────────────────┐
                    │     HTML / Thymeleaf │
                    │       Frontend       │
                    └──────────┬───────────┘
                               │
                               ↓
                    ┌──────────────────────┐
                    │     Controllers      │
                    └──────────┬───────────┘
                               │
                               ↓
                    ┌──────────────────────┐
                    │       Services       │
                    └──────────┬───────────┘
                               │
                               ↓
                    ┌──────────────────────┐
                    │     Repositories     │
                    └──────────┬───────────┘
                               │
                               ↓
                    ┌──────────────────────┐
                    │       MySQL          │
                    │      Database        │
                    └──────────────────────┘
```

Security is applied across the application:

```text
             HTTP Request
                   ↓
        JwtAuthenticationFilter
                   ↓
           Spring Security
                   ↓
        Role-Based Authorization
                   ↓
             Controller
```

---

# 📁 Project Structure

The main project structure is:

```text
campusfix/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── campusfix/
│   │   │           └── campusfix/
│   │   │
│   │   │               ├── config/
│   │   │               │   └── SecurityConfig.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   ├── AuthenticationController.java
│   │   │               │   ├── IssueController.java
│   │   │               │   ├── NotificationController.java
│   │   │               │   ├── PageController.java
│   │   │               │   └── UserController.java
│   │   │               │
│   │   │               ├── dto/
│   │   │               │   ├── AuthenticationRequest.java
│   │   │               │   ├── AuthenticationResponse.java
│   │   │               │   ├── DuplicateIssueResponse.java
│   │   │               │   ├── IssueCreateResponse.java
│   │   │               │   ├── IssueRequest.java
│   │   │               │   ├── IssueResponse.java
│   │   │               │   ├── NotificationResponse.java
│   │   │               │   ├── RegisterRequest.java
│   │   │               │   └── UserResponse.java
│   │   │               │
│   │   │               ├── exception/
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── IssueNotFoundException.java
│   │   │               │   └── NotificationNotFoundException.java
│   │   │               │
│   │   │               ├── filter/
│   │   │               │   └── JwtAuthenticationFilter.java
│   │   │               │
│   │   │               ├── model/
│   │   │               │   ├── Issue.java
│   │   │               │   ├── Notification.java
│   │   │               │   └── User.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── IssueRepository.java
│   │   │               │   ├── NotificationRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               │
│   │   │               └── service/
│   │   │                   ├── AuthenticationService.java
│   │   │                   ├── CustomUserDetailsService.java
│   │   │                   ├── IssueService.java
│   │   │                   ├── JwtService.java
│   │   │                   ├── NotificationService.java
│   │   │                   └── UserService.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       │   ├── index.html
│   │       │   ├── login.html
│   │       │   ├── register.html
│   │       │   ├── student.html
│   │       │   └── admin.html
│   │       │
│   │       └── application.properties
│   │
│   └── test/
│
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

# 🛠️ Technologies Used

| Technology      | Purpose                          |
| --------------- | -------------------------------- |
| Java            | Main programming language        |
| Spring Boot     | Application framework            |
| Spring MVC      | Web and REST controllers         |
| Spring Data JPA | Database access                  |
| Hibernate       | ORM                              |
| Spring Security | Authentication and authorization |
| JWT             | Stateless authentication         |
| BCrypt          | Password encryption              |
| Thymeleaf       | Server-side HTML rendering       |
| MySQL           | Relational database              |
| Maven           | Dependency and build management  |
| HTML            | Frontend structure               |
| IntelliJ IDEA   | Development environment          |
| Git             | Version control                  |
| GitHub          | Source code repository           |

---

# 🗄️ Database

The application uses MySQL.

The database name used by the application is:

```text
campusfix
```

The database connection is configured using:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/campusfix
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
```

Hibernate is configured with:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Therefore, the required tables can be created/updated automatically when the application starts.

---

# 🔐 Security Configuration

CampusFix uses Spring Security with JWT authentication.

The application uses:

```text
BCryptPasswordEncoder
```

for password hashing.

Passwords are **not stored as plain text** in the database.

JWT authentication is handled by:

```text
JwtAuthenticationFilter
```

The security configuration is defined in:

```text
SecurityConfig.java
```

---

# 🔑 Environment Variables

Sensitive information is not stored directly in the source code.

The application expects:

```text
DB_PASSWORD
JWT_SECRET
```

The `application.properties` file contains:

```properties
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
```

Each developer must provide their own values.

### Example

```text
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_generated_secret
```

Do not commit real passwords or JWT secrets to GitHub.

---

# 🚀 Setup Instructions

## Step 1: Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

Move into the project:

```bash
cd campusfix
```

---

## Step 2: Create the Database

Open MySQL and execute:

```sql
CREATE DATABASE campusfix;
```

Verify:

```sql
SHOW DATABASES;
```

---

## Step 3: Configure Database Password

The application does not contain the database password.

Set:

```text
DB_PASSWORD
```

to the password of your local MySQL `root` user.

---

## Step 4: Generate a JWT Secret

Generate a secure random secret.

One option is OpenSSL:

```bash
openssl rand -base64 32
```

Copy the generated value.

Set it as:

```text
JWT_SECRET
```

Do not use another person's JWT secret.

Each installation should use its own secret.

---

# 💻 IntelliJ IDEA Configuration

If you are running the project using IntelliJ IDEA:

1. Open the project.
2. Go to **Run → Edit Configurations**.
3. Select `CampusfixApplication`.
4. Open **Modify options**.
5. Enable **Environment variables**.
6. Add:

```text
DB_PASSWORD=YOUR_MYSQL_PASSWORD;JWT_SECRET=YOUR_GENERATED_JWT_SECRET
```

7. Click **Apply**.
8. Click **OK**.
9. Run `CampusfixApplication`.

---

# ▶️ Running the Application

### Using IntelliJ IDEA

Run:

```text
CampusfixApplication
```

### Using Maven Wrapper on Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Using Maven Wrapper on Linux/macOS

```bash
./mvnw spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

# 🌐 Application Pages

After starting the application, open:

```text
http://localhost:8080
```

Main pages:

| Page           | URL         | Purpose                                   |
| -------------- | ----------- | ----------------------------------------- |
| Home           | `/`         | Application home page                     |
| Login          | `/login`    | User authentication                       |
| Register       | `/register` | User registration                         |
| Student Portal | `/student`  | Student issue reporting and notifications |
| Admin Portal   | `/admin`    | Administrative issue management           |

---

# 🔌 REST API Endpoints

## Authentication

### Authenticate User

```http
POST /authenticate
```

Example request:

```json
{
    "username": "student",
    "password": "password"
}
```

The response contains a JWT token.

---

# User Registration

### Register User

```http
POST /users/register
```

Example:

```json
{
    "username": "student",
    "password": "password",
    "role": "STUDENT"
}
```

---

# Issue APIs

### Get All Issues

```http
GET /issues
```

### Get Issue by ID

```http
GET /issues/{id}
```

Example:

```text
GET /issues/2
```

### Create Issue

```http
POST /issues
```

Example:

```json
{
    "title": "Projector not working",
    "location": "Block A - Room 204"
}
```

### Update Issue

```http
PUT /issues/{id}
```

### Delete Issue

```http
DELETE /issues/{id}
```

Administrative operations are protected using role-based authorization.

---

# Notification APIs

### Get Student Notifications

```http
GET /notifications/student
```

### Get Admin Notifications

```http
GET /notifications/admin
```

### Mark Notification as Read

```http
PUT /notifications/{id}/read
```

Example:

```text
PUT /notifications/2/read
```

---

# 🔒 Authorization Rules

The application protects endpoints according to user roles.

Examples:

```text
Student
    ↓
POST /issues
    ↓
Allowed
```

```text
Student
    ↓
PUT /issues/{id}
    ↓
Denied
```

```text
Admin
    ↓
PUT /issues/{id}
    ↓
Allowed
```

```text
Admin
    ↓
DELETE /issues/{id}
    ↓
Allowed
```

---

# 🧪 Testing the Application

The APIs can be tested using tools such as:

* Postman
* Browser
* IntelliJ IDEA
* MySQL client

A typical testing flow is:

```text
1. Register User
       ↓
2. Authenticate User
       ↓
3. Receive JWT
       ↓
4. Send JWT with protected requests
       ↓
5. Create Issue
       ↓
6. Try Duplicate Issue
       ↓
7. Receive Duplicate Notification
       ↓
8. Login as Admin
       ↓
9. View Issues
       ↓
10. Manage Issue
       ↓
11. View Admin Notification
       ↓
12. Mark Notification as Read
```

---

# 🔄 Complete Application Flow

```text
                    CampusFix
                       │
          ┌────────────┴────────────┐
          │                         │
       Student                    Admin
          │                         │
          ↓                         ↓
     Login/Register              Login
          │                         │
          ↓                         ↓
        JWT                    JWT Authentication
          │                         │
          ↓                         ↓
   Student Portal             Admin Portal
          │                         │
          ↓                         ↓
    Report Issue              View Issues
          │                         │
          ↓                         ↓
    Duplicate Check            Manage Issues
          │                         │
       ┌──┴──┐                    ┌─┴─┐
       │     │                    │   │
     New   Duplicate           Update Delete
       │     │                    │   │
       ↓     ↓                    └─┬─┘
   Database Notification             │
             │                       ↓
             └──────────────→ Database
```

---

# 📚 Concepts Demonstrated

This project demonstrates several important backend and full-stack development concepts.

## Spring Boot

Used to build and run the application.

## Spring MVC

Used to create web pages and REST APIs.

## Dependency Injection

Spring manages application components such as:

* Controllers
* Services
* Repositories
* Security components

## Spring Data JPA

Used to communicate with the MySQL database through repository interfaces.

## Hibernate

Used as the ORM layer for mapping Java objects to database tables.

## REST APIs

The application provides REST endpoints for:

* Authentication
* Users
* Issues
* Notifications

## DTOs

Data Transfer Objects are used to transfer structured data between the client and application.

Examples:

```text
AuthenticationRequest
AuthenticationResponse
IssueRequest
IssueResponse
IssueCreateResponse
NotificationResponse
RegisterRequest
UserResponse
```

## Exception Handling

Custom exceptions are used for situations such as:

```text
IssueNotFoundException
NotificationNotFoundException
```

The application also contains a:

```text
GlobalExceptionHandler
```

for handling exceptions centrally.

## Spring Security

Used for:

* Authentication
* Authorization
* Password encryption
* Role-based access control

## JWT

Used to implement stateless authentication.

## Thymeleaf

Used to connect backend data with HTML pages.

For example:

```html
<tr th:each="issue : ${issues}">
```

allows issue data retrieved from the backend to be displayed dynamically in the HTML page.

---

# 🛡️ Security Best Practices

The project follows several security practices:

### Password Encryption

Passwords are encrypted using:

```text
BCryptPasswordEncoder
```

### JWT Authentication

Protected requests require valid JWT authentication.

### Role-Based Authorization

Different operations are restricted based on user roles.

### Secret Management

Database passwords and JWT secrets are provided through environment variables rather than being stored directly in the source code.

Never commit:

```text
DB_PASSWORD
JWT_SECRET
```

values to GitHub.

---

# ⚠️ Important Notes for Developers

When cloning this project:

1. Install Java.
2. Install MySQL.
3. Create the `campusfix` database.
4. Configure your own MySQL password.
5. Generate your own JWT secret.
6. Set `DB_PASSWORD`.
7. Set `JWT_SECRET`.
8. Start the Spring Boot application.

You do **not** need the original developer's:

```text
MySQL password
JWT secret
```

Each developer should use their own local configuration.

---

# 🔮 Future Enhancements

Possible future improvements include:

* Image upload for reported issues
* Issue priority levels
* Issue categories
* Search and filtering
* Pagination
* Email notifications
* Admin dashboard statistics
* Issue assignment to maintenance staff
* Issue history tracking
* Comments on issues
* Better frontend styling
* Mobile-friendly UI
* Cloud database integration
* Cloud deployment
* Automated testing
* Docker support

---

# 🎓 Learning Outcomes

By completing this project, you can gain practical experience in:

* Building Spring Boot applications
* Creating REST APIs
* Working with Spring Data JPA
* Connecting Spring Boot with MySQL
* Implementing CRUD operations
* Creating DTOs
* Implementing validation
* Handling exceptions
* Implementing JWT authentication
* Implementing Spring Security
* Implementing role-based authorization
* Encrypting passwords
* Creating Thymeleaf pages
* Connecting HTML pages with backend services
* Designing a layered application
* Using Git and GitHub
* Managing application secrets securely

---

# 👨‍💻 Author

**G Sai Kiran**

Bachelor of Technology - Information Technology

CampusFix was developed as a practical full-stack Spring Boot project demonstrating backend development, database integration, authentication, authorization, and server-side HTML rendering.
