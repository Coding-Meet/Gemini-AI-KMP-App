package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class DeleteGroupWithMessageUseCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun deleteGroupWithMessage(
        groupId: String
    ) {
        geminiRepository.deleteGroupWithMessage(groupId)
    }

}