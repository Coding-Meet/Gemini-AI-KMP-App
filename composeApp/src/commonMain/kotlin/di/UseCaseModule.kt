package di


import domain.use_cases.GetContentUseCase
import domain.use_cases.IGetContentUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single<IGetContentUseCase> { GetContentUseCase(get()) }
}