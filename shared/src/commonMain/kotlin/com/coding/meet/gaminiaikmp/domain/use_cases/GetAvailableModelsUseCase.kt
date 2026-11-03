package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.model.GeminiModelInfo
import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent

class GetAvailableModelsUseCase(
    private val geminiRepository: GeminiRepository
) : KoinComponent {
    suspend operator fun invoke(apiKey: String): List<GeminiModelInfo> {
        return geminiRepository.getAvailableModels(apiKey)
    }
}

