package domain.di

import domain.use_cases.SendMessageUseCase
import org.koin.dsl.module

val domainModule = module {

    factory { SendMessageUseCase() }

}