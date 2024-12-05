package com.coding.meet.gaminiaikmp.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.coding.meet.gaminiaikmp.BuildKonfig
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import com.coding.meet.gaminiaikmp.utils.DB_NAME
import java.io.File

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        if (BuildKonfig.isDebug) {
            return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + DB_NAME).apply {
                if (!File(DB_NAME).exists()) {
                    GeminiApiChatDB.Schema.create(this).await()
                }
            }
        } else {
            val parentFolder = File(System.getProperty("user.home") + "/Gemini-AI-KMP-App")
            if (!parentFolder.exists()) {
                parentFolder.mkdirs()
            }
            val databasePath = File(parentFolder, DB_NAME)
            return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + databasePath.absolutePath).also { driver ->
                if (!databasePath.exists()) {
                    GeminiApiChatDB.Schema.create(driver = driver).await()
                }
            }
        }
    }
}