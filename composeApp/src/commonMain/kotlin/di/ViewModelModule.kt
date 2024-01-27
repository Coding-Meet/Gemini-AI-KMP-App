package di

import org.koin.dsl.module
import screens.chatscreen.ChatViewModel
import screens.main.MainViewModel

val viewModelModule = module {
    single { ChatViewModel(get()) }
}