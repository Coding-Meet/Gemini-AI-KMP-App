package com.coding.meet.gaminiaikmp.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import com.coding.meet.gaminiaikmp.utils.DB_NAME
import org.koin.mp.KoinPlatform

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            GeminiApiChatDB.Schema.synchronous(),
            KoinPlatform.getKoin().get(),
            DB_NAME
        )
    }
}