package di

import AppCoroutineDispatchersImpl
import org.koin.dsl.module
import screens.chatscreen.ChatViewModel
import screens.main.MainViewModel
import utils.AppCoroutineDispatchers

val viewModelModule = module {
    single<AppCoroutineDispatchers> { AppCoroutineDispatchersImpl() }
    single { MainViewModel() }
    single { ChatViewModel(get()) }
}