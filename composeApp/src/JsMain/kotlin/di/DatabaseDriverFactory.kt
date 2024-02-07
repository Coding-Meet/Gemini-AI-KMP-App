package di

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.w3c.dom.Worker

actual class DatabaseDriverFactory {
    private val mutex = Mutex()
    actual suspend fun createDriver(): SqlDriver {
        @Suppress("UnsafeCastFromDynamic")
        val driver = WebWorkerDriver(
            Worker(js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")),
        )
        mutex.withLock {
            GeminiApiChatDB.Schema.awaitCreate(driver)
            return driver
        }
    }
}