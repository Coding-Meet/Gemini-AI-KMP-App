package com.coding.meet.gaminiaikmp.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import com.coding.meet.gaminiaikmp.presentation.components.WelcomeScreen
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatScreen
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.DrawerToggleButton
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupListScreen
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupUiIntent
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainUiIntent
import com.coding.meet.gaminiaikmp.presentation.screens.mainscreen.MainViewModel
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.SettingsScreen
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = koinViewModel(),
    groupViewModel: GroupViewModel = koinViewModel()
) {
    val mainUiState by mainViewModel.uiState.collectAsState()
    val backStack = rememberNavBackStack(nav3Configuration, ScreenRoute.ConversationList)
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()

    // Adapt layout partitions dynamically:
    // On wide screens (desktop/tablet/foldable), allow expanding/collapsing the drawer
    val directive = remember(windowAdaptiveInfo, mainUiState.isDesktopDrawerOpen) {
        val baseDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo)
        val maxPartitions = if (baseDirective.maxHorizontalPartitions > 1) {
            if (mainUiState.isDesktopDrawerOpen) 2 else 1
        } else {
            1
        }
        baseDirective.copy(
            maxHorizontalPartitions = maxPartitions,
            horizontalPartitionSpacerSize = 0.dp
        )
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
                groupViewModel.handleIntent(GroupUiIntent.ClearSelection)
            }
        },
        sceneStrategies = listOf(listDetailStrategy),
        entryProvider = entryProvider {
            entry<ScreenRoute.ConversationList>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        val isCompactScreen = !windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
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
                                WelcomeScreen()
                                if (!isCompactScreen) {
                                    DrawerToggleButton(
                                        isDesktopDrawerOpen = mainUiState.isDesktopDrawerOpen,
                                        onToggleDrawer = {
                                            mainViewModel.handleIntent(MainUiIntent.ToggleDesktopDrawer)
                                        },
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                }
                            }
                        }
                    }
                )
            ) {
                GroupListScreen(
                    groupViewModel = groupViewModel,
                    onSettingsClick = {
                        backStack.add(ScreenRoute.Settings)
                    },
                    onGroupClick = { index, groupId ->
                        groupViewModel.handleIntent(GroupUiIntent.SelectGroup(index, groupId))
                        val currentDetail = backStack.lastOrNull() as? ScreenRoute.ConversationDetail
                        if (currentDetail != null) {
                            if (currentDetail.groupId != groupId) {
                                backStack[backStack.lastIndex] = ScreenRoute.ConversationDetail(groupId)
                            }
                        } else {
                            backStack.add(ScreenRoute.ConversationDetail(groupId))
                        }
                    }
                )
            }

            entry<ScreenRoute.ConversationDetail>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { conversation ->
                ChatScreen(
                    groupId = conversation.groupId,
                    onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                        groupViewModel.handleIntent(GroupUiIntent.ClearSelection)
                    },
                    onDeleteSuccess = {
                        groupViewModel.handleIntent(GroupUiIntent.LoadGroups)
                        groupViewModel.handleIntent(GroupUiIntent.ClearSelection)
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },
                    isDesktopDrawerOpen = mainUiState.isDesktopDrawerOpen,
                    onToggleDrawer = {
                        mainViewModel.handleIntent(MainUiIntent.ToggleDesktopDrawer)
                    }
                )
            }

            entry<ScreenRoute.Settings>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                SettingsScreen(
                    onBackClick = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },
                    onAllConversationsDeleted = {
                        groupViewModel.handleIntent(GroupUiIntent.LoadGroups)
                        groupViewModel.handleIntent(GroupUiIntent.ClearSelection)
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    },
                    isDesktopDrawerOpen = mainUiState.isDesktopDrawerOpen,
                    onToggleDrawer = {
                        mainViewModel.handleIntent(MainUiIntent.ToggleDesktopDrawer)
                    }
                )
            }
        }
    )
}
