package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent

class UpdatePendingUseCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun updatePending(
        messageId: String,
        isPending: Boolean
    ) {
        geminiRepository.updatePendingStatus(messageId, isPending)
    }

}