package com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.coding.meet.gaminiaikmp.presentation.components.AppVerticalScrollbar
import com.coding.meet.gaminiaikmp.presentation.preview.AllAdaptivePreviews
import com.coding.meet.gaminiaikmp.presentation.screens.chatscreen.components.DrawerToggleButton
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.DeleteAllChatsDialog
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.EditApiKeyDialog
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.ModelCard
import com.coding.meet.gaminiaikmp.presentation.screens.settingsscreen.components.SettingsSectionCard
import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import com.coding.meet.gaminiaikmp.utils.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onAllConversationsDeleted: () -> Unit = {},
    isDesktopDrawerOpen: Boolean = false,
    onToggleDrawer: (() -> Unit)? = null
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observe SettingsUiEffect lifecycle-safely
    settingsViewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is SettingsUiEffect.NavigateBack -> onBackClick()
            is SettingsUiEffect.ShowSnackbar -> {
                snackbarHostState.showSnackbar(effect.message)
            }
            is SettingsUiEffect.AllConversationsDeleted -> {
                onAllConversationsDeleted()
            }
        }
    }

    EditApiKeyDialog(
        show = uiState.isEditApiKeyDialogOpen,
        initialKey = uiState.apiKey,
        onDismiss = { settingsViewModel.handleIntent(SettingsUiIntent.DismissEditApiKeyDialog) },
        onSaveKey = { settingsViewModel.handleIntent(SettingsUiIntent.SaveApiKey(it)) }
    )

    DeleteAllChatsDialog(
        show = uiState.isDeleteAllDialogOpen,
        onDismiss = { settingsViewModel.handleIntent(SettingsUiIntent.DismissDeleteAllDialog) },
        onConfirmDeleteAll = {
            settingsViewModel.handleIntent(SettingsUiIntent.ConfirmDeleteAllConversations)
        }
    )

    SettingsScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        isDesktopDrawerOpen = isDesktopDrawerOpen,
        onToggleDrawer = onToggleDrawer,
        onSelectModel = { settingsViewModel.handleIntent(SettingsUiIntent.SelectModel(it)) },
        onRefreshModels = { settingsViewModel.handleIntent(SettingsUiIntent.RefreshModels) },
        onSelectTheme = { settingsViewModel.handleIntent(SettingsUiIntent.SelectTheme(it)) },
        onOpenEditApiKey = { settingsViewModel.handleIntent(SettingsUiIntent.OpenEditApiKeyDialog) },
        onToggleApiKeyMask = { settingsViewModel.handleIntent(SettingsUiIntent.ToggleApiKeyMask) },
        onOpenDeleteAllDialog = { settingsViewModel.handleIntent(SettingsUiIntent.OpenDeleteAllDialog) }
    )
}

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState = SettingsUiState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick: () -> Unit = {},
    isDesktopDrawerOpen: Boolean = false,
    onToggleDrawer: (() -> Unit)? = null,
    onSelectModel: (String) -> Unit = {},
    onRefreshModels: () -> Unit = {},
    onSelectTheme: (AppThemeMode) -> Unit = {},
    onOpenEditApiKey: () -> Unit = {},
    onToggleApiKeyMask: () -> Unit = {},
    onOpenDeleteAllDialog: () -> Unit = {}
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val isCompactScreen = !windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
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
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        title = {
                            Text(
                                text = "Settings",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        scrollBehavior = scrollBehavior
                    )
                }
            ) { paddingValues ->
                val scrollState = rememberScrollState()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                    Spacer(modifier = Modifier.height(4.dp))

                    // SECTION 1: GEMINI API KEY
                    SettingsSectionCard(
                        title = "Google Gemini API Key",
                        icon = Icons.Default.Key,
                        description = "Key used to authenticate requests to Gemini AI"
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    val displayKey = if (uiState.apiKey.isBlank()) {
                                        "No API Key Configured"
                                    } else if (uiState.isApiKeyMasked) {
                                        uiState.apiKey.take(6) + "••••••••••••••••••••••••" + uiState.apiKey.takeLast(4)
                                    } else {
                                        uiState.apiKey
                                    }
                                    Text(
                                        text = displayKey,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (uiState.apiKey.isBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                if (uiState.apiKey.isNotBlank()) {
                                    IconButton(onClick = onToggleApiKeyMask) {
                                        Icon(
                                            imageVector = if (uiState.isApiKeyMasked) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle mask",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        FilledTonalButton(
                            shape = RoundedCornerShape(14.dp),
                            onClick = onOpenEditApiKey,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Change API Key")
                        }
                    }

                    // SECTION 2: GEMINI MODEL SELECTION
                    SettingsSectionCard(
                        title = "Gemini AI Model",
                        icon = Icons.Default.Psychology,
                        description = "Choose which Gemini model generates your answers"
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Models from Gemini API",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (uiState.isLoadingModels) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                IconButton(
                                    onClick = onRefreshModels,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh models",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                            val numColumns = when {
                                maxWidth >= 840.dp -> 3
                                maxWidth >= 460.dp -> 2
                                else -> 1
                            }

                            if (numColumns == 1) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    uiState.availableModels.forEach { model ->
                                        ModelCard(
                                            model = model,
                                            isSelected = model.id == uiState.selectedModel,
                                            onSelect = { onSelectModel(model.id) }
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    val columns = List(numColumns) { colIndex ->
                                        uiState.availableModels.filterIndexed { index, _ -> index % numColumns == colIndex }
                                    }
                                    columns.forEach { colModels ->
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            colModels.forEach { model ->
                                                ModelCard(
                                                    model = model,
                                                    isSelected = model.id == uiState.selectedModel,
                                                    onSelect = { onSelectModel(model.id) }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 3: THEME TOGGLE
                    SettingsSectionCard(
                        title = "Appearance & Theme",
                        icon = Icons.Default.Palette,
                        description = "Switch between Light, Dark, or System theme"
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AppThemeMode.entries.forEach { mode ->
                                val isSelected = mode == uiState.themeMode
                                Surface(
                                    onClick = { onSelectTheme(mode) },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                                    border = BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = when (mode) {
                                                AppThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
                                                AppThemeMode.LIGHT -> Icons.Default.LightMode
                                                AppThemeMode.DARK -> Icons.Default.DarkMode
                                            },
                                            contentDescription = mode.title,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = mode.title,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // SECTION 4: DATA & STORAGE (DELETE ALL CONVERSATIONS)
                    SettingsSectionCard(
                        title = "Data & Storage",
                        icon = Icons.Default.Storage,
                        description = "Manage conversation history stored on this device"
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Clear All Chats",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = "Permanently delete all conversations, messages and images",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }

                                Button(
                                    shape = RoundedCornerShape(12.dp),
                                    onClick = onOpenDeleteAllDialog,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteForever,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Delete All")
                                }
                            }
                        }
                    }

                    // SECTION 5: ABOUT
                    SettingsSectionCard(
                        title = "About",
                        icon = Icons.Default.Info,
                        description = "App version and technology details"
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Application", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Gemini AI KMP App", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Version", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("1.0.0", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Framework", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Compose Multiplatform 1.12.2", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Navigation", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Jetpack Navigation 3 Adaptive", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Targets", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Android • iOS • Desktop • Web", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                AppVerticalScrollbar(
                    scrollState = scrollState,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                )
            }
        }

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
fun SettingsScreenPreview() {
    SettingsScreenContent(
        uiState = SettingsUiState(
            apiKey = "AIzaSyD-sample-gemini-key",
            selectedModel = "gemini-2.5-flash",
            themeMode = AppThemeMode.DARK
        )
    )
}

