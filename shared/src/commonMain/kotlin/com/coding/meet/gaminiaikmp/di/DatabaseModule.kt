package com.coding.meet.gaminiaikmp.di

import com.coding.meet.gaminiaikmp.data.database.SharedDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseDriverFactory() }
    single { SharedDatabase(get()) }
}