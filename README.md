# 🛒 E-Commerce Backend REST API

A production-ready RESTful backend for an e-commerce platform built with **Spring Boot**, **Spring Security (JWT)**, **Spring Data JPA**, and **MySQL**.

---

## 🏗️ Architecture

```
Controller Layer  →  Service Layer  →  Repository Layer  →  MySQL Database
     ↕                   ↕
  DTOs / Validation    Business Logic + Transactions
     ↕
  JWT Auth Filter (Spring Security)
```

Follows strict **layered architecture** with separation of concerns: Controllers handle HTTP, Services handle business logic, Repositories handle data access.

---

## ✨ Features

- **JWT-based Authentication** — Stateless auth with role-based access (CUSTOMER / ADMIN)
- **Product Management** — Full CRUD with category filtering and keyword search
- **Cart System** — Per-user cart with item management and live total calculation
- **Order Placement** — Atomic order creation with stock deduction and cart clearing
- **Input Validation** — Bean Validation on all request bodies
- **Global Exception Handling** — Consistent error responses across all endpoints
- **Transaction Management** — JPA transactions ensure data consistency

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security + JWT (jjwt) |
| Database | MySQL 8 |
| Build Tool | Maven |
| Testing | Postman |

---

## 📁 Project Structure

```
src/main/java/com/thameem/ecommerce/
├── config/          # SecurityConfig
├── controller/      # AuthController, ProductController, CartController, OrderController
├── dto/             # Request & Response DTOs
├── entity/          # JPA Entities (User, Product, Cart, CartItem, Order, OrderItem)
├── exception/       # GlobalExceptionHandler, custom exceptions
├── repository/      # JPA Repositories with custom queries
├── security/        # JwtUtils, JwtAuthFilter, UserDetailsServiceImpl
└── service/
    ├── (interfaces)
    └── impl/        # AuthServiceImpl, ProductServiceImpl, CartServiceImpl, OrderServiceImpl
```

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+
- IntelliJ IDEA (recommended)

### 1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/ecommerce-backend.git
cd ecommerce-backend
```

### 2. Configure the database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run the application
```bash
mvn spring-boot:run
```
The server starts at `http://localhost:8080`

---

## 📡 API Endpoints

### Auth
| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register new user |
| POST | `/api/auth/login` | Public | Login and get JWT token |

### Products
| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/products` | Public | List all products |
| GET | `/api/products/{id}` | Public | Get product by ID |
| GET | `/api/products/category/{cat}` | Public | Filter by category |
| GET | `/api/products/search?keyword=` | Public | Search products |
| GET | `/api/products/categories` | Public | List all categories |
| POST | `/api/products` | Admin | Create product |
| PUT | `/api/products/{id}` | Admin | Update product |
| DELETE | `/api/products/{id}` | Admin | Delete product |

### Cart (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/cart` | View cart |
| POST | `/api/cart/add` | Add item to cart |
| PUT | `/api/cart/items/{id}?quantity=` | Update item quantity |
| DELETE | `/api/cart/items/{id}` | Remove item |
| DELETE | `/api/cart/clear` | Clear entire cart |

### Orders (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Place order from cart |
| GET | `/api/orders` | Get my orders |
| GET | `/api/orders/{id}` | Get specific order |
| GET | `/api/orders/admin/all` | (Admin) All orders |
| PUT | `/api/orders/admin/{id}/status` | (Admin) Update status |

---

## 🔐 Authentication

All protected endpoints require the JWT token in the `Authorization` header:
```
Authorization: Bearer <your_token_here>
```

---

## 📊 Entity Relationships

```
User ──< Order ──< OrderItem >── Product
 │                                  │
 └──── Cart ──< CartItem >──────────┘
```

---

## 🧠 Key Design Decisions

1. **Price snapshot on order** — `OrderItem` stores `unitPrice` at time of purchase so future product price changes don't affect past orders.
2. **Auto cart creation** — A cart is automatically created on user registration.
3. **Atomic stock deduction** — Stock is decremented within the same transaction as order creation.
4. **Lazy loading** — All `@OneToMany` / `@ManyToOne` relationships use `FETCH = LAZY` to avoid N+1 queries.

---

## 👨‍💻 Author

**Mohamed Thameem K**  
B.Tech CSE — B.S. Abdur Rahman Crescent Institute of Science and Technology  
[LinkedIn](https://linkedin.com/in/mohamed-thameem-93820b322) • thameemmohamed770@gmail.com
