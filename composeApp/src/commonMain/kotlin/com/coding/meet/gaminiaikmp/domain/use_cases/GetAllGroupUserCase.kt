package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.GeminiRepository
import org.koin.core.component.KoinComponent


class GetAllGroupUserCase(private val geminiRepository: GeminiRepository) : KoinComponent {
    suspend fun  getGroupList() = geminiRepository.getGroupList()

}