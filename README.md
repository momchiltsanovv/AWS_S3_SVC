# AWS S3 Service

A Spring Boot microservice for managing file uploads to AWS S3 with PostgreSQL persistence for asset metadata tracking.

## Overview

This service provides RESTful APIs for uploading user profile pictures and item images to AWS S3. It automatically stores asset metadata (user ID, creation timestamp, S3 URL) in a PostgreSQL database, ensuring a complete audit trail of all uploaded files.

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.7
- **Spring Data JPA**: For database operations
- **PostgreSQL**: Relational database for asset metadata
- **AWS SDK for Java v2**: 2.38.1 (S3 client)
- **Lombok**: For reducing boilerplate code
- **Maven**: Build tool

## Features

- **File Upload to S3**: Upload profile pictures and item images to AWS S3 buckets
- **Asset Metadata Persistence**: Automatically saves asset information to PostgreSQL database
- **Public URL Generation**: Returns publicly accessible S3 URLs after upload
- **Admin Asset Listing**: Retrieve all uploaded assets for administrative purposes
- **Transactional Operations**: Ensures data consistency between S3 uploads and database saves
- **Organized Storage**: Files are stored in separate folders (profile-pictures/, item-pictures/)

## API Endpoints

### Base URL
```
http://localhost:8082/api/v1/aws
```

### 1. Upload Profile Picture
**POST** `/upload`

Uploads a user's profile picture to S3 and saves the asset metadata.

**Request:**
- Content-Type: `multipart/form-data`
- Parameters:
  - `user_id` (UUID, required): The user ID
  - `file` (MultipartFile, required): The image file to upload

**Response:**
```json
{
  "URL": "https://nexio-aws-s3.s3.eu-north-1.amazonaws.com/profile-pictures/{userId}.{ext}"
}
```

**Example:**
```bash
curl -X POST "http://localhost:8082/api/v1/aws/upload?user_id=123e4567-e89b-12d3-a456-426614174000" \
  -F "file=@profile.jpg"
```

### 2. Upload Item Picture
**POST** `/upload/item`

Uploads an item image to S3 and saves the asset metadata with the user who uploaded it.

**Request:**
- Content-Type: `multipart/form-data`
- Parameters:
  - `item_id` (UUID, required): The item ID (used for S3 filename)
  - `user_id` (UUID, required): The user ID who uploaded the file (stored as createdBy)
  - `file` (MultipartFile, required): The image file to upload

**Response:**
```json
{
  "URL": "https://nexio-aws-s3.s3.eu-north-1.amazonaws.com/item-pictures/{itemId}.{ext}"
}
```

**Example:**
```bash
curl -X POST "http://localhost:8082/api/v1/aws/upload/item?item_id=123e4567-e89b-12d3-a456-426614174001&user_id=123e4567-e89b-12d3-a456-426614174000" \
  -F "file=@item.jpg"
```

### 3. Get All Assets (Admin)
**GET** `/assets`

Retrieves all uploaded assets from the database.

**Response:**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174002",
    "createdBy": "123e4567-e89b-12d3-a456-426614174000",
    "createdOn": "2024-01-15T10:30:00",
    "awsS3Path": "https://nexio-aws-s3.s3.eu-north-1.amazonaws.com/profile-pictures/user123.jpg"
  },
  ...
]
```

**Example:**
```bash
curl -X GET "http://localhost:8082/api/v1/aws/assets"
```

## Database Schema

### Asset Entity

The `Asset` table stores metadata for all uploaded files:

| Column | Type | Description |
|--------|------|-------------|
| `id` | UUID | Primary key (auto-generated) |
| `created_on` | LocalDateTime | Timestamp when the asset was created (auto-set) |
| `created_by` | UUID | User ID who uploaded the file |
| `aws_s3_path` | String(1000) | Full S3 URL of the uploaded file |

**Entity Class:** `com.nexio.aws_s3_svc.model.Asset`

## Project Structure

```
src/main/java/com/nexio/aws_s3_svc/
├── Application.java                 # Main Spring Boot application
├── config/
│   └── S3Config.java                # AWS S3 client configuration
├── controller/
│   └── S3Controller.java            # REST API endpoints
├── dto/
│   ├── AssetResponse.java          # Response DTO for asset listing
│   └── S3Response.java              # Response DTO for upload operations
├── model/
│   └── Asset.java                   # JPA entity for asset metadata
├── repository/
│   └── AssetRepository.java         # JPA repository interface
└── service/
    ├── AssetService.java            # Service for asset retrieval operations
    └── S3Service.java                # Service for S3 upload and persistence
