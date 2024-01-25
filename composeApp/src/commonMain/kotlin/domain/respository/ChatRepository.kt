package domain.respository

import data.DataResult
import data.entity.ChatModel
import domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun generateContent(
        chat: ChatModel,
        text: String,
        list: List<Message>
    ): Flow<DataResult<List<Message>>>

}