package presentation.commoninterface

import androidx.compose.runtime.Composable

interface DistributeComp {
    @Composable
    fun SideRow(
        content: @Composable () -> Unit,
    )

}