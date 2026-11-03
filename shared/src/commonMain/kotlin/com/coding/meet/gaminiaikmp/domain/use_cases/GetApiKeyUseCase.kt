package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.SettingRepository

class GetApiKeyUseCase(
    private val settingRepository: SettingRepository
) {
    fun getApiKey(): String = settingRepository.getApiKey()
    operator fun invoke(): String = settingRepository.getApiKey()
}

