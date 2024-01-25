package mobile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import commoninterface.DistributeComp
import theme.lightBackgroundColor
import presenation.screens.main.MainViewModel


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