package ru.vsu.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.vsu.models.*

object DatabaseFactory {
    fun init(config: AppConfig) {
        val driverClassName = "org.postgresql.Driver"
        val jdbcUrl = config.dbUrl
        val database = Database.connect(hikari(config))

        transaction {
            exec("SELECT 1") {
                println("DB OK")
            }
        }
//        transaction(database) {
//            SchemaUtils.createMissingTablesAndColumns(
//                Users,
//                UserProfiles,
//                Roadmaps,
//                RoadmapSteps,
//                StepLinks,
//                Favorites,
//                UserRoadmapProgress,
//                UserStepProgress
//            )
//        }
        println(database)
        println("Ya ebal kotlin")
    }

    private fun hikari(config: AppConfig): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = "org.postgresql.Driver"
        hikariConfig.jdbcUrl = config.dbUrl
        hikariConfig.username = config.dbUser
        hikariConfig.password = config.dbPassword
        hikariConfig.maximumPoolSize = 3
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }
}
