package domain.use_cases

import domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class InsertGroupUseCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun insertGroup(groupId: String, groupName: String, date: String, icon: String) {
        geminiRepository.insertGroup(groupId, groupName, date, icon)
    }

}