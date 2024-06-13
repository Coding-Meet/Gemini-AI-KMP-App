package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.coding.meet.gaminiaikmp.BuildKonfig
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import java.io.File

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        if (BuildKonfig.isDebug) {
            return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + "GeminiApiChatDB.db").apply {
                if (!File("GeminiApiChatDB.db").exists()) {
                    GeminiApiChatDB.Schema.create(this).await()
                }
            }
        } else {
            val parentFolder = File(System.getProperty("user.home") + "/Gemini-AI-KMP-App")
            if (!parentFolder.exists()) {
                parentFolder.mkdirs()
            }
            val databasePath = File(parentFolder, "GeminiApiChatDB.db")
            return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + databasePath.absolutePath).also { driver ->
                if (!databasePath.exists()) {
                    GeminiApiChatDB.Schema.create(driver = driver).await()
                }
            }
        }
    }
}