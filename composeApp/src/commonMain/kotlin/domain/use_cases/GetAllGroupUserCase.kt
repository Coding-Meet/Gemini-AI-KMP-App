package domain.use_cases

import domain.model.Group
import domain.respository.GeminiRepository

interface IGetAllGroupUserCase {
    suspend fun getGroupList(): List<Group>
}

class GetAllGroupUserCase(private val geminiRepository: GeminiRepository) : IGetAllGroupUserCase {
    override suspend fun getGroupList(): List<Group> {
        return geminiRepository.getGroupList()
    }

}