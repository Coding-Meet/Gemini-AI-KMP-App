package di


import data.respository.GeminiRepositoryImp
import database.SharedDatabase
import domain.respository.GeminiRepository
import org.koin.dsl.module


val geminiRepositoryModule = module {
    single { SharedDatabase(get()) }
    single<GeminiRepository> { GeminiRepositoryImp(get(), get()) }
}
