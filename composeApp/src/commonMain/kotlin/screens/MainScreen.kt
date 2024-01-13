package screens


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import desktopweb.SideScreenDesktop
import mobile.SideScreenMobile
import models.Robot
import org.jetbrains.compose.resources.*
import theme.*
import utils.*
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
                                onValueChange = {
                                    viewModel.searchText = it
                                    viewModel.isAddNewChat = false
                                },
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                                singleLine = true,
                                label = { Text("Search") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Search, contentDescription = "search",
                                        tint = whiteColor
                                    )
                                },
                                trailingIcon = {
                                    if (viewModel.searchText.isNotEmpty()) {
                                        IconButton(onClick = {
                                            viewModel.isAddNewChat = false
                                            viewModel.searchText = ""
                                        }) {
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
                                    cursorColor = whiteColor,
                                    selectionColors = TextSelectionColors(selectionColor, selectionColor)
                                )
                            )
                            if (viewModel.searchText.isEmpty() && viewModel.isAddNewChat) {
                                TextField(
                                    value = viewModel.newChartRobotText,
                                    onValueChange = { viewModel.newChartRobotText = it },
                                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                                        .background(borderColor),
                                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                                    placeholder = {
                                        Text("Enter the Chat Name", color = textHintColor)
                                    },
                                    singleLine = true,
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
                                        cursorColor = whiteColor,
                                        selectionColors = TextSelectionColors(selectionColor, selectionColor)
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                            Button(
                                onClick = {
                                    if (viewModel.isAddNewChat) {
                                        if (viewModel.newChartRobotText.trim().isNotEmpty()) {
                                            val newRobot = Robot(
                                                generateRandomKey(),
                                                viewModel.newChartRobotText.trim(),
                                                currentDateTimeToString(),
                                                "robot_${(1..8).random()}.png"
                                            )
                                            viewModel.robotList.add(newRobot)
                                            viewModel.newChartRobotText = ""
                                            viewModel.isAddNewChat = false
                                        }
                                    } else {
                                        viewModel.isAddNewChat = true
                                        viewModel.searchText = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = whiteColor,
                                    contentColor = blackColor,
                                ),
                            ) {
                                Icon(
                                    Icons.Filled.Add, contentDescription = "add",
                                    tint = blackColor
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSize))
                                Text(
                                    text = if (viewModel.isAddNewChat) "Save" else "New Chat",
                                )
                            }
                        }
                        itemsIndexed(viewModel.robotList) { index,menuItem ->
                            UserLayout(menuItem) {
                                if (viewModel.allPlatform == TYPE.MOBILE) {
                                    viewModel.screens = Screens.DETAIL
                                }
                                viewModel.currentPos = index
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLayout(robot: Robot, onClick: () -> Unit) {
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
        UserRow(robot, Modifier.padding(10.dp))
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun UserRow(robot: Robot, customModifier: Modifier = Modifier) {
    Row(
        Modifier
            .fillMaxWidth()
            .then(customModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(robot.icon), null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(0.8f)
        ) {
            Text(
                text = robot.robotName.capitalizeFirstLetter(),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = robot.date,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}