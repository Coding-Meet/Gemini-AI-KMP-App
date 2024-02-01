package di


import domain.use_cases.*
import org.koin.dsl.module

val useCaseModule = module {
    single<IGetContentUseCase> { GetContentUseCase(get()) }
}