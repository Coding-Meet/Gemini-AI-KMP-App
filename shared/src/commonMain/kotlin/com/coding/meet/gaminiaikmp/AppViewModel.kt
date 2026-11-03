package com.coding.meet.gaminiaikmp

import androidx.lifecycle.ViewModel
import com.coding.meet.gaminiaikmp.domain.use_cases.GetThemeModeUseCase
import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import kotlinx.coroutines.flow.StateFlow

class AppViewModel(
    getThemeModeUseCase: GetThemeModeUseCase
) : ViewModel() {
    val themeMode: StateFlow<AppThemeMode> = getThemeModeUseCase.themeMode
}

