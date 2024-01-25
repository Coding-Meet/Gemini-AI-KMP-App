package domain.use_cases

import data.DataResult
import data.entity.ChatModel
import domain.GeminiResult
import domain.GeminiResult.*
import domain.model.Message
import domain.respository.ChatRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.AppCoroutineDispatchers

class SendMessageUseCase() : KoinComponent {
    private val chatRepository: ChatRepository by inject()
    private val appCoroutineDispatchers: AppCoroutineDispatchers by inject()

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