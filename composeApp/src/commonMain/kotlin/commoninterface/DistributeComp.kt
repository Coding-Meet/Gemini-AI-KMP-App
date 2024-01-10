package commoninterface

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import viewmodels.MainViewModel

interface DistributeComp {
    @Composable
    fun SideRow(
        content: @Composable () -> Unit,
    )

}