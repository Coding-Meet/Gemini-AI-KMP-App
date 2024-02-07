package domain.use_cases

import domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class GetAllGroupUserCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun  getGroupList() = geminiRepository.getGroupList()

}