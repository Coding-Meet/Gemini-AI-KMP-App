package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent

class ClearAllConversationsUseCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend operator fun invoke() {
        geminiRepository.clearAllConversations()
    }
}

