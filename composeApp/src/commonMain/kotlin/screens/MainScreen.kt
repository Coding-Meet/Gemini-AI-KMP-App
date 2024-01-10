package screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import desktopweb.SideScreenDesktop
import mobile.SideScreenMobile
import org.jetbrains.compose.resources.*
import theme.*
import utils.Screens
import utils.TYPE
import utils.menuItems
import viewmodels.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val sideScreen = if (viewModel.allPlatform == TYPE.MOBILE) {
        SideScreenMobile(viewModel)
    } else {
        SideScreenDesktop(viewModel)
    }
    when (viewModel.screens) {
        Screens.MAIN -> {
            sideScreen.SideRow {
                TopBarLayout {
                    LazyColumn(
                        Modifier.fillMaxSize().padding(it).background(lightBackgroundColor)
                    ) {
                        item {
                            TextField(
                                value = viewModel.searchText,
                                onValueChange = { viewModel.searchText = it },
                                label = { Text("Search") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Search, contentDescription = "search",
                                        tint = whiteColor
                                    )
                                },
                                trailingIcon = {
                                    if (viewModel.searchText.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.searchText = "" }) {
                                            Icon(
                                                Icons.Filled.Close, contentDescription = "close",
                                                tint = whiteColor
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(10.dp).background(borderColor),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = borderColor,
                                    unfocusedContainerColor = borderColor,
                                    focusedTextColor = whiteColor,
                                    unfocusedTextColor = whiteColor,
                                    focusedPlaceholderColor = whiteColor,
                                    unfocusedPlaceholderColor = whiteColor,
                                    unfocusedLabelColor = whiteColor,
                                    focusedLabelColor = whiteColor,
                                    focusedIndicatorColor = whiteColor,
                                    cursorColor = whiteColor
                                )
                            )
                            Button(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = whiteColor,
                                    contentColor = blackColor,
                                ),
                            ) {
                                Icon(
                                    Icons.Filled.Add, contentDescription = "search",
                                    tint = blackColor
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSize))
                                Text(
                                    text = "New Chat",
                                )
                            }
                        }
                        items(menuItems) { menuItem ->
                            UserLayout(menuItem) {
                                if (viewModel.allPlatform == TYPE.MOBILE) {
                                    viewModel.screens = Screens.DETAIL
                                }
                            }
                        }
                    }
                }

            }
        }

        Screens.DETAIL -> {
            DetailScreen(viewModel)
        }

        Screens.SETTING -> {

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLayout(
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

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
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun UserBarLayout(
    viewModel: MainViewModel,
    menuItem: MenuItem,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = borderColor,
                    titleContentColor = whiteColor,
                ),
                title = {
                    Row(
                        Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource("compose-multiplatform.xml"), null,
                            modifier = Modifier.width(50.dp),
                            colorFilter = ColorFilter.tint(color = whiteColor)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = menuItem.titleResId,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = menuItem.descriptionResId,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "delete",
                            tint = Color.Red
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = borderColor,
                contentColor = whiteColor,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "upload",
                            tint = whiteColor
                        )
                    }
                    TextField(
                        value = viewModel.userText,
                        onValueChange = { viewModel.userText = it },
                        modifier = Modifier.weight(1f)
                            .padding(start = 8.dp)
                            .background(borderColor),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = lightBorderColor,
                            focusedContainerColor = lightBorderColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = whiteColor,
                            unfocusedTextColor = whiteColor,
                            focusedPlaceholderColor = whiteColor,
                            unfocusedPlaceholderColor = whiteColor,
                            unfocusedLabelColor = whiteColor,
                            focusedLabelColor = whiteColor,
                            cursorColor = whiteColor
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "send",
                            tint = whiteColor
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}

data class MenuItem(
    val routeId: String,
    val titleResId: String,
    val descriptionResId: String,
    val icon: String
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun UserLayout(menuItem: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        onClick = {
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = borderColor,
            contentColor = whiteColor
        )
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource("compose-multiplatform.xml"), null,
                modifier = Modifier
                    .weight(0.1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(0.8f)
            ) {
                Text(
                    text = menuItem.titleResId,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = menuItem.descriptionResId,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}