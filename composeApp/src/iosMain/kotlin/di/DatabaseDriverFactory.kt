package di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.coding.meet.gaminiaikmp.GeminiApiChatDB

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return NativeSqliteDriver(GeminiApiChatDB.Schema.synchronous(), "GeminiApiChatDB.db")
    }
}