package desktopweb

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import commoninterface.DistributeComp
import screens.DetailScreen
import screens.UserBarLayout
import theme.borderColor
import theme.lightBackgroundColor
import utils.menuItems
import viewmodels.MainViewModel


class SideScreenDesktop(private val viewModel: MainViewModel) : DistributeComp {
    @Composable
    override fun SideRow(content: @Composable () -> Unit) {
        Row(
            modifier = Modifier.fillMaxSize().background(lightBackgroundColor)
                .padding(20.dp).border(2.dp, borderColor).padding(end = 2.dp)
        ) {
            Column(
                Modifier.fillMaxHeight().weight(0.25f).background(lightBackgroundColor).border(2.dp, borderColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }

            Column(
                Modifier.fillMaxHeight().weight(0.75f).padding(start = 2.dp).background(lightBackgroundColor)
                    .border(2.dp, borderColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DetailScreen(viewModel)
            }
        }
    }

}
