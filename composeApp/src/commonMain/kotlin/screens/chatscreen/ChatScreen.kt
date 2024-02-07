package screens.chatscreen

import ImagePicker
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.DialogProperties
import domain.model.ChatMessage
import domain.model.Role
import domain.model.Group
import kotlinx.coroutines.delay
import org.koin.mp.KoinPlatform
import screens.main.GroupUiState
import screens.main.UserRow
import theme.*
import utils.Screens
import utils.TYPE
import screens.main.MainViewModel
import toComposeImageBitmap


@Composable
fun DetailScreen(mainViewModel: MainViewModel) {
    val chatViewModel: ChatViewModel = KoinPlatform.getKoin().get()

    val chatUiState by chatViewModel.chatUiState.collectAsState()
    val groupUiState by mainViewModel.uiState.collectAsState()
    DeleteChatAlertDialogBox(chatViewModel, mainViewModel)
    Column(
        Modifier.fillMaxHeight().background(lightBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (groupUiState.data.isNotEmpty() && mainViewModel.currentPos != -1) {
            UserBarLayout(
                mainViewModel,
                chatViewModel,
                chatUiState,
                groupUiState,
                groupUiState.data[mainViewModel.currentPos]
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (mainViewModel.platformType != TYPE.MOBILE) {
                        IconButton(
                            onClick = {
                                mainViewModel.isDesktopDrawerOpen = !mainViewModel.isDesktopDrawerOpen
                            },
                            modifier = Modifier.size(30.dp, 80.dp)
                                .background(borderColor, RoundedCornerShape(10))
                                .clip(RoundedCornerShape(10))
                        ) {
                            Icon(
                                imageVector = if (mainViewModel.isDesktopDrawerOpen) {
                                    Icons.Default.KeyboardArrowRight
                                } else {
                                    Icons.Default.KeyboardArrowLeft
                                },
                                tint = whiteColor,
                                contentDescription = "drawer",
                            )
                        }
                    }
                    if (chatUiState.isLoading) {
                        LoadingAnimation(Modifier.fillMaxWidth().wrapContentSize())
                    } else {
                        val lazyListState = rememberLazyListState()
                        LazyColumn(
                            Modifier.fillMaxSize().background(lightBackgroundColor),
                            lazyListState,
                            reverseLayout = true,
                            contentPadding = PaddingValues(horizontal = 10.dp),
                        ) {
                            items(chatUiState.message) {
                                MessageItem(it)
                            }
                        }
                    }
                }
            }
        } else {
            Column {
                Text("Welcome To Gemini Ai Kmp App", color = whiteColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserBarLayout(
    mainViewModel: MainViewModel,
    chatViewModel: ChatViewModel,
    chatUiState: ChatUiState,
    groupUiState: GroupUiState,
    group: Group,
    content: @Composable () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


    Column(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = borderColor,
                titleContentColor = whiteColor,
            ),
            title = {
                UserRow(group)
            },
            actions = {
                IconButton(onClick = {
                    chatViewModel.isDeleteShowDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "delete",
                        tint = Color.Red
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = if (chatUiState.isLoading) Arrangement.Center else Arrangement.Start,
            verticalAlignment = if (chatUiState.isLoading) Alignment.CenterVertically else Alignment.Top,
        ) {
            content()
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth().background(borderColor).padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            items(chatViewModel.imageUris) { imageUri ->
                val bitmap = imageUri.toComposeImageBitmap()
                Box(
                    modifier = Modifier.padding(4.dp)
                        .background(lightBorderColor, RoundedCornerShape(10.dp))
                ) {
                    Image(
                        bitmap,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.padding(4.dp).height(192.dp).clip(RoundedCornerShape(16.dp)),
                    )

                    Icon(Icons.Default.Close,
                        tint = whiteColor,
                        contentDescription = "remove",
                        modifier = Modifier.padding(end = 8.dp).clip(CircleShape)
                            .background(Color.Gray).align(Alignment.TopEnd).clickable {
                                chatViewModel.imageUris.remove(imageUri)
                            })
                }
            }
        }
        BottomTextBar(mainViewModel, chatViewModel, chatUiState, groupUiState)
    }
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = lightBorderColor,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1.0f at 300 with LinearOutSlowInEasing
                        0.0f at 600 with LinearOutSlowInEasing
                        0.0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        circleValues.forEach { value ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        translationY = -value * distance
                    }
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    )
            )
        }
    }

}

@Composable
fun DeleteChatAlertDialogBox(chatViewModel: ChatViewModel, mainViewModel: MainViewModel) {
    if (chatViewModel.isDeleteShowDialog) {
        AlertDialog(
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            icon = {
                Icon(Icons.Default.Delete, contentDescription = "delete")
            },
            containerColor = lightBackgroundColor,
            textContentColor = whiteColor,
            iconContentColor = whiteColor,
            titleContentColor = whiteColor,
            title = {
                Text(text = "Delete Messages")
            },
            text = {
                Text(text = "Are you sure you want to delete all messages?")
            },
            onDismissRequest = {
                chatViewModel.isDeleteShowDialog = false
            },

            confirmButton = {
                Button(
                    onClick = {
                        chatViewModel.deleteAllMessage()
                        chatViewModel.isDeleteShowDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = redColor,
                        contentColor = whiteColor,
                    ),
                ) {
                    Text("Delete All Message")
                }

                Button(
                    onClick = {
                        chatViewModel.deleteGroupWithMessage(){
                            chatViewModel.isDeleteShowDialog = false
                            mainViewModel.currentPos = -1
                            mainViewModel.screens = Screens.MAIN
                            mainViewModel.getGroupList()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = redColor,
                        contentColor = whiteColor,
                    ),
                ) {
                    Text("Delete Group with All Message")
                }

            },
            dismissButton = {
                Button(
                    onClick = {
                        chatViewModel.isDeleteShowDialog = false
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomTextBar(
    viewModel: MainViewModel,
    chatViewModel: ChatViewModel,
    chatUiState: ChatUiState,
    groupUiState: GroupUiState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showFilePicker by remember { mutableStateOf(false) }

    ImagePicker(showFilePicker) {
        it?.let { chatViewModel.imageUris.add(it) }
        showFilePicker = false
    }
    Row(
        modifier = Modifier.background(borderColor).padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            if (!chatUiState.isApiLoading) {
                if (chatViewModel.imageUris.size != 3) {
                    showFilePicker = true
                }
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "upload", tint = whiteColor
            )
        }
        TextField(
            value = chatViewModel.userText,
            onValueChange = { chatViewModel.userText = it },
            modifier = Modifier.weight(1f).background(borderColor),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            maxLines = 3,
            placeholder = {
                Text("Type a message", color = textHintColor)
            },
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
        FloatingActionButton(containerColor = lightBorderColor,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = if (chatUiState.isApiLoading) 0.dp else 6.dp,
                pressedElevation = 0.dp
            ),
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                if (chatViewModel.userText.isNotEmpty()) {
                    chatViewModel.generateContentWithText(
                        groupUiState.data[viewModel.currentPos].groupId,
                        chatViewModel.userText,
                        viewModel.getApikeyLocalStorage(),
                    )
                    keyboardController?.hide()
                    chatViewModel.userText = ""
                }
            }) {
            AnimatedContent(chatUiState.isApiLoading) { generating ->
                if (generating) {
                    CircularProgressIndicator(
                        trackColor = whiteColor,
                        color = lightBorderColor,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "send",
                        tint = whiteColor
                    )
                }
            }
        }

    }
}

@Composable
fun MessageItem(chatMessage: ChatMessage) {

    val isGEMINIMessage = chatMessage.participant != Role.YOU

    val backgroundColor = when (chatMessage.participant) {
        Role.GEMINI -> borderColor
        Role.YOU -> borderColor
        Role.ERROR -> redColor
    }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = EaseOutSine
            )
        ), label = ""
    )
    val horizontalAlignment = if (isGEMINIMessage) {
        Alignment.Start
    } else {
        Alignment.End
    }
    val circleColors = listOf(
        Color(0xFF5851D8),
        Color(0xFF833AB4),
        Color(0xFFC13584),
        Color(0xFFE1306C),
        Color(0xFFFD1D1D),
        Color(0xFFF56040),
        Color(0xFFF77737),
        Color(0xFFFCAF45),
        Color(0xFFFFDC80),
        Color(0xFF5851D8)
    )
    val cardShape = if (isGEMINIMessage) {
        RoundedCornerShape(
            16.dp, 16.dp, 16.dp, 0.dp
        )
    } else {
        RoundedCornerShape(
            16.dp, 16.dp, 0.dp, 16.dp
        )
    }

    val cardPadding = if (isGEMINIMessage) {
        PaddingValues(end = 24.dp)
    } else {
        PaddingValues(start = 24.dp)
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.wrapContentSize().padding(cardPadding)
                .clip(cardShape)
                .padding(2.dp)
                .drawWithContent {
                    rotate(
                        if (chatMessage.isPending) {
                            rotate
                        } else {
                            0f
                        }
                    ) {
                        drawCircle(
                            brush = if (chatMessage.isPending && chatMessage.participant == Role.YOU) {
                                Brush.sweepGradient(
                                    circleColors
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        backgroundColor,
                                        backgroundColor
                                    )
                                )
                            },
                            radius = size.width,
                            blendMode = BlendMode.SrcIn,
                        )
                    }
                    drawContent()
                }.background(backgroundColor, cardShape)
        ) {
            Column(
                modifier = Modifier.wrapContentSize().padding(10.dp)
            ) {
                if (chatMessage.images.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.wrapContentSize().padding(bottom = 4.dp),
                        reverseLayout = true,
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End)
                    ) {
                        items(chatMessage.images) {
                            val bitmap = it.toComposeImageBitmap()
                            Image(
                                bitmap,
                                contentDescription = null,
                                modifier = Modifier.height(192.dp).clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.FillHeight,
                            )
                        }
                    }
                }
                SelectionContainer {
                    Text(
                        text = chatMessage.text,
                        color = whiteColor,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

}
