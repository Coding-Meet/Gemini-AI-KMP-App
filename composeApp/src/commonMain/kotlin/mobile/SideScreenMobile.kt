package mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import commoninterface.DistributeComp
import theme.borderColor
import theme.lightBackgroundColor
import viewmodels.MainViewModel


class SideScreenMobile(viewModel: MainViewModel) : DistributeComp {
    @Composable

    override fun SideRow(content: @Composable () -> Unit) {
        Row(
            modifier = Modifier.fillMaxSize().background(lightBackgroundColor)
        ) {
            content()
        }
    }

}