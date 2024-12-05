package com.coding.meet.gaminiaikmp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainScreen
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel

@Composable
fun App(mainViewModel: MainViewModel) {
    MaterialTheme {
        MainScreen(mainViewModel)
    }
}