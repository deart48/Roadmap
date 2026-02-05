package ru.vsu.services

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.net.url.Url
//import aws.smithy.kotlin.runtime.auth.awscredentials.StaticCredentialsProvider
//import aws.smithy.kotlin.runtime.auth.awscredentials.AwsCredentials
import ru.vsu.config.AppConfig
import java.util.UUID

class StorageService(private val config: AppConfig) {

    private suspend fun getClient(): S3Client {
        return S3Client.fromEnvironment {
            region = config.s3Region
            endpointUrl = Url.parse(config.s3Endpoint)

//            credentialsProvider = StaticCredentialsProvider(
//                AwsCredentials(
//                    accessKeyId = config.s3AccessKey,
//                    secretAccessKey = config.s3SecretKey
//                )
//            )

        }
    }

    suspend fun uploadFile(bytes: ByteArray, fileName: String, contentType: String): String {
        val extension = fileName.substringAfterLast('.', "")
        val uniqueName = "${UUID.randomUUID()}.$extension"
        val key = "images/$uniqueName"

        val request = PutObjectRequest {
            bucket = config.s3BucketName
            this.key = key
            body = ByteStream.fromBytes(bytes)
            this.contentType = contentType
        }

        getClient().use { s3 ->
            s3.putObject(request)
        }

        return "${config.s3Endpoint}/${config.s3BucketName}/$key"
    }
}
