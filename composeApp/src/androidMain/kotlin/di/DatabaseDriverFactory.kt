package di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import org.koin.mp.KoinPlatform

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            GeminiApiChatDB.Schema.synchronous(),
            KoinPlatform.getKoin().get(),
            "GeminiApiChatDB.db"
        )
    }
}