package com.coding.meet.gaminiaikmp.di


import com.coding.meet.gaminiaikmp.data.respository.GeminiRepositoryImp
import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import org.koin.dsl.module


val geminiRepositoryModule = module {
    single<GeminiRepository> { GeminiRepositoryImp(get(), get()) }
}
