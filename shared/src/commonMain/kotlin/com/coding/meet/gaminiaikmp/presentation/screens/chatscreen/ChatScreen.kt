package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.di.toComposeImageBitmap
import com.coding.meet.gaminiaikmp.domain.model.ChatMessage
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.domain.model.Role
import com.coding.meet.gaminiaikmp.presentation.components.AppVerticalScrollbar
import com.coding.meet.gaminiaikmp.presentation.components.LoadingAnimation
import com.coding.meet.gaminiaikmp.presentation.components.SimpleAlertDialog
import com.coding.meet.gaminiaikmp.presentation.components.WelcomeScreen
import com.coding.meet.gaminiaikmp.presentation.preview.AllAdaptivePreviews
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.BottomTextBar
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.DeleteChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.DrawerToggleButton
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.GroupRowLayout
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.MessageItem
import com.coding.meet.gaminiaikmp.utils.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreen(
    groupId: String = "",
    chatViewModel: ChatViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onDeleteSuccess: () -> Unit = {},
    isDesktopDrawerOpen: Boolean = false,
    onToggleDrawer: (() -> Unit)? = null
) {
    val chatUiState by chatViewModel.uiState.collectAsState()

    // Observe ChatUiEffect lifecycle-safely
    chatViewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is ChatUiEffect.NavigateBack -> {
                onDeleteSuccess()
                onBackClick()
            }
            is ChatUiEffect.ShowAlert -> {
                chatViewModel.handleIntent(ChatUiIntent.ShowAlert(effect.title, effect.message))
            }
            else -> Unit
        }
    }

    // Load messages if groupId provided
    LaunchedEffect(groupId) {
        if (groupId.isNotEmpty()) {
            chatViewModel.handleIntent(ChatUiIntent.LoadMessages(groupId, isClicked = true))
        }
    }

    DeleteChatAlertDialog(
        show = chatUiState.isDeleteShowDialog,
        onDismiss = { chatViewModel.handleIntent(ChatUiIntent.DismissDeleteDialog) },
        onConfirmDelete = {
            chatViewModel.handleIntent(ChatUiIntent.DeleteGroupWithMessages(groupId))
        }
    )

    SimpleAlertDialog(
        show = chatUiState.isAlertDialogShow,
        title = chatUiState.alertTitleText,
        message = chatUiState.alertDescText,
        onDismiss = { chatViewModel.handleIntent(ChatUiIntent.DismissAlert) }
    )

    ChatScreenContent(
        chatUiState = chatUiState,
        group = chatUiState.group,
        groupId = if (groupId.isNotEmpty()) groupId else chatUiState.groupId,
        onBackClick = onBackClick,
        isDesktopDrawerOpen = isDesktopDrawerOpen,
        onToggleDrawer = onToggleDrawer,
        lazyListState = chatViewModel.lazyListState,
        onDeleteClick = {
            if (!chatUiState.isApiLoading) {
                if (chatUiState.message.isNotEmpty()) {
                    chatViewModel.handleIntent(ChatUiIntent.OpenDeleteDialog)
                }
            } else {
                chatViewModel.handleIntent(
                    ChatUiIntent.ShowAlert(
                        "API Call in Progress",
                        "Please wait while an API call is currently in progress."
                    )
                )
            }
        },
        onRemoveImage = { chatViewModel.handleIntent(ChatUiIntent.RemoveImage(it)) },
        onMessageChange = { chatViewModel.handleIntent(ChatUiIntent.UpdateInputMessage(it)) },
        onAddImage = { chatViewModel.handleIntent(ChatUiIntent.AddImage(it)) },
        onSendMessage = { chatViewModel.handleIntent(ChatUiIntent.SendMessage(groupId, chatUiState.inputMessage)) },
        onShowAlert = { title, msg -> chatViewModel.handleIntent(ChatUiIntent.ShowAlert(title, msg)) }
    )
}

