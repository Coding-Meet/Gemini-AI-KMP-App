package di


import data.respository.NGeminiRepositoryImp
import domain.respository.NGeminiRepository
import org.koin.dsl.module


val nGeminiRepositoryModule = module {
    single<NGeminiRepository> { NGeminiRepositoryImp(get()) }
}
