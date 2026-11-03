package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.SettingRepository

class SaveApiKeyUseCase(
    private val settingRepository: SettingRepository
) {
    fun saveApiKey(apiKey: String) = settingRepository.saveApiKey(apiKey)
    operator fun invoke(apiKey: String) = settingRepository.saveApiKey(apiKey)
}

