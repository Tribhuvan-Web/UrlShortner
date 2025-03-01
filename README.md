# URL Shortener 🔗

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![OpenAPI 3.1](https://img.shields.io/badge/OpenAPI-3.1-green)](https://spec.openapis.org/oas/v3.1.0)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)

A high-performance URL shortening service with analytics and user authentication, built with Spring Boot and OpenAPI 3.1
specification.

## Features ✨

- URL shortening with custom slugs
- Click analytics tracking
- User authentication (JWT)
- RESTful API endpoints
- Swagger/OpenAPI documentation
- User-specific URL management
- Total click statistics
- Redirect tracking system

## Tech Stack 🛠️

- **Backend**: Spring Boot 3.x
- **Security**: Spring Security + JWT
- **Documentation**: OpenAPI 3.1
- **Database**: H2 (Development), MySQL (Production-ready)
- **Testing**: JUnit 5, Mockito

## Installation ⚙️

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+ (Optional)

### Steps

1. Clone the repository:
   ```bash# URL Shortener API 🔗
   https://github.com/Tribhuvan-Web/UrlShortner

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![OpenAPI 3.1](https://img.shields.io/badge/OAS-3.1-success)](https://spec.openapis.org/oas/v3.1.0)

A robust URL shortening service with analytics and authentication capabilities.

**Author**: Tribhuvan Nath  
**API Documentation**: [/v3/api-docs](http://localhost:8080/v3/api-docs)  
**Version**: 1.0

## Features ✨

- URL shortening with custom slugs
- Click analytics tracking
- JWT-based authentication
- User-specific URL management
- Real-time click statistics
- Automatic redirection system

## Servers 🌐

- Local Development: `https://localhost:8080`

## API Endpoints 📍

### URL Mapping APIs

| Method | Endpoint                         | Description                             |
|--------|----------------------------------|-----------------------------------------|
| POST   | `/api/urls/shorten`              | Create short URL from original          |
| GET    | `/api/urls/totalClicks`          | Get total clicks for authenticated user |
| GET    | `/api/urls/myurls`               | Get all URLs created by current user    |
| GET    | `/api/urls/analytics/{shortUrl}` | Get detailed analytics for short URL    |

### Authentication APIs 🔑

| Method | Endpoint                    | Description             |
|--------|-----------------------------|-------------------------|
| POST   | `/api/auth/public/register` | Register new user       |
| POST   | `/api/auth/public/login`    | Login and get JWT token |

### Redirect API ↪️

| Method | Endpoint      | Description              |
|--------|---------------|--------------------------|
| GET    | `/{shortUrl}` | Redirect to original URL |

## Authorization ⚠️

All protected endpoints require JWT token in header:

```http
Authorization: Bearer YOUR_JWT_TOKEN
   git clone
