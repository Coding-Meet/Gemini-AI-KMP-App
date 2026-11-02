package com.coding.meet.gaminiaikmp.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.coding.meet.gaminiaikmp.GeminiApiChatDB
import com.coding.meet.gaminiaikmp.utils.DB_NAME

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return NativeSqliteDriver(GeminiApiChatDB.Schema.synchronous(), DB_NAME)
    }
}