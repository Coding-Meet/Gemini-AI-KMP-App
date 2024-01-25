package data.respository

import domain.respository.ChatRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<ChatRepository>{ ChatRepositoryImpl(get(),get(), get()) }
}