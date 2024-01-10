package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import theme.borderColor
import theme.lightBackgroundColor
import utils.menuItems
import viewmodels.MainViewModel


@Composable
fun DetailScreen(viewModel: MainViewModel) {
    Column(
        Modifier.fillMaxHeight().background(lightBackgroundColor)
            .border(2.dp, borderColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserBarLayout(viewModel, menuItems[0]) {
            LazyColumn(
                Modifier.fillMaxSize().padding(it).background(lightBackgroundColor)
            ) {

            }
        }
    }
}