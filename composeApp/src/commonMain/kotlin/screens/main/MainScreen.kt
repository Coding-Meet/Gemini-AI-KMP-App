package screens.main


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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.DialogProperties
import clipData
import desktopweb.SideScreenDesktop
import kotlinx.coroutines.launch
import mobile.SideScreenMobile
import models.Group
import org.jetbrains.compose.resources.*
import screens.chatscreen.DetailScreen
import theme.*
import utils.*

@Composable
fun MainScreen(viewModel: MainViewModel) {
    ApiKeyAlertDialogBox(viewModel)
    ChatAlertDialogBox(viewModel)
    val sideScreen = if (viewModel.platformType == TYPE.MOBILE) {
        SideScreenMobile()
    } else {
        SideScreenDesktop(viewModel)
    }
    when (viewModel.screens) {
        Screens.MAIN -> {
            sideScreen.SideRow {
                TopBarLayout(viewModel) { paddingValues ->
                    LazyColumn(
                        Modifier.fillMaxSize().padding(paddingValues).background(lightBackgroundColor)
                    ) {
                        item {
                            TextField(
                                value = viewModel.searchText,
                                onValueChange = {
                                    viewModel.searchText = it
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
                            Button(
                                onClick = {
                                    val apiKey = viewModel.getApikeyLocalStorage().trim()
                                    if (apiKey.isNotEmpty()) {
                                        if (apiKey.isValidApiKey()) {
                                            viewModel.isChatShowDialog = true
                                        }else{
                                            viewModel.apiKeyText = apiKey
                                            viewModel.isApiShowDialog = true
                                        }
                                    }else{
                                        viewModel.apiKeyText = apiKey
                                        viewModel.isApiShowDialog = true
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
                                Text(text = "New Chat")
                            }
                        }
                        itemsIndexed(viewModel.groupList) { index, menuItem ->
                            UserLayout(menuItem) {
                                if (viewModel.platformType == TYPE.MOBILE) {
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
    viewModel: MainViewModel,
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
                    IconButton(onClick = {
                        viewModel.apiKeyText = viewModel.getApikeyLocalStorage()
                        viewModel.isApiShowDialog = true
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLayout(group: Group, onClick: () -> Unit) {
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
        UserRow(group, Modifier.padding(10.dp))
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun UserRow(group: Group, customModifier: Modifier = Modifier) {
    Row(
        Modifier
            .fillMaxWidth()
            .then(customModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(group.icon), null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .weight(0.8f)
        ) {
            Text(
                text = group.groupName.capitalizeFirstLetter(),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = group.date,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ApiKeyAlertDialogBox(viewModel: MainViewModel) {
    val coroutine = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    if (viewModel.isApiShowDialog) {
        AlertDialog(properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            icon = {
                Icon(Icons.Default.Settings, contentDescription = "setting")
            },
            containerColor = lightBackgroundColor,
            textContentColor = whiteColor,
            iconContentColor = whiteColor,
            titleContentColor = whiteColor,
            title = {
                Text(
                    text = "Add Api Key"
                )
            },
            text = {
                TextField(
                    value = viewModel.apiKeyText,
                    onValueChange = { viewModel.apiKeyText = it },
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                        .background(borderColor),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    placeholder = {
                        Text("Enter the Api Key", color = textHintColor)
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
                    shape = RoundedCornerShape(10.dp),
                    trailingIcon = {
                        IconButton(onClick = {
                            coroutine.launch {
                                clipData(clipboardManager)?.let {
                                    viewModel.apiKeyText = it
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource("ic_paste.xml"),
                                contentDescription = "api key",
                                tint = whiteColor
                            )
                        }
                    }
                )
            },
            onDismissRequest = {
                viewModel.apiKeyText = ""
                viewModel.isApiShowDialog = false
            },

            confirmButton = {
                Button(
                    onClick = {
                        if (viewModel.apiKeyText.trim().isNotEmpty()) {
                            if (viewModel.apiKeyText.trim().isValidApiKey()) {
                                viewModel.setApikeyLocalStorage(viewModel.apiKeyText.trim())
                                viewModel.apiKeyText = ""
                                viewModel.isApiShowDialog = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.apiKeyText = ""
                        viewModel.isApiShowDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ChatAlertDialogBox(viewModel: MainViewModel) {
    if (viewModel.isChatShowDialog) {
        AlertDialog(properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            icon = {
                Icon(Icons.Default.Person, contentDescription = "robot")
            },
            containerColor = lightBackgroundColor,
            textContentColor = whiteColor,
            iconContentColor = whiteColor,
            titleContentColor = whiteColor,
            title = {
                Text(text = "Add Robot")
            },
            text = {
                TextField(
                    value = viewModel.newGroupText,
                    onValueChange = { viewModel.newGroupText = it },
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
                    shape = RoundedCornerShape(10.dp),
                )
            },
            onDismissRequest = {
                viewModel.newGroupText = ""
                viewModel.isChatShowDialog = false
            },

            confirmButton = {
                Button(
                    onClick = {
                        if (viewModel.newGroupText.trim().isNotEmpty()) {
                            val newGroup = Group(
                                generateRandomKey(),
                                viewModel.newGroupText.trim(),
                                currentDateTimeToString(),
                                "robot_${(1..8).random()}.png"
                            )
                            viewModel.groupList.add(newGroup)
                        }
                        viewModel.newGroupText = ""
                        viewModel.isChatShowDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.newGroupText = ""
                        viewModel.isChatShowDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = whiteColor,
                        contentColor = blackColor,
                    ),
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}