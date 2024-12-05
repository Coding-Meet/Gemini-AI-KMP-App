package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class GetAllMessageByGroupIdUserCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun getMessageListByGroupId(groupId:String): List<ChatMessage> {
        return geminiRepository.getMessageListByGroupId(groupId)
    }

}