package com.coding.meet.gaminiaikmp.presentation.screens.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.components.ApiKeyAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.components.GroupLayout
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.components.NewChatAlertDialog
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.components.TopBarLayout
import com.coding.meet.gaminiaikmp.utils.ObserveAsEvents
import com.coding.meet.gaminiaikmp.utils.currentDateTimeToString
import com.coding.meet.gaminiaikmp.utils.isValidApiKey
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.Uuid

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = koinViewModel(),
    onGroupClick: (index: Int, groupId: String) -> Unit = { _, _ -> },
    onSettingsClick: () -> Unit = {}
) {
    val mainUiState by mainViewModel.uiState.collectAsState()

    // Observe MainUiEffect lifecycle-safely
    mainViewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is MainUiEffect.NavigateToDetail -> {
                val groupIndex = mainUiState.groupList.indexOfFirst { it.groupId == effect.groupId }
                if (groupIndex != -1) {
                    onGroupClick(groupIndex, effect.groupId)
                }
            }
            is MainUiEffect.ShowAlert -> {
                mainViewModel.handleIntent(MainUiIntent.ShowAlert(effect.title, effect.message))
            }
            else -> Unit
        }
    }

    ApiKeyAlertDialog(
        show = mainUiState.isApiShowDialog,
        initialApiKey = mainUiState.apiKeyText,
        onDismiss = { mainViewModel.handleIntent(MainUiIntent.DismissApiKeyDialog) },
        onSave = { apiKey -> mainViewModel.handleIntent(MainUiIntent.SaveApiKey(apiKey)) },
        onError = { title, message -> mainViewModel.handleIntent(MainUiIntent.ShowAlert(title, message)) }
    )

    NewChatAlertDialog(
        show = mainUiState.isNewChatShowDialog,
        onDismiss = { mainViewModel.handleIntent(MainUiIntent.DismissNewChatDialog) },
        onSave = { name ->
            mainViewModel.handleIntent(
                MainUiIntent.AddNewGroup(
                    groupId = Uuid.random().toString(),
                    groupName = name,
                    date = currentDateTimeToString(),
                    icon = "robot_${(1..8).random()}.png"
                )
            )
        },
        onError = { title, message -> mainViewModel.handleIntent(MainUiIntent.ShowAlert(title, message)) }
    )

    SimpleAlertDialog(
        show = mainUiState.isAlertDialogShow,
        title = mainUiState.alertTitleText,
        message = mainUiState.alertDescText,
        onDismiss = { mainViewModel.handleIntent(MainUiIntent.DismissAlert) }
    )

    MainScreenContent(
        mainUiState = mainUiState,
        onNewGroupClick = {
            val apiKey = mainViewModel.getApikeyLocalStorage().trim()
            if (apiKey.isNotEmpty()) {
                if (apiKey.isValidApiKey()) {
                    mainViewModel.handleIntent(MainUiIntent.OpenNewChatDialog)
                } else {
                    mainViewModel.handleIntent(MainUiIntent.OpenApiKeyDialog)
                }
            } else {
                mainViewModel.handleIntent(MainUiIntent.OpenApiKeyDialog)
            }
        },
        onSettingsClick = onSettingsClick,
        onItemClick = { index, groupItem ->
            val apiKey = mainViewModel.getApikeyLocalStorage().trim()
            if (apiKey.isNotEmpty()) {
                if (apiKey.isValidApiKey()) {
                    onGroupClick(index, groupItem.groupId)
                } else {
                    mainViewModel.handleIntent(MainUiIntent.UpdateApiKeyText(apiKey))
                    mainViewModel.handleIntent(MainUiIntent.OpenApiKeyDialog)
                }
            } else {
                mainViewModel.handleIntent(MainUiIntent.UpdateApiKeyText(apiKey))
                mainViewModel.handleIntent(MainUiIntent.OpenApiKeyDialog)
            }
        }
    )
}

@Composable
fun MainScreenContent(
    mainUiState: MainUiState,
    onNewGroupClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onItemClick: (index: Int, group: Group) -> Unit = { _, _ -> }
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val isCompactScreen = !windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val listState = rememberLazyListState()

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
                verticalArrangement = if (mainUiState.groupList.isEmpty()) Arrangement.Center else Arrangement.Top,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(mainUiState.groupList, key = { _, groupItem ->
                    groupItem.groupId
                }) { index, groupItem ->
                    GroupLayout(
                        group = groupItem,
                        isSelected = mainUiState.currentPos == index,
                        onClick = { onItemClick(index, groupItem) }
                    )
                }
                if (isCompactScreen && mainUiState.groupList.isEmpty()) {
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

@AllAdaptivePreviews
@Composable
fun MainScreenPreview() {
    MainScreenContent(
        mainUiState = MainUiState(
            groupList = listOf(
                Group("1", "Kotlin Multiplatform", "03 Nov, 10:00 AM", "robot_1.png"),
                Group("2", "Compose Adaptive UI", "02 Nov, 08:30 PM", "robot_2.png"),
                Group("3", "Gemini 2.5 Pro Assistant", "01 Nov, 04:15 PM", "robot_3.png")
            ),
            currentPos = 0
        )
    )
}
