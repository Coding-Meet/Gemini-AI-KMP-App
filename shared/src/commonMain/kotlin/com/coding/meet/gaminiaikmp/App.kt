package com.coding.meet.gaminiaikmp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.coding.meet.gaminiaikmp.presentation.navigation.AppNavigation
import com.coding.meet.gaminiaikmp.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    appViewModel: AppViewModel = koinViewModel()
) {
    val themeMode by appViewModel.themeMode.collectAsState()

    AppTheme(themeMode = themeMode) {
        AppNavigation()
    }
}
