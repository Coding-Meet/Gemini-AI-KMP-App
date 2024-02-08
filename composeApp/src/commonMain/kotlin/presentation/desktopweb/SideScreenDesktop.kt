package presentation.desktopweb

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import presentation.commoninterface.DistributeComp
import presentation.screens.detailscreen.DetailScreen
import theme.*
import presentation.screens.mainscreen.MainViewModel


class SideScreenDesktop(private val mainViewModel: MainViewModel) : DistributeComp {
    @Composable
    override fun ContentComposable(content: @Composable () -> Unit) {
        Row(
            modifier = Modifier.fillMaxSize().background(lightBackgroundColor)
                .padding(20.dp)
        ) {
            AnimatedVisibility(mainViewModel.isDesktopDrawerOpen,
                Modifier.fillMaxHeight().weight(0.25f),
            ) {
                Column(
                    Modifier.background(lightBackgroundColor).border(2.dp, borderColor),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
            val customModifier = if(mainViewModel.isDesktopDrawerOpen){
                Modifier.fillMaxHeight().weight(0.75f)
            }else{
                Modifier.fillMaxSize()
            }
            Column(
                customModifier.padding(start = 2.dp).background(lightBackgroundColor)
                    .border(2.dp, borderColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreen(mainViewModel)
            }
        }
    }

}
