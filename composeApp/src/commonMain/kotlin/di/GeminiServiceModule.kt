package di


import data.network.GeminiService
import data.network.GeminiServiceImp
import org.koin.dsl.module

val geminiServiceModule = module {
    single<GeminiService> { GeminiServiceImp(get()) }
}
