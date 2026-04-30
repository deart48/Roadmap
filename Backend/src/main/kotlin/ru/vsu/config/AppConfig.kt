package ru.vsu.config

data class AppConfig(
    val dbUrl: String = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/backend",
    val dbUser: String = System.getenv("DB_USER") ?: "deart",
    val dbPassword: String = System.getenv("DB_PASSWORD") ?: "1234",
    
    val jwtSecret: String = System.getenv("JWT_SECRET") ?: "secret",
    val jwtIssuer: String = System.getenv("JWT_ISSUER") ?: "http://0.0.0.0:8080/",
    val jwtAudience: String = System.getenv("JWT_AUDIENCE") ?: "jwt-audience",
    val jwtRealm: String = System.getenv("JWT_REALM") ?: "Access to 'backend'",
    
    val s3Endpoint: String = System.getenv("S3_ENDPOINT") ?: "https://storage.yandexcloud.net",
    val s3Region: String = System.getenv("S3_REGION") ?: "ru-central1",
    val s3AccessKey: String = System.getenv("S3_ACCESS_KEY") ?: "access_key",
    val s3SecretKey: String = System.getenv("S3_SECRET_KEY") ?: "secret_key",
    val s3BucketName: String = System.getenv("S3_BUCKET_NAME") ?: "bucket_name"
)
