package domain.use_cases

import AppCoroutineDispatchersImpl
import data.DataResult
import data.entity.ChatModel
import data.respository.ChatRepositoryImpl
import domain.GeminiResult
import domain.GeminiResult.*
import domain.model.Message
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import presenation.screens.main.MainViewModel

class SendMessageUseCase(mainViewModel: MainViewModel)  {

    private val appCoroutineDispatchers = AppCoroutineDispatchersImpl()
    private val chatRepository =  ChatRepositoryImpl(mainViewModel,appCoroutineDispatchers)

    data class Param(
        val chat: ChatModel,
        val text: String,
        val list: List<Message>
    )

    suspend operator fun invoke(params: Param) = flow<GeminiResult<List<Message>>> {
        emit(Loading(isLoading = true))
        try {
            chatRepository.generateContent(
                chat = params.chat,
                list = params.list,
                text = params.text
            ).collect { dataResult ->
                when (dataResult) {
                    is DataResult.Error -> emit(Error(dataResult.message))
                    is DataResult.Success -> emit(Success(dataResult.data))
                }
            }
        } catch (e: Exception) {
            emit(Error(e.message))
        }
    }.flowOn(appCoroutineDispatchers.io)



}