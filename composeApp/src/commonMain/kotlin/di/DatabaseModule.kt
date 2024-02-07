package di

import data.database.SharedDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { SharedDatabase(get()) }
}