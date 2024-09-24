package di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import presentation.screens.chatscreen.ChatViewModel
import presentation.screens.mainscreen.MainViewModel
import utils.AppCoroutineDispatchers

val viewModelModule = module {
    single<AppCoroutineDispatchers> { AppCoroutineDispatchersImpl() }
    factoryOf(::MainViewModel)
    factoryOf(::ChatViewModel)
}