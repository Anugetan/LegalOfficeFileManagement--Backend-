# Legal Office Management System - Backend

Backend REST API for the **Legal Office Management System**.

The backend is responsible for user authentication, JWT security, registration approval, legal file management, file workflow, proof of service, and communication with the PostgreSQL database.

---

## 🚀 Project Overview

The Legal Office Management System is designed to help legal offices manage and track legal files throughout their processing workflow.

The backend provides REST APIs for:

- User registration
- User login
- JWT authentication
- Role-based authorization
- Administrator user approval
- User rejection
- Legal file management
- Legal file tracking
- Proof of Service
- Document information
- File status management
- File workflow management
- PostgreSQL database access

---

## 🛠️ Technologies Used

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT
- REST API
- Maven

### Database

- PostgreSQL

### Security

- Spring Security
- JWT Authentication
- BCrypt Password Hashing
- Role-Based Access Control
- Stateless Authentication

### Development Tools

- Eclipse
- Visual Studio Code
- Postman
- Git
- GitHub

---

## 🏗️ Application Architecture

The backend follows a layered architecture.

```text
Angular Frontend
       |
       | HTTP / REST API
       v
Spring Boot Backend
       |
       +----------------------+
       |                      |
       v                      v
 Controllers              Services
       |                      |
       +----------+-----------+
                  |
                  v
             Repositories
                  |
                  v
              PostgreSQL

The main backend layers are:

Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
PostgreSQL
🔐 Authentication

The application uses JWT-based authentication with Spring Security.

Authentication flow:

User
  |
  v
Login
  |
  v
AuthController
  |
  v
AuthService
  |
  v
AuthenticationManager
  |
  v
Spring Security
  |
  v
JWT Token
  |
  v
Angular Frontend
  |
  v
Protected API Requests

The JWT token is sent with protected requests using:

Authorization: Bearer <JWT_TOKEN>
🔑 JWT Security

JWT is used to authenticate users without maintaining server-side sessions.

The backend:

Receives username and password.
Finds the user from PostgreSQL.
Checks the registration status.
Authenticates the credentials using Spring Security.
Generates a JWT token.
Returns the token to the frontend.
Validates the token for protected requests.

JWT expiration is configured in the backend application configuration.

👤 User Registration

New users cannot immediately access the system.

The registration workflow is:

Registration
      |
      v
   PENDING
      |
      v
Administrator Review
      |
      +----------------+
      |                |
      v                v
  APPROVED         REJECTED
      |                |
      v                v
  Can Login        Login Blocked

When a user registers:

role = USER
active = false
registrationStatus = PENDING

The user must be approved by an administrator before logging in.

👨‍💼 Administrator Approval

Administrators can manage registration requests.

Available operations:

View pending registrations
Approve users
Reject users
Get Pending Registrations
GET /api/admin/registration-requests
Approve User
PUT /api/admin/users/{id}/approve

When approved:

registrationStatus = APPROVED
active = true
Reject User
PUT /api/admin/users/{id}/reject

When rejected:

registrationStatus = REJECTED
active = false

Only users with the ADMIN role can access these endpoints.

🛡️ Role-Based Authorization

The system currently supports:

ADMIN
USER

Administrative endpoints are protected by Spring Security.

Example:

.requestMatchers("/api/admin/**").hasRole("ADMIN")

The backend is responsible for enforcing authorization.

Frontend role checking should never be considered the primary security mechanism.

🚫 Registration Status Validation

Before authentication, the backend checks the user's registration status.

Pending
Your registration is still pending administrator approval.
Rejected
You don't have permission to access the Legal Office system.
Your registration request was rejected by the administrator.
Inactive
Your account is currently inactive.

This prevents pending or rejected users from receiving a JWT token.

📁 Legal File Management

The Legal Files module manages legal files and their processing information.

Legal files contain information such as:

Case Number
Date Received
Time Received
Date Completed
Status
SPMS Type
Requesting Office
Document Type
Document Format
Contact Details
Current Stage
Created By
Created Date
Updated Date
📋 Legal File Workflow

Legal files can move through different processing stages.

Example workflow:

RECEIVED
    |
    v
INITIAL REVIEW
    |
    v
PROCESSING
    |
    v
FINAL REVIEW
    |
    v
COMPLETED

The backend stores the current stage and status of each legal file.

📬 Proof of Service

The Proof of Service module manages proof of service information related to legal files.

Proof of Service records are associated with legal files through the backend REST API.

🗄️ Database

The application uses PostgreSQL.

Main database entities include:

users
statuses
spms_types
offices
document_types
logbook_types
document_formats
legal_files
file_actions
file_documents
initial_reviews
file_reviews
final_documents
proof_of_service

The legal_files table acts as the central entity for legal file information.

👤 Users Table

The users table contains authentication and account information.

Important fields include:

id
username
full_name
email
password_hash
role
active
registration_status
created_at

Registration status values:

PENDING
APPROVED
REJECTED

Passwords are stored using BCrypt hashing and are never stored as plain text.

📂 Project Structure

The backend follows a layered Spring Boot structure.

src/
└── main/
    ├── java/
    │   └── com/
    │       └── anuge/
    │           └── legaloffice/
    │               │
    │               ├── config/
    │               │   └── SecurityConfig.java
    │               │
    │               ├── controller/
    │               │   ├── AuthController.java
    │               │   ├── AdminController.java
    │               │   └── ...
    │               │
    │               ├── dto/
    │               │   ├── AuthResponse.java
    │               │   ├── LoginRequest.java
    │               │   └── RegisterRequest.java
    │               │
    │               ├── entity/
    │               │   ├── Users.java
    │               │   ├── LegalFile.java
    │               │   └── ...
    │               │
    │               ├── enums/
    │               │   └── RegistrationStatus.java
    │               │
    │               ├── repository/
    │               │   ├── UserRepository.java
    │               │   ├── LegalFileRepository.java
    │               │   └── ...
    │               │
    │               ├── security/
    │               │   ├── CustomUserDetailsService.java
    │               │   ├── JwtAuthenticationFilter.java
    │               │   └── JwtService.java
    │               │
    │               └── service/
    │                   ├── AuthService.java
    │                   ├── AdminUserService.java
    │                   └── ...
    │
    └── resources/
        └── application.properties
🎯 Main Backend Classes
AuthController

Handles authentication endpoints.

POST /api/auth/register
POST /api/auth/login
AuthService

Responsible for:

User registration
User login
Password encoding
Registration status validation
Authentication
JWT generation
AdminController

Handles administrator registration management.

GET  /api/admin/registration-requests
PUT  /api/admin/users/{id}/approve
PUT  /api/admin/users/{id}/reject
AdminUserService

Responsible for:

Retrieving pending users
Approving users
Rejecting users
Updating registration status
Updating account activation
CustomUserDetailsService

Loads user information from PostgreSQL for Spring Security.

It provides:

Username
Password hash
Role
Account status
JwtAuthenticationFilter

The JWT filter:

Reads the Authorization header.
Extracts the Bearer token.
Extracts the username from the JWT.
Loads the user.
Validates the authentication.
Sets the authenticated user in Spring Security.
JwtService

Responsible for:

Generating JWT tokens
Extracting usernames
Validating JWT information
Managing token expiration
UserRepository

Provides database operations for users.

Examples:

findByUsername()
existsByUsername()
existsByEmail()
findByRegistrationStatus()
🔌 API Endpoints
Authentication
Method	Endpoint	Description
POST	/api/auth/register	Register a new user
POST	/api/auth/login	Login
Administrator
Method	Endpoint	Description
GET	/api/admin/registration-requests	Get pending users
PUT	/api/admin/users/{id}/approve	Approve user
PUT	/api/admin/users/{id}/reject	Reject user

Other protected endpoints require a valid JWT token.

⚙️ Configuration

The backend configuration is stored in:

src/main/resources/application.properties

Example configuration:

spring.datasource.url=jdbc:postgresql://localhost:5432/legal_office
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=YOUR_JWT_SECRET

Do not commit real passwords, database credentials, or JWT secrets to GitHub.

Use environment variables for production credentials.

🌐 CORS

The backend must allow requests from the Angular frontend.

Development:

http://localhost:4200

Production:

YOUR_VERCEL_DOMAIN

CORS configuration should be updated when deploying the frontend.

▶️ Running the Backend Locally
1. Clone the repository
git clone YOUR_GITHUB_REPOSITORY_URL
2. Navigate to the backend project
cd LegalOfficeBackend
3. Build the project

Using Maven:

mvn clean install
4. Run the application
mvn spring-boot:run

The backend will normally run on:

http://localhost:8080
🧪 Testing

API endpoints can be tested using:

Postman
Browser Developer Tools
Angular Frontend

Recommended authentication test:

1. Register a new user
        |
        v
2. User becomes PENDING
        |
        v
3. Login as ADMIN
        |
        v
4. Open registration requests
        |
        v
5. Approve the user
        |
        v
6. Login as approved user
        |
        v
7. JWT token is generated
        |
        v
8. Access protected endpoints

Rejected-user test:

Register
   |
   v
PENDING
   |
   v
ADMIN rejects user
   |
   v
REJECTED
   |
   v
User attempts login
   |
   v
Login blocked
🔒 Security Considerations

The backend uses several security mechanisms:

JWT authentication
Spring Security
BCrypt password hashing
Role-based authorization
Stateless sessions
Protected REST endpoints
Registration approval workflow
Account activation control
Registration status validation

Sensitive information such as passwords and JWT secrets should never be committed to GitHub.

🚀 Production Deployment

The backend can be deployed using Railway.

Typical production architecture:

┌──────────────────────────┐
│          Vercel          │
│                          │
│    Angular Frontend      │
└────────────┬─────────────┘
             |
             | HTTPS / REST API
             v
┌──────────────────────────┐
│         Railway          │
│                          │
│     Spring Boot API      │
└────────────┬─────────────┘
             |
             v
┌──────────────────────────┐
│       PostgreSQL         │
└──────────────────────────┘

The frontend communicates with the deployed Spring Boot API through HTTPS.

📦 Production Build

Create a production build using:

mvn clean package

The generated JAR file will be located in:

target/

Run the generated application using:

java -jar target/YOUR_APPLICATION_NAME.jar
☁️ Railway Deployment

For Railway deployment:

Push the backend project to GitHub.
Create a Railway project.
Connect the GitHub repository.
Configure the PostgreSQL database.
Configure environment variables.
Deploy the Spring Boot application.
Copy the generated backend URL.
Configure the Angular frontend to use the backend URL.
Configure CORS for the Vercel frontend domain.
🔄 Complete System Flow
                    USER
                      |
          +-----------+-----------+
          |                       |
          v                       v
      REGISTER                  LOGIN
          |                       |
          v                       v
       PENDING             Check Registration
          |                       |
          v                 +-----+-----+
    ADMIN REVIEW             |     |     |
          |                  |     |     |
      +---+---+              v     v     v
      |       |           PENDING REJECTED
      v       v                    |
  APPROVE   REJECT                 v
      |       |              LOGIN BLOCKED
      v       v
  APPROVED REJECTED
      |
      v
    LOGIN
      |
      v
 JWT TOKEN
      |
      v
 PROTECTED API
      |
      +------------------+
      |                  |
      v                  v
 LEGAL FILES       PROOF OF SERVICE
      |
      v
 FILE WORKFLOW
      |
      v
  COMPLETED
📄 License

This project is developed for the management of legal office files and related administrative processes.

👨‍💻 Developer

Jonathan Eguna

Software Engineer
