package ru.vsu.services

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import ru.vsu.config.DatabaseFactory.dbQuery
import ru.vsu.models.*
import java.time.LocalDateTime

import java.time.LocalDate

class UserService {

    suspend fun createUser(request: RegisterRequest): UserDto? = dbQuery {
        val existingUser = Users.selectAll().where { Users.email eq request.email }.singleOrNull()
        if (existingUser != null) return@dbQuery null

        val passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt())

        val userId = Users.insertAndGetId {
            it[email] = request.email
            it[Users.passwordHash] = passwordHash
            it[createdAt] = LocalDateTime.now()
        }

        // Create profile with initial data
        UserProfiles.insert {
            it[UserProfiles.userId] = userId
            it[name] = request.name
            it[surname] = request.surname
            it[about] = request.about
            request.birthDate?.let { dateStr ->
                try {
                    it[birthDate] = LocalDate.parse(dateStr)
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }

        UserDto(userId.value, request.email, LocalDateTime.now().toString())
    }

    suspend fun authenticate(request: LoginRequest): UserDto? = dbQuery {
        val row = Users.selectAll().where { Users.email eq request.email }.singleOrNull() ?: return@dbQuery null
        
        if (BCrypt.checkpw(request.password, row[Users.passwordHash])) {
             UserDto(
                row[Users.id].value,
                row[Users.email],
                row[Users.createdAt].toString()
            )
        } else {
            null
        }
    }
    
    suspend fun getUserById(id: Long): UserDto? = dbQuery {
        Users.selectAll().where { Users.id eq id }.singleOrNull()?.let {
            UserDto(
                it[Users.id].value,
                it[Users.email],
                it[Users.createdAt].toString()
            )
        }
    }

    suspend fun getUserProfile(userId: Long): UserProfileDto? = dbQuery {
        (UserProfiles innerJoin Users)
            .select(UserProfiles.columns + Users.email)
            .where { UserProfiles.userId eq userId }
            .singleOrNull()?.let {
                UserProfileDto(
                    userId,
                    it[Users.email],
                    it[UserProfiles.name],
                    it[UserProfiles.surname],
                    it[UserProfiles.birthDate]?.toString(),
                    it[UserProfiles.about],
                    it[UserProfiles.avatarUrl]
                )
            }
    }

    suspend fun updateUserProfile(userId: Long, request: UpdateProfileRequest): UserProfileDto? = dbQuery {
        UserProfiles.update({ UserProfiles.userId eq userId }) {
            request.name?.let { name -> it[UserProfiles.name] = name }
            request.surname?.let { surname -> it[UserProfiles.surname] = surname }
            request.birthDate?.let { dateStr -> 
                try {
                    it[UserProfiles.birthDate] = LocalDate.parse(dateStr)
                } catch (e: Exception) {
                    // Ignore invalid date format for prototype
                }
            }
            request.about?.let { about -> it[UserProfiles.about] = about }
            request.avatarUrl?.let { avatarUrl -> it[UserProfiles.avatarUrl] = avatarUrl }
        }
        // Return full profile including email
        val userEmail = Users.select(Users.email).where { Users.id eq userId }.single()[Users.email]
        val profile = UserProfiles.select(UserProfiles.columns).where { UserProfiles.userId eq userId }.single()
        
        UserProfileDto(
            userId,
            userEmail,
            profile[UserProfiles.name],
            profile[UserProfiles.surname],
            profile[UserProfiles.birthDate]?.toString(),
            profile[UserProfiles.about],
            profile[UserProfiles.avatarUrl]
        )
    }
}
