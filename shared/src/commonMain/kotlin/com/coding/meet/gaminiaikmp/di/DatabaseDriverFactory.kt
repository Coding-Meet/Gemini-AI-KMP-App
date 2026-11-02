package com.coding.meet.gaminiaikmp.di

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    suspend fun createDriver(): SqlDriver
}