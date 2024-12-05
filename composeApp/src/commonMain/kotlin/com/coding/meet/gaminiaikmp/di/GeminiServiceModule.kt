package com.coding.meet.gaminiaikmp.di


import com.coding.meet.gaminiaikmp.data.network.GeminiService
import com.coding.meet.gaminiaikmp.data.network.GeminiServiceImp
import org.koin.dsl.module

val geminiServiceModule = module {
    single<GeminiService> { GeminiServiceImp(get()) }
}
