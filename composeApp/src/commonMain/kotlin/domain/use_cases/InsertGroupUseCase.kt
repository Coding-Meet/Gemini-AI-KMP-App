package domain.use_cases

import domain.respository.GeminiRepository


interface IInsertGroupUseCase {
    suspend fun insertGroup(
        groupId: String,
        groupName: String,
        date: String,
        icon: String
    )
}

class InsertGroupUseCase(private val geminiRepository: GeminiRepository) : IInsertGroupUseCase {
    override suspend fun insertGroup(groupId: String, groupName: String, date: String, icon: String) {
        geminiRepository.insertGroup(groupId, groupName, date, icon)
    }

}