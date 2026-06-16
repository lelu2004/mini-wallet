# 💳 Mini E-Wallet API

A RESTful API for a mini e-wallet system built with Spring Boot.

## 🛠 Tech Stack

- **Java 24** + **Spring Boot 4.x**
- **Spring Security** + **JWT Authentication**
- **Spring Data JPA** + **MySQL**
- **Lombok**
- **Docker** (MySQL)

## 🗂 Project Structure

src/main/java/com/kim/shin/miniwalletapi/
├── controller/        # REST Controllers
├── service/           # Business Logic
│   └── impl/
├── repository/        # Database Layer
├── entity/            # JPA Entities
├── dto/
│   ├── request/       # Request DTOs
│   └── response/      # Response DTOs
├── enums/             # Enums
├── security/          # JWT, Filter, Config
└── exception/         # Global Exception Handler

## 📐 ERD

| Table           | Description                     |
|-----------------|---------------------------------|
| users           | User account information        |
| wallets         | User wallet with balance        |
| transactions    | Deposit/Withdraw/Transfer logs  |
| refresh_tokens  | JWT Refresh token storage       |
| notifications   | Transaction notifications       |

## 🚀 Getting Started

### Prerequisites
- Java 24
- Maven
- Docker Desktop

### 1. Clone the repository
git clone https://github.com/lelu2004/mini-wallet.git
cd mini-wallet

### 2. Start MySQL with Docker
docker-compose up -d

### 3. Run the application
./mvnw spring-boot:run

The API will be available at: http://localhost:8081

## 🔐 Authentication

This API uses JWT Bearer Token authentication.

**Flow:**
1. Register a new account
2. Login to receive `accessToken` and `refreshToken`
3. Include token in request header:
Authorization: Bearer <accessToken>

4. Use `refreshToken` to get new `accessToken` when expired

## 📡 API Endpoints

### Auth
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/auth/login | Login | ❌ |
| POST | /api/auth/refresh | Refresh access token | ❌ |
| POST | /api/auth/logout/{userId} | Logout | ❌ |

### Users
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/users/register | Register new user | ❌ |

### Wallets
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | /api/wallets/{userId}/balance | Get wallet balance | ✅ |
| POST | /api/wallets/{userId}/deposit | Deposit money | ✅ |
| POST | /api/wallets/{userId}/withdraw | Withdraw money | ✅ |
| POST | /api/wallets/{userId}/transfer | Transfer money | ✅ |
| GET | /api/wallets/{userId}/transactions | Transaction history | ✅ |

## 📦 Request & Response Examples

### Register
**Request:**
POST /api/users/register
{
    "fullName": "Nguyen Van A",
    "email": "vana@gmail.com",
    "password": "123456",
    "phone": "0901234567"
}

**Response:** 201 Created
{
    "id": 1,
    "fullName": "Nguyen Van A",
    "email": "vana@gmail.com",
    "walletId": 1,
    "balance": 0.00
}

### Login
**Request:**
POST /api/auth/login
{
    "email": "vana@gmail.com",
    "password": "123456"
}

**Response:** 200 OK
{
    "accessToken": "eyJhbGc...",
    "refreshToken": "550e8400-e29b...",
    "tokenType": "Bearer",
    "userId": 1,
    "email": "vana@gmail.com"
}

### Deposit
**Request:**
POST /api/wallets/1/deposit
Authorization: Bearer <accessToken>
{
    "amount": 100000,
    "description": "Deposit"
}

**Response:** 200 OK
{
    "id": 1,
    "type": "DEPOSIT",
    "amount": 100000,
    "balanceBefore": 0.00,
    "balanceAfter": 100000.00,
    "status": "SUCCESS"
}