@Composable
fun ChatScreenContent(
    chatUiState: ChatUiState,
    group: Group?,
    groupId: String,
    onBackClick: () -> Unit = {},
    isDesktopDrawerOpen: Boolean = false,
    onToggleDrawer: (() -> Unit)? = null,
    lazyListState: androidx.compose.foundation.lazy.LazyListState = androidx.compose.foundation.lazy.rememberLazyListState(),
    onDeleteClick: () -> Unit = {},
    onRemoveImage: (ByteArray) -> Unit = {},
    onMessageChange: (String) -> Unit = {},
    onAddImage: (ByteArray) -> Unit = {},
    onSendMessage: () -> Unit = {},
    onShowAlert: (String, String) -> Unit = { _, _ -> }
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val isCompactScreen = !windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Continuous vertical divider separating the list panel and detail panel on wide screens
        if (!isCompactScreen) {
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                TopAppBar(
                    navigationIcon = {
                        if (isCompactScreen) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    title = {
                        group?.let { GroupRowLayout(it) }
                    },
                    actions = {
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (chatUiState.isLoading) {
                        LoadingAnimation(Modifier.fillMaxWidth().wrapContentSize())
                    } else {
                        val infiniteTransition = rememberInfiniteTransition(label = "aurora_rotate")
                        val rotate by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    durationMillis = 1500,
                                    easing = LinearEasing
                                )
                            ),
                            label = "rotate"
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                            state = lazyListState,
                            reverseLayout = true,
                            verticalArrangement = if (chatUiState.message.isEmpty()) Arrangement.Center else Arrangement.Bottom,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        ) {
                            if (chatUiState.message.isNotEmpty()) {
                                items(chatUiState.message, key = { it.messageId }) {
                                    MessageItem(it, rotate)
                                }
                            } else {
                                item {
                                    WelcomeScreen()
                                }
                            }
                        }

                        AppVerticalScrollbar(
                            lazyListState = lazyListState,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxHeight()
                                .padding(vertical = 4.dp, horizontal = 2.dp),
                            reverseLayout = true
                        )
                    }
                }

                if (chatUiState.selectedImages.isNotEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LazyRow(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(chatUiState.selectedImages) { imageBytes ->
                                val bitmap = imageBytes.toComposeImageBitmap()
                                Box(
                                    modifier = Modifier
                                        .size(88.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                ) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = "Selected image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    FilledIconButton(
                                        onClick = { onRemoveImage(imageBytes) },
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(2.dp)
                                            .align(Alignment.TopEnd),
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.85f),
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove image",
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                BottomTextBar(
                    message = chatUiState.inputMessage,
                    onMessageChange = onMessageChange,
                    selectedImagesCount = chatUiState.selectedImages.size,
                    onAddImage = onAddImage,
                    onSendMessage = onSendMessage,
                    isApiLoading = chatUiState.isApiLoading,
                    onShowAlert = onShowAlert
                )
            }

            // Drawer Toggle Handle anchored to the vertical divider at CenterStart
            if (!isCompactScreen && onToggleDrawer != null) {
                DrawerToggleButton(
                    isDesktopDrawerOpen = isDesktopDrawerOpen,
                    onToggleDrawer = onToggleDrawer,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
    }
}

@AllAdaptivePreviews
@Composable
fun ChatScreenPreview() {
    ChatScreenContent(
        chatUiState = ChatUiState(
            groupId = "1",
            group = Group("1", "Gemini AI Chat", "03 Nov, 12:00 PM", "robot_1.png"),
            message = listOf(
                ChatMessage(messageId = "1", groupId = "1", text = "Hello Gemini!", participant = Role.YOU),
                ChatMessage(messageId = "2", groupId = "1", text = "Hello! How can I help you today?", participant = Role.GEMINI)
            )
        ),
        group = Group("1", "Gemini AI Chat", "03 Nov, 12:00 PM", "robot_1.png"),
        groupId = "1",
        isDesktopDrawerOpen = true
    )
}
