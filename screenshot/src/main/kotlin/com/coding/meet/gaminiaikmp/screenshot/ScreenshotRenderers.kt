package com.coding.meet.gaminiaikmp.screenshot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apadmi.cmpdevicepreview.core.Appearance
import com.apadmi.cmpdevicepreview.core.DeviceConfiguration
import com.apadmi.cmpdevicepreview.core.DeviceOrientation
import com.apadmi.cmpdevicepreview.core.DevicePreset
import com.apadmi.cmpdevicepreview.preview.DeviceFrameView
import com.apadmi.cmpdevicepreview.preview.DeviceScreenshots
import com.apadmi.cmpdevicepreview.preview.LocalDeviceScreenshots
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupListScreenContent
import com.coding.meet.gaminiaikmp.presentation.screens.grouplistscreen.GroupUiState
import com.coding.meet.gaminiaikmp.theme.AppTheme
import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File
import kotlin.math.roundToInt

@Composable
internal fun TwoPaneLayout(
    groupUiState: GroupUiState,
    selectedGroupId: String? = null,
    detailContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .width(360.dp)
                .fillMaxHeight()
        ) {
            GroupListScreenContent(
                groupUiState = groupUiState,
                selectedGroupId = selectedGroupId
            )
        }
        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            detailContent()
        }
    }
}
internal fun renderPhoneScreen(
    preset: DevicePreset,
    outputFile: File,
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    val isPortrait = true
    val bounds = preset.frame?.bodyBounds(preset.portraitSize, isPortrait)
        ?: Rect(Offset.Zero, preset.portraitSize)

    val sceneWidth = (bounds.size.width + 40f).roundToInt()
    val sceneHeight = (bounds.size.height + 40f).roundToInt()

    val screenshots = DeviceScreenshots()
    val config = DeviceConfiguration(
        orientation = DeviceOrientation.Portrait,
        appearance = if (isDark) Appearance.Dark else Appearance.Light,
        isFrameVisible = true,
    )

    val scene = ImageComposeScene(width = sceneWidth, height = sceneHeight) {
        CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
            AppTheme(themeMode = if (isDark) AppThemeMode.DARK else AppThemeMode.LIGHT) {
                DeviceFrameView(
                    device = preset,
                    configuration = config,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    content()
                }
            }
        }
    }

    try {
        repeat(10) { frame ->
            Thread.sleep(30)
            scene.render(nanoTime = (frame + 1) * 100_000_000L)
        }
        val captured = screenshots.capture(includeFrame = true)
        if (captured == null) {
            System.err.println("Failed to capture: ${outputFile.name}")
            return
        }
        val pngBytes = Image.makeFromBitmap(captured.asSkiaBitmap())
            .encodeToData(EncodedImageFormat.PNG)
            ?.bytes

        if (pngBytes != null) {
            outputFile.writeBytes(pngBytes)
            println("  -> Saved ${outputFile.name} (${captured.width}x${captured.height}, ${pngBytes.size / 1024} KB)")
        }
    } finally {
        scene.close()
    }
}

internal fun renderDesktopScreen(
    preset: DevicePreset,
    outputFiles: List<File>,
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    val bounds = preset.frame?.bodyBounds(preset.portraitSize, false)
        ?: Rect(Offset.Zero, preset.portraitSize)

    val sceneWidth = (bounds.size.width + 30f).roundToInt()
    val sceneHeight = (bounds.size.height + 30f).roundToInt()

    val screenshots = DeviceScreenshots()
    val config = DeviceConfiguration(
        orientation = DeviceOrientation.Landscape,
        appearance = if (isDark) Appearance.Dark else Appearance.Light,
        isFrameVisible = true,
    )

    val scene = ImageComposeScene(width = sceneWidth, height = sceneHeight) {
        CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
            AppTheme(themeMode = if (isDark) AppThemeMode.DARK else AppThemeMode.LIGHT) {
                DeviceFrameView(
                    device = preset,
                    configuration = config,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    content()
                }
            }
        }
    }

    try {
        repeat(10) { frame ->
            Thread.sleep(30)
            scene.render(nanoTime = (frame + 1) * 100_000_000L)
        }
        val captured = screenshots.capture(includeFrame = true)
        if (captured == null) {
            System.err.println("Failed to capture desktop")
            return
        }

        val targetScale = 1600f / captured.width.toFloat()
        val finalImage = captured.scaled(targetScale)
        val skiaBitmap = finalImage.asSkiaBitmap()

        val pngBytes = Image.makeFromBitmap(skiaBitmap).encodeToData(EncodedImageFormat.PNG)?.bytes

        for (file in outputFiles) {
            if (pngBytes != null) {
                file.writeBytes(pngBytes)
                println("  -> Saved ${file.name} (${finalImage.width}x${finalImage.height}, ${pngBytes.size / 1024} KB)")
            }
        }
    } finally {
        scene.close()
    }
}

