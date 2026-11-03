package com.coding.meet.gaminiaikmp.di

import com.coding.meet.gaminiaikmp.AppViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::SettingsViewModel)
}
