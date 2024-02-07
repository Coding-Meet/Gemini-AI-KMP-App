package di

import org.koin.dsl.module
import presentation.screens.chatscreen.ChatViewModel
import presentation.screens.mainscreen.MainViewModel
import utils.AppCoroutineDispatchers

val viewModelModule = module {
    single<AppCoroutineDispatchers> { AppCoroutineDispatchersImpl() }
    single { MainViewModel(get(),get(),get()) }
    single { ChatViewModel(get(),get(),get(),get(),get(),get(),get()) }
}