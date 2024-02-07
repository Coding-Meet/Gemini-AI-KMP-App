package domain.use_cases

import domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent

class UpdatePendingUseCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun updatePending(
        messageId: String,
        isPending: Boolean
    ) {
        geminiRepository.updatePendingStatus(messageId, isPending)
    }

}