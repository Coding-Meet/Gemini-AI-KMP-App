package desktopweb

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import commoninterface.DistributeComp
import presenation.screens.chatscreen.DetailScreen
import theme.*
import presenation.screens.main.MainViewModel


class SideScreenDesktop(private val viewModel: MainViewModel) : DistributeComp {
    @Composable
    override fun SideRow(content: @Composable () -> Unit) {
        Row(
            modifier = Modifier.fillMaxSize().background(lightBackgroundColor)
                .padding(20.dp)
        ) {
            AnimatedVisibility(viewModel.isDesktopDrawerOpen,
                Modifier.fillMaxHeight().weight(0.25f),
            ) {
                Column(
                    Modifier.background(lightBackgroundColor).border(2.dp, borderColor),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    content()
                }
            }
            val customModifier = if(viewModel.isDesktopDrawerOpen){
                Modifier.fillMaxHeight().weight(0.75f)
            }else{
                Modifier.fillMaxSize()
            }
            Column(
                customModifier.padding(start = 2.dp).background(lightBackgroundColor)
                    .border(2.dp, borderColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreen(viewModel)
            }
        }
    }

}
