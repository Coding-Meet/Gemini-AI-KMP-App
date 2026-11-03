package com.coding.meet.gaminiaikmp.data.respository

import com.coding.meet.gaminiaikmp.BuildKonfig
import com.coding.meet.gaminiaikmp.domain.respository.SettingRepository
import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import com.coding.meet.gaminiaikmp.utils.GEMINI_PRO
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingRepositoryImp(
    private val settings: Settings
) : SettingRepository {

    private val _themeMode = MutableStateFlow(
        AppThemeMode.fromString(settings.getString(KEY_THEME, AppThemeMode.SYSTEM.name))
    )
    override val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    override fun getApiKey(): String {
        return settings.getString(KEY_API_KEY, BuildKonfig.GEMINI_API_KEY)
    }

    override fun saveApiKey(apiKey: String) {
        settings.putString(KEY_API_KEY, apiKey)
    }

    override fun getSelectedModel(): String {
        return settings.getString(KEY_MODEL, GEMINI_PRO)
    }

    override fun saveSelectedModel(modelId: String) {
        settings.putString(KEY_MODEL, modelId)
    }

    override fun getThemeMode(): AppThemeMode {
        return _themeMode.value
    }

    override fun saveThemeMode(themeMode: AppThemeMode) {
        settings.putString(KEY_THEME, themeMode.name)
        _themeMode.value = themeMode
    }

    companion object {
        private const val KEY_API_KEY = "GEMINI_API_KEY"
        private const val KEY_MODEL = "GEMINI_MODEL"
        private const val KEY_THEME = "APP_THEME"
    }
}
