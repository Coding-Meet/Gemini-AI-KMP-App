package com.coding.meet.gaminiaikmp.di

import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import com.coding.meet.gaminiaikmp.utils.AppCoroutineDispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {
    single<AppCoroutineDispatchers> { AppCoroutineDispatchersImpl() }
    factoryOf(::MainViewModel)
    factoryOf(::ChatViewModel)
}