package di


import data.respository.GeminiRepositoryImp
import domain.respository.GeminiRepository
import org.koin.dsl.module


val geminiRepositoryModule = module {
    single<GeminiRepository> { GeminiRepositoryImp(get(), get()) }
}
