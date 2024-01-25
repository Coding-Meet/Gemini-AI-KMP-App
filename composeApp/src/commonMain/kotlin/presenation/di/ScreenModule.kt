package presenation.di

import org.koin.dsl.module
import presenation.screens.chatscreen.ChatViewModel
import presenation.screens.main.MainViewModel

val screenViewModel = module {
    factory { MainViewModel() }
    factory { ChatViewModel(get()) }

}