# URL Shortener API - Spring Boot Application

## Overview 
A RESTful API for shortening URLs with analytics and user management. Compliant with OpenAPI 3.1.

**Version**: 1.0  
**Author**: Tribhuvan Nath  
**Docs**: `/v3/api-docs`  
**Base URL**: `https://localhost:8080`

---

## API Endpoints

### URL Management Controller

| Method | Endpoint                      | Parameters               | Headers                          | Request Body                                | Description                     |
|--------|-------------------------------|--------------------------|----------------------------------|---------------------------------------------|---------------------------------|
| POST   | `/api/urls/shorten`           | None                     | `Authorization: Bearer {token}`  | `{ "originalUrl": "string" }`               | Generate short URL              |
| POST   | `/api/urls/shorten/custom`    | None                     | `Authorization: Bearer {token}`  | `{ "originalUrl": "string", "customAlias": "string" }` | Create custom alias URL         |
| GET    | `/api/urls/totalClicks`       | None                     | `Authorization: Bearer {token}`  | None                                        | Get user's total clicks         |
| GET    | `/api/urls/myurls`            | None                     | `Authorization: Bearer {token}`  | None                                        | Get all user's URL mappings     |
| GET    | `/api/urls/analytics/{shortUrl}` | `shortUrl` (path)     | `Authorization: Bearer {token}`  | None                                        | Get analytics for short URL     |
| DELETE | `/api/urls/{id}`              | `id` (path)              | `Authorization: Bearer {token}`  | None                                        | Delete URL mapping by ID        |

### Authentication Controller

| Method | Endpoint                   | Parameters | Headers                | Request Body                                | Description         |
|--------|----------------------------|------------|------------------------|---------------------------------------------|---------------------|
| POST   | `/api/auth/public/register`| None       | `Content-Type: application/json` | `{ "username": "string", "password": "string" }` | User registration  |
| POST   | `/api/auth/public/login`   | None       | `Content-Type: application/json` | `{ "username": "string", "password": "string" }` | User login         |
| GET    | `/api/auth/username`       | None       | `Authorization: Bearer {token}`  | None                                        | Get current username|

### Redirect Controller

| Method | Endpoint       | Parameters        | Headers | Request Body | Description              |
|--------|----------------|-------------------|---------|--------------|--------------------------|
| GET    | `/{shortUrl}`  | `shortUrl` (path) | None    | None         | Redirect to original URL |

---

## Schema Definitions

### UrlMappingDTO
| Field         | Type     | Description                     |
|---------------|----------|---------------------------------|
| id            | Long     | Unique identifier               |
| originalUrl   | String   | Original long URL               |
| shortUrl      | String   | Generated short URL             |
| creationDate  | DateTime | Creation timestamp              |
| expirationDate| DateTime | Expiration timestamp (optional) |
| clickCount    | Integer  | Total clicks counter            |

### CustomUrlRequest
| Field       | Type   | Description               |
|-------------|--------|---------------------------|
| originalUrl | String | Original URL to shorten   |
| customAlias | String | Desired custom short path |

### RegisterRequest
| Field     | Type   | Description          |
|-----------|--------|----------------------|
| username  | String | Email/unique ID      |
| password  | String | Minimum 8 characters |

### LoginRequest
| Field     | Type   | Description     |
|-----------|--------|-----------------|
| username  | String | Registered name |
| password  | String | Account password|

### ClickEventDTO
| Field       | Type     | Description                |
|-------------|----------|----------------------------|
| timestamp   | DateTime | Click time                 |
| referrer    | String   | Source website (optional)  |
| userAgent   | String   | Browser/device information |
| ipAddress   | String   | Client IP address          |

---
