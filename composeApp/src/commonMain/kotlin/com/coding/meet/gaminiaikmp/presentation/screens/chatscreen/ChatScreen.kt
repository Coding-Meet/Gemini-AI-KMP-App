package com.coding.meet.gaminiaikmp.presentation.screens.chatscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.coding.meet.gaminiaikmp.di.toComposeImageBitmap
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.presentation.components.*
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.GroupUiState
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import com.coding.meet.gaminiaikmp.theme.borderColor
import com.coding.meet.gaminiaikmp.theme.lightBackgroundColor
import com.coding.meet.gaminiaikmp.theme.lightBorderColor
import com.coding.meet.gaminiaikmp.theme.whiteColor
import com.coding.meet.gaminiaikmp.utils.Screens
import com.coding.meet.gaminiaikmp.utils.TYPE


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    mainViewModel: MainViewModel,
    chatViewModel: ChatViewModel,
    chatUiState: ChatUiState,
    groupUiState: GroupUiState,
    group: Group
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Column(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        TopAppBar(
            navigationIcon = {
                if (mainViewModel.platformType == TYPE.MOBILE) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = lightBorderColor
                        ),
                        onClick = {
                            mainViewModel.screens = Screens.MAIN
                            mainViewModel.currentPos = -1
                        }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                            tint = whiteColor
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = borderColor,
                scrolledContainerColor = borderColor,
                titleContentColor = whiteColor,
            ),
            title = {
                GroupRowLayout(group)
            },
            actions = {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = lightBorderColor
                    ),
                    onClick = {
                        if (!chatUiState.isApiLoading) {
                            if (chatUiState.message.isNotEmpty()){
                                chatViewModel.isDeleteShowDialog = true
                            }
                        }else{
                            mainViewModel.alertTitleText = "API Call in Progress"
                            mainViewModel.alertDescText = "Please wait while there is already an API call going on, so please be patient."
                            mainViewModel.isAlertDialogShow = true
                        }
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
            horizontalArrangement = Arrangement.Center,
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
                            Icons.AutoMirrored.Filled.KeyboardArrowRight
                        } else {
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft
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
                    verticalArrangement =  if (chatUiState.message.isEmpty()) Arrangement.Center else Arrangement.Bottom,
                    contentPadding = PaddingValues(horizontal = 10.dp),
                ) {
                    if (chatUiState.message.isNotEmpty()) {
                        items(chatUiState.message, key = {
                            it.messageId
                        }) {
                            MessageItem(it)
                        }
                    } else {
                        item {
                            WelcomeScreen()
                        }
                    }
                }
            }

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