package com.coding.meet.gaminiaikmp.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.coding.meet.gaminiaikmp.presentation.components.WelcomeScreen
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.ChatScreen
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupListScreen
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.SettingsScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(nav3Configuration, ScreenRoute.ConversationList)
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()

    // Canonical Material 3 List-Detail adaptive directive
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    val activeGroupId = (backStack.lastOrNull() as? ScreenRoute.ConversationDetail)?.groupId

    NavDisplay(
        modifier = Modifier.fillMaxSize(),
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        sceneStrategies = listOf(listDetailStrategy),
        entryProvider = entryProvider {
            entry<ScreenRoute.ConversationList>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        WelcomeScreen()
                    }
                )
            ) {
                GroupListScreen(
                    selectedGroupId = activeGroupId,
                    onSettingsClick = {
                        if (backStack.lastOrNull() is ScreenRoute.Settings) return@GroupListScreen
                        backStack.add(ScreenRoute.Settings)
                    },
                    onGroupClick = { groupId ->
                        if (backStack.lastOrNull() is ScreenRoute.ConversationDetail) {
                            backStack.removeLastOrNull()
                        }
                        backStack.add(ScreenRoute.ConversationDetail(groupId))
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
                    },
                    onDeleteSuccess = {
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
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
                        if (backStack.size > 1) {
                            backStack.removeLastOrNull()
                        }
                    }
                )
            }
        }
    )
}
