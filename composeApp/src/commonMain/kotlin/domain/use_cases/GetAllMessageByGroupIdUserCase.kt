package domain.use_cases

import domain.model.ChatMessage
import domain.respository.GeminiRepository

interface IGetAllMessageByGroupIdUserCase {
    suspend fun getMessageListByGroupId(groupId:String): List<ChatMessage>
}

class GetAllMessageByGroupIdUserCase(private val geminiRepository: GeminiRepository) : IGetAllMessageByGroupIdUserCase {
    override suspend fun getMessageListByGroupId(groupId:String): List<ChatMessage> {
        return geminiRepository.getMessageListByGroupId(groupId)
    }

}