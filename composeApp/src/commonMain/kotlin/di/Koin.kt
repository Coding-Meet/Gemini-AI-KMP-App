package di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            geminiServiceModule,
            databaseModule,
            geminiRepositoryModule,
            networkModule,
            useCaseModule,
            viewModelModule,
            platformModule()
        )
    }