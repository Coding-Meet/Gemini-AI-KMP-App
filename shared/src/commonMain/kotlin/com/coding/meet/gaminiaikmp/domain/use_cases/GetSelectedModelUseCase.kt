package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.SettingRepository

class GetSelectedModelUseCase(
    private val settingRepository: SettingRepository
) {
    fun getSelectedModel(): String = settingRepository.getSelectedModel()
    operator fun invoke(): String = settingRepository.getSelectedModel()
}