```

## Configuration

Configuration is managed through `application.yaml`:

### Application Settings
- **Port**: 8082
- **Max File Size**: 30MB
- **Max Request Size**: 30MB

### Database Configuration
- **Driver**: PostgreSQL
- **URL**: `jdbc:postgresql://localhost:5432/aws_db`
- **Username**: postgres
- **Password**: 1234
- **DDL Auto**: update (automatically updates schema)

### AWS Configuration
- **Region**: eu-north-1
- **Bucket Name**: nexio-aws-s3
- **Folders**:
  - Profile pictures: `profile-pictures/`
  - Item pictures: `item-pictures/`

### Important Notes
⚠️ **Security**: The `application.yaml` file contains AWS credentials. In production, these should be moved to environment variables or a secure secrets management system.

## How to Run

### Prerequisites
1. Java 21 installed
2. PostgreSQL database running on localhost:5432
3. AWS S3 bucket created and accessible with provided credentials
4. Maven installed (or use included Maven wrapper)

### Setup Steps

1. **Create PostgreSQL Database:**
   ```sql
   CREATE DATABASE aws_db;
   ```

2. **Update Configuration:**
   - Update `application.yaml` with your database credentials if different
   - Update AWS credentials and bucket name if needed

3. **Build the Project:**
   ```bash
   ./mvnw clean install
   ```

4. **Run the Application:**
   ```bash
   ./mvnw spring-boot:run
   ```

   Or using the JAR:
   ```bash
   java -jar target/AWS_S3_SVC-0.0.1-SNAPSHOT.jar
   ```

5. **Verify the Application:**
   The service will be available at `http://localhost:8082`

## How It Works

### Upload Flow

1. **Client Request**: Client sends multipart file upload request with user/item ID
2. **File Processing**: Service extracts file extension and generates S3 key
3. **S3 Upload**: File is uploaded to AWS S3 with public-read ACL
4. **URL Generation**: Public S3 URL is generated
5. **Database Persistence**: Asset metadata (user ID, timestamp, S3 URL) is saved to PostgreSQL
6. **Response**: S3 URL is returned to the client

### Key Implementation Details

- **Transactional Operations**: Both `upsertProfilePic` and `upsertItemPic` methods are annotated with `@Transactional` to ensure atomicity
- **Automatic Timestamps**: `createdOn` field is automatically set using Hibernate's `@CreationTimestamp`
- **File Naming**: Files are renamed to `{objectId}.{extension}` format (e.g., `userId.jpg`, `itemId.png`)
- **Folder Organization**: Files are organized in separate S3 folders based on type (profile vs item)

## Dependencies

### Core Dependencies
- `spring-boot-starter-web`: Web framework and REST API support
- `spring-boot-starter-data-jpa`: JPA and Hibernate support
- `postgresql`: PostgreSQL JDBC driver
- `lombok`: Code generation for getters, setters, builders
- `s3` (AWS SDK): AWS S3 client library
- `spring-cloud-starter-openfeign`: Feign client support (for microservice communication)

## Development Notes

- The application uses Spring Boot's auto-configuration for JPA
- Database schema is automatically updated on startup (`ddl-auto: update`)
- S3 client is configured as a Spring Bean for dependency injection
- All services use constructor injection for better testability

## License

This project is part of a larger application system.

