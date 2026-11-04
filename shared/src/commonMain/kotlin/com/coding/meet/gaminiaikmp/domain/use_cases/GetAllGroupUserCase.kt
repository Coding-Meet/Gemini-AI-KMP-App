package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

class GetAllGroupUserCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    fun getGroupListFlow(): Flow<List<Group>> = geminiRepository.getGroupListFlow()
    suspend fun getGroupList(): List<Group> = geminiRepository.getGroupList()
}