internal fun renderWebBrowserScreen(
    outputFile: File,
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    val sceneWidth = 1600
    val sceneHeight = 900

    val scene = ImageComposeScene(width = sceneWidth, height = sceneHeight) {
        AppTheme(themeMode = if (isDark) AppThemeMode.DARK else AppThemeMode.LIGHT) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) Color(0xFF202124) else Color(0xFFF1F3F4))
            ) {
                BrowserTopBar(isDark = isDark)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    content()
                }
            }
        }
    }

    try {
        repeat(10) { frame ->
            Thread.sleep(30)
            scene.render(nanoTime = (frame + 1) * 100_000_000L)
        }
        val skiaImage = scene.render(nanoTime = 1_000_000_000L)
        val pngBytes = skiaImage.encodeToData(EncodedImageFormat.PNG)?.bytes
        if (pngBytes != null) {
            outputFile.writeBytes(pngBytes)
            println("  -> Saved ${outputFile.name} (${sceneWidth}x${sceneHeight}, ${pngBytes.size / 1024} KB)")
        }
    } finally {
        scene.close()
    }
}

@Composable
private fun BrowserTopBar(isDark: Boolean) {
    val tabStripBg = if (isDark) Color(0xFF1E1F22) else Color(0xFFDEE1E6)
    val tabBg = if (isDark) Color(0xFF2B2D30) else Color(0xFFFFFFFF)
    val tabTextColor = if (isDark) Color.White else Color(0xFF3C4043)
    val addressStripBg = if (isDark) Color(0xFF2B2D30) else Color(0xFFFFFFFF)
    val navIconColor = if (isDark) Color(0xFF9AA0A6) else Color(0xFF5F6368)
    val urlBoxBg = if (isDark) Color(0xFF1E1F22) else Color(0xFFF1F3F4)
    val urlTextColor = if (isDark) Color(0xFFE8EAED) else Color(0xFF3C4043)
    val lockColor = if (isDark) Color(0xFF8AB4F8) else Color(0xFF1A73E8)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(tabStripBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 8.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(12.dp).background(Color(0xFFFF5F56), CircleShape))
                Box(modifier = Modifier.size(12.dp).background(Color(0xFFFFBD2E), CircleShape))
                Box(modifier = Modifier.size(12.dp).background(Color(0xFF27C93F), CircleShape))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Surface(
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                color = tabBg,
                modifier = Modifier.padding(bottom = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✨ Gemini AI KMP App",
                        color = tabTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(addressStripBg)
                .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = navIconColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = navIconColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = urlBoxBg,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = lockColor,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "https://coding-meet.github.io/Gemini-AI-KMP-App/",
                        color = urlTextColor,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

private fun ImageBitmap.scaled(scale: Float): ImageBitmap {
    if (scale == 1f) return this
    val targetW = (width * scale).roundToInt().coerceAtLeast(1)
    val targetH = (height * scale).roundToInt().coerceAtLeast(1)
    val scaledImage = ImageBitmap(targetW, targetH)
    CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, Canvas(scaledImage), Size(targetW.toFloat(), targetH.toFloat())) {
        scale(scale, scale, pivot = Offset.Zero) {
            drawImage(this@scaled)
        }
    }
    return scaledImage
}

