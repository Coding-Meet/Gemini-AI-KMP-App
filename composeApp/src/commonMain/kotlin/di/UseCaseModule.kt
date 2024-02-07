package di


import domain.use_cases.*
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetAllGroupUserCase(get()) }
    factory { GetContentUseCase(get()) }
    factory { InsertGroupUseCase(get()) }
    factory { GetAllMessageByGroupIdUserCase(get()) }
    factory { InsertMessageUseCase(get()) }
    factory { UpdatePendingUseCase(get()) }
    factory { DeleteGroupWithMessageUseCase(get()) }
    factory { DeleteMessageUseCase(get()) }
}