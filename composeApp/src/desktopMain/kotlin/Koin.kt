import di.*
import org.koin.core.context.startKoin

fun initKoin() =

    startKoin {
        modules(
            geminiServiceModule,
            geminiRepositoryModule,
            networkModule,
            useCaseModule,
            viewModelModule
        )
    }
