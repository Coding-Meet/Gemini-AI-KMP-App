package com.coding.meet.gaminiaikmp.domain.respository

import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import kotlinx.coroutines.flow.StateFlow

interface SettingRepository {
    val themeMode: StateFlow<AppThemeMode>
    fun getApiKey(): String
    fun saveApiKey(apiKey: String)
    fun getSelectedModel(): String
    fun saveSelectedModel(modelId: String)
    fun getThemeMode(): AppThemeMode
    fun saveThemeMode(themeMode: AppThemeMode)
}
