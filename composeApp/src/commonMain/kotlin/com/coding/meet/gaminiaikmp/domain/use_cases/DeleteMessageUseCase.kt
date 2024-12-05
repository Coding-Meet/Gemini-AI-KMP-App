package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class DeleteMessageUseCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun deleteAllMessage(
        groupId: String
    ) {
        geminiRepository.deleteAllMessage(groupId)
    }

}