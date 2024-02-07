package di


import domain.use_cases.*
import org.koin.dsl.module

val useCaseModule = module {
    single<IGetContentUseCase> { GetContentUseCase(get()) }
    single<IGetAllGroupUserCase> { GetAllGroupUserCase(get()) }
    single<IInsertGroupUseCase> { InsertGroupUseCase(get()) }
    single<IGetAllMessageByGroupIdUserCase> { GetAllMessageByGroupIdUserCase(get()) }
    single<IInsertMessageUseCase> { InsertMessageUseCase(get()) }
    single<IUpdatePendingUseCase> { UpdatePendingUseCase(get()) }
    single<IDeleteGroupWithMessageUseCase> { DeleteGroupWithMessageUseCase(get()) }
    single<IDeleteMessageUseCase> { DeleteMessageUseCase(get()) }
}