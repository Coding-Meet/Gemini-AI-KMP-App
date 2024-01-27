import di.*
import org.koin.core.context.startKoin

fun initKoin() =
    startKoin {
        modules(
            nGeminiServiceModule,
            nGeminiRepositoryModule,
            networkModule,
            useCaseModule,
            viewModelModule
        )
    }
