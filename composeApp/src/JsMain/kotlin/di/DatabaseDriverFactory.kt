package di

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import org.w3c.dom.Worker

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        @Suppress("UnsafeCastFromDynamic")
        return WebWorkerDriver(
            Worker(js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")),
        ).also {
            GeminiApiChatDB.Schema.awaitCreate(it)
        }
    }
}