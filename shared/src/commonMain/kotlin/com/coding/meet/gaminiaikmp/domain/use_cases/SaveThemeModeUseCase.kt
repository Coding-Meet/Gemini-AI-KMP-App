package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.SettingRepository
import com.coding.meet.gaminiaikmp.theme.AppThemeMode

class SaveThemeModeUseCase(
    private val settingRepository: SettingRepository
) {
    fun saveThemeMode(themeMode: AppThemeMode) = settingRepository.saveThemeMode(themeMode)
    operator fun invoke(themeMode: AppThemeMode) = settingRepository.saveThemeMode(themeMode)
}

