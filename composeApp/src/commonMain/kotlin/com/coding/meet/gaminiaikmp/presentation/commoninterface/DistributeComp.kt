package com.coding.meet.gaminiaikmp.presentation.commoninterface

import androidx.compose.runtime.Composable

interface DistributeComp {
    @Composable
    fun ContentComposable(
        content: @Composable () -> Unit,
    )

}