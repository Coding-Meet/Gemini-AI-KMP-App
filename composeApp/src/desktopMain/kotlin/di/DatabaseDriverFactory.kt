package di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import java.io.File

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + "GeminiApiChatDB.db").apply {
            if (!File("GeminiApiChatDB.db").exists()) {
                GeminiApiChatDB.Schema.create(this).await()
            }
        }
    }
}