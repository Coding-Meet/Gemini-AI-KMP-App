package data.respository

import data.DataResult
import data.entity.ChatModel
import data.entity.GeminiContent
import data.entity.GeminiMessage
import data.entity.generateContent.GeminiGenerateContentRequest
import data.entity.generateContent.GeminiGenerateContentResponse
import data.network.client
import data.toDataResult
import domain.model.Message
import domain.respository.ChatRepository
import io.ktor.client.request.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import utils.AppCoroutineDispatchers
import utils.currentDateTimeToString
import utils.generateRandomKey
import presenation.screens.main.MainViewModel

class ChatRepositoryImpl(
    private val viewModel: MainViewModel,
    private val appCoroutineDispatchers: AppCoroutineDispatchers
) : ChatRepository {
    override suspend fun generateContent(
        chat: ChatModel,
        text: String,
        list: List<Message>
    ) = flow {
        val userMessage = Message(
            chatId = chat.chatId,
            content = text,
            isAiResponse = false,
            messageId = generateRandomKey(),
            timestamp = currentDateTimeToString()
        )
        val newList = list + userMessage
        emit(DataResult.Success(newList))

        val geminiContent = newList.map {
            GeminiContent(
                role = if (it.isAiResponse) "model" else "user",
                parts = listOf(GeminiMessage(it.content))
            )
        }.toMutableList()

        when (val response = client.post {
                url("v1beta/models/gemini-pro:generateContent")
                parameter("key",viewModel.getApikeyLocalStorage())
                setBody(
                    GeminiGenerateContentRequest(
                        contents = geminiContent
                    )
                )
            }.toDataResult<GeminiGenerateContentResponse>()
        ) {
            is DataResult.Error -> emit(DataResult.Error(response.message))
            is DataResult.Success -> {
                val modelMessage = Message(
                    messageId = generateRandomKey(),
                    chatId = chat.chatId,
                    content = response.data.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "",
                    isAiResponse = true,
                    timestamp = ""
                )
//                AppDatabase.chatDao.sendMessage(userMessage)
//                AppDatabase.chatDao.sendMessage(modelMessage)
                emit(DataResult.Success(newList + modelMessage))
            }
        }
    }.flowOn(appCoroutineDispatchers.io)
}