package com.coding.meet.gaminiaikmp.di

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import kotlin.js.ExperimentalWasmJsInterop
import org.w3c.dom.Worker

@OptIn(ExperimentalWasmJsInterop::class)
private fun createWorker(): Worker =
    js("""new Worker(new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url))""")

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return WebWorkerDriver(
            createWorker(),
        ).also {
            GeminiApiChatDB.Schema.awaitCreate(it)
        }
    }
}