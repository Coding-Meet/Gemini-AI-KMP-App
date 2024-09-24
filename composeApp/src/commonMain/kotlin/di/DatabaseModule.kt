package di

import data.database.SharedDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseDriverFactory() }
    single { SharedDatabase(get()) }
}