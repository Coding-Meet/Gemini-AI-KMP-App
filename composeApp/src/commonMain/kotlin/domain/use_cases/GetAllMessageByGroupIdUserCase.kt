package domain.use_cases

import domain.model.ChatMessage
import domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class GetAllMessageByGroupIdUserCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun getMessageListByGroupId(groupId:String): List<ChatMessage> {
        return geminiRepository.getMessageListByGroupId(groupId)
    }

}