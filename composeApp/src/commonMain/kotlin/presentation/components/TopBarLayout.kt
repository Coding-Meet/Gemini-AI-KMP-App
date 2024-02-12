package presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import presentation.screens.mainscreen.MainViewModel
import theme.*
import utils.isValidApiKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLayout(
    mainViewModel: MainViewModel,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                contentColor = whiteColor,
                containerColor = lightBorderColor,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 0.dp
                ),
                onClick = {
                    val apiKey = mainViewModel.getApikeyLocalStorage().trim()
                    if (apiKey.isNotEmpty()) {
                        if (apiKey.isValidApiKey()) {
                            mainViewModel.isNewChatShowDialog = true
                        } else {
                            mainViewModel.apiKeyText = apiKey
                            mainViewModel.isApiShowDialog = true
                        }
                    } else {
                        mainViewModel.apiKeyText = apiKey
                        mainViewModel.isApiShowDialog = true
                    }
                },
                text = {
                    Text(text = "New Group")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "new_group",
                    )
                })
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = borderColor,
                    titleContentColor = whiteColor,
                ),
                title = {
                    Text(
                        "Chats",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                },
                actions = {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = lightBorderColor
                        ),
                        onClick = {
                            mainViewModel.apiKeyText = mainViewModel.getApikeyLocalStorage()
                            mainViewModel.isApiShowDialog = true
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "api key",
                            tint = whiteColor
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}