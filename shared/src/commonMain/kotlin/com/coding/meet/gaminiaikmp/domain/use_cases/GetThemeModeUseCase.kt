package com.coding.meet.gaminiaikmp.domain.use_cases

import com.coding.meet.gaminiaikmp.domain.respository.SettingRepository
import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import kotlinx.coroutines.flow.StateFlow

class GetThemeModeUseCase(
    private val settingRepository: SettingRepository
) {
    val themeMode: StateFlow<AppThemeMode> = settingRepository.themeMode
    fun getThemeMode(): AppThemeMode = settingRepository.getThemeMode()
    operator fun invoke(): AppThemeMode = settingRepository.getThemeMode()
}
