package com.coding.meet.gaminiaikmp.di

import com.coding.meet.gaminiaikmp.data.respository.GeminiRepositoryImp
import com.coding.meet.gaminiaikmp.data.respository.SettingRepositoryImp
import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import com.coding.meet.gaminiaikmp.domain.respository.SettingRepository
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val geminiRepositoryModule = module {
    single<Settings> { Settings() }
    single<SettingRepository> { SettingRepositoryImp(get()) }
    single<GeminiRepository> { GeminiRepositoryImp(get(), get()) }
}
