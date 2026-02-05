# Backend for Mobile App (Kotlin + Ktor)

This is a REST API backend built with Kotlin, Ktor, Exposed (PostgreSQL), and JWT Authentication. It supports storing images in Yandex Cloud Storage (S3).

## Prerequisites
- Java 17+
- PostgreSQL
- Yandex Cloud Object Storage (or any S3 compatible storage)

## Configuration
The application is configured via Environment Variables. You can set them in your IDE run configuration or in a `.env` file (if you use a loader) or export them in your shell.

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/postgres` | JDBC URL for PostgreSQL |
| `DB_USER` | `postgres` | Database User |
| `DB_PASSWORD` | `postgres` | Database Password |
| `JWT_SECRET` | `secret` | Secret key for signing JWTs |
| `S3_ENDPOINT` | `https://storage.yandexcloud.net` | S3 Endpoint URL |
| `S3_REGION` | `ru-central1` | S3 Region |
| `S3_ACCESS_KEY` | `access_key` | S3 Access Key ID |
| `S3_SECRET_KEY` | `secret_key` | S3 Secret Access Key |
| `S3_BUCKET_NAME` | `bucket_name` | S3 Bucket Name |

## Running the Application
```bash
./gradlew run
```

## API Endpoints

### Auth
- `POST /auth/register` - { email, password }
- `POST /auth/login` - { email, password }

### User
- `GET /profile` - Get current user profile (Auth required)
- `PUT /profile` - Update profile (Auth required)
- `POST /profile/upload` - Upload avatar/image (Multipart)

### Roadmaps
- `GET /roadmaps` - List all roadmaps
- `GET /roadmaps/{id}` - Get roadmap details
- `GET /roadmaps/{id}/steps` - Get steps for a roadmap
- `GET /roadmaps/favorites` - Get user favorites (Auth required)
- `POST /roadmaps/{id}/favorite` - Toggle favorite (Auth required)
- `GET /roadmaps/{id}/progress` - Get user progress (Auth required)
- `POST /roadmaps/steps/{stepId}/complete` - Mark step as completed (Auth required)
