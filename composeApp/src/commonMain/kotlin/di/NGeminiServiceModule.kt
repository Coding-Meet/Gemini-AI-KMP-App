package di


import data.network.NGeminiService
import data.network.NGeminiServiceImp
import org.koin.dsl.module

val nGeminiServiceModule = module {
    single<NGeminiService> { NGeminiServiceImp(get()) }
}
