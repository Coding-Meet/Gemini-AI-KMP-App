package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.SettingRepository

class SaveSelectedModelUseCase(
    private val settingRepository: SettingRepository
) {
    fun saveSelectedModel(modelId: String) = settingRepository.saveSelectedModel(modelId)
    operator fun invoke(modelId: String) = settingRepository.saveSelectedModel(modelId)
}

