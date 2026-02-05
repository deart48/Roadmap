package ru.vsu.config

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import ru.vsu.models.*
import java.time.LocalDateTime

object DatabaseSeeder {
    fun seed() {
        transaction {
            if (Users.selectAll().count() > 0L) return@transaction

            println("Seeding database with test data...")

            // 1. Create Users (Password: "password")
            val passwordHash = BCrypt.hashpw("password", BCrypt.gensalt())
            
            val aliceId = Users.insertAndGetId {
                it[email] = "alice@example.com"
                it[Users.passwordHash] = passwordHash
                it[createdAt] = LocalDateTime.now()
            }
            
            UserProfiles.insert {
                it[userId] = aliceId.value
                it[name] = "Alice Wonderland"
                it[about] = "Senior Android Developer"
                it[avatarUrl] = "https://ui-avatars.com/api/?name=Alice+Wonderland"
            }

            val bobId = Users.insertAndGetId {
                it[email] = "bob@example.com"
                it[Users.passwordHash] = passwordHash
                it[createdAt] = LocalDateTime.now()
            }

            UserProfiles.insert {
                it[userId] = bobId.value
                it[name] = "Bob Builder"
                it[about] = "Fullstack Enthusiast"
                it[avatarUrl] = "https://ui-avatars.com/api/?name=Bob+Builder"
            }

            // 2. Create Roadmaps
            val androidRoadmapId = Roadmaps.insertAndGetId {
                it[title] = "Android Developer"
                it[description] = "Become a professional Android developer using Kotlin."
                it[imageUrl] = "https://upload.wikimedia.org/wikipedia/commons/d/d7/Android_robot.svg"
                it[createdAt] = LocalDateTime.now()
            }

            val backendRoadmapId = Roadmaps.insertAndGetId {
                it[title] = "Kotlin Backend"
                it[description] = "Master server-side development with Ktor and Exposed."
                it[imageUrl] = "https://resources.jetbrains.com/storage/products/ktor/img/ktor_logo.svg"
                it[createdAt] = LocalDateTime.now()
            }

            // 3. Create Steps for Android Roadmap
            val step1 = RoadmapSteps.insertAndGetId {
                it[roadmapId] = androidRoadmapId.value
                it[title] = "Kotlin Basics"
                it[description] = "Learn the fundamentals: Variables, Control Flow, Functions."
                it[orderIndex] = 1
            }
            
            StepLinks.insert {
                it[stepId] = step1.value
                it[title] = "Kotlin Docs"
                it[url] = "https://kotlinlang.org/docs/home.html"
            }

            RoadmapSteps.insert {
                it[roadmapId] = androidRoadmapId.value
                it[title] = "Android Studio Setup"
                it[description] = "Install and configure the IDE for development."
                it[orderIndex] = 2
            }
            
            RoadmapSteps.insert {
                it[roadmapId] = androidRoadmapId.value
                it[title] = "Jetpack Compose"
                it[description] = "Modern UI toolkit for Android."
                it[orderIndex] = 3
            }

            // 4. Create Steps for Backend Roadmap
            RoadmapSteps.insert {
                it[roadmapId] = backendRoadmapId.value
                it[title] = "Ktor Basics"
                it[description] = "Routing, Plugins, and Application Structure."
                it[orderIndex] = 1
            }

            RoadmapSteps.insert {
                it[roadmapId] = backendRoadmapId.value
                it[title] = "Database with Exposed"
                it[description] = "Connecting to PostgreSQL."
                it[orderIndex] = 2
            }

            // 5. Favorites
            Favorites.insert {
                it[userId] = bobId.value
                it[roadmapId] = androidRoadmapId.value
                it[Favorites.createdAt] = LocalDateTime.now()
            }

            println("Database seeded successfully!")
        }
    }
}
