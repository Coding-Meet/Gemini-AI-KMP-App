package com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.coding.meet.gaminiaikmp.domain.model.Group
import com.coding.meet.gaminiaikmp.presentation.components.AppVerticalScrollbar
import com.coding.meet.gaminiaikmp.presentation.components.SimpleAlertDialog
import com.coding.meet.gaminiaikmp.presentation.components.WelcomeScreen
import com.coding.meet.gaminiaikmp.presentation.preview.AllAdaptivePreviews
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.ApiKeyAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.GroupLayout
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.NewChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.components.TopBarLayout
import com.coding.meet.gaminiaikmp.utils.ObserveAsEvents
import com.coding.meet.gaminiaikmp.utils.currentDateTimeToString
import com.coding.meet.gaminiaikmp.utils.isValidApiKey
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.Uuid

@Composable
fun GroupListScreen(
    groupViewModel: GroupViewModel = koinViewModel(),
    selectedGroupId: String? = null,
    onGroupClick: (groupId: String) -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val groupUiState by groupViewModel.uiState.collectAsState()

    // Observe GroupUiEffect lifecycle-safely
    groupViewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is GroupUiEffect.NavigateToDetail -> {
                groupViewModel.handleIntent(GroupUiIntent.SelectGroup(effect.groupId))
                onGroupClick(effect.groupId)
            }
            is GroupUiEffect.ShowAlert -> {
                groupViewModel.handleIntent(GroupUiIntent.ShowAlert(effect.title, effect.message))
            }
            else -> Unit
        }
    }

    ApiKeyAlertDialog(
        show = groupUiState.isApiShowDialog,
        initialApiKey = groupUiState.apiKeyText,
        onDismiss = { groupViewModel.handleIntent(GroupUiIntent.DismissApiKeyDialog) },
        onSave = { apiKey -> groupViewModel.handleIntent(GroupUiIntent.SaveApiKey(apiKey)) },
        onError = { title, message -> groupViewModel.handleIntent(GroupUiIntent.ShowAlert(title, message)) }
    )

    NewChatAlertDialog(
        show = groupUiState.isNewChatShowDialog,
        onDismiss = { groupViewModel.handleIntent(GroupUiIntent.DismissNewChatDialog) },
        onSave = { name ->
            groupViewModel.handleIntent(
                GroupUiIntent.AddNewGroup(
                    groupId = Uuid.random().toString(),
                    groupName = name,
                    date = currentDateTimeToString(),
                    icon = "robot_${(1..8).random()}.png"
                )
            )
        },
        onError = { title, message -> groupViewModel.handleIntent(GroupUiIntent.ShowAlert(title, message)) }
    )

    SimpleAlertDialog(
        show = groupUiState.isAlertDialogShow,
        title = groupUiState.alertTitleText,
        message = groupUiState.alertDescText,
        onDismiss = { groupViewModel.handleIntent(GroupUiIntent.DismissAlert) }
    )

    GroupListScreenContent(
        groupUiState = groupUiState,
        selectedGroupId = selectedGroupId,
        onNewGroupClick = {
            val apiKey = groupViewModel.getApikeyLocalStorage().trim()
            if (apiKey.isNotEmpty()) {
                if (apiKey.isValidApiKey()) {
                    groupViewModel.handleIntent(GroupUiIntent.OpenNewChatDialog)
                } else {
                    groupViewModel.handleIntent(GroupUiIntent.OpenApiKeyDialog)
                }
            } else {
                groupViewModel.handleIntent(GroupUiIntent.OpenApiKeyDialog)
            }
        },
        onSettingsClick = onSettingsClick,
        onItemClick = { groupItem ->
            val apiKey = groupViewModel.getApikeyLocalStorage().trim()
            if (apiKey.isNotEmpty()) {
                if (apiKey.isValidApiKey()) {
                    groupViewModel.handleIntent(GroupUiIntent.SelectGroup(groupItem.groupId))
                    onGroupClick(groupItem.groupId)
                } else {
                    groupViewModel.handleIntent(GroupUiIntent.UpdateApiKeyText(apiKey))
                    groupViewModel.handleIntent(GroupUiIntent.OpenApiKeyDialog)
                }
            } else {
                groupViewModel.handleIntent(GroupUiIntent.UpdateApiKeyText(apiKey))
                groupViewModel.handleIntent(GroupUiIntent.OpenApiKeyDialog)
            }
        }
    )
}

@Composable
fun GroupListScreenContent(
    groupUiState: GroupUiState,
    selectedGroupId: String? = null,
    onNewGroupClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onItemClick: (group: Group) -> Unit = {}
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val isCompactScreen = !windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val listState = rememberLazyListState()
    val activeSelection = selectedGroupId ?: groupUiState.selectedGroupId

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            TopBarLayout(
                onNewGroupClick = onNewGroupClick,
                onSettingsClick = onSettingsClick
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        state = listState,
                        verticalArrangement = if (groupUiState.groupList.isEmpty()) Arrangement.Center else Arrangement.Top,
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(groupUiState.groupList, key = { it.groupId }) { groupItem ->
                            GroupLayout(
                                group = groupItem,
                                isSelected = !isCompactScreen && (activeSelection == groupItem.groupId),
                                onClick = { onItemClick(groupItem) }
                            )
                        }
                        if (isCompactScreen && groupUiState.groupList.isEmpty()) {
                            item {
                                WelcomeScreen()
                            }
                        }
                    }

                    AppVerticalScrollbar(
                        lazyListState = listState,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .padding(vertical = 4.dp, horizontal = 2.dp)
                    )
                }
            }
        }

        if (!isCompactScreen) {
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@AllAdaptivePreviews
@Composable
fun GroupListScreenPreview() {
    GroupListScreenContent(
        groupUiState = GroupUiState(
            groupList = listOf(
                Group("1", "Kotlin Multiplatform Discussion", "03 Nov, 10:30 AM", "robot_1.png"),
                Group("2", "Gemini 3.8 Flash Testing", "03 Nov, 11:15 AM", "robot_2.png"),
                Group("3", "Compose Adaptive Layouts", "03 Nov, 01:45 PM", "robot_3.png")
            ),
            selectedGroupId = "1"
        )
    )
}
