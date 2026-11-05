package com.coding.meet.gaminiaikmp.screenshot

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import com.apadmi.cmpdevicepreview.core.Appearance
import com.apadmi.cmpdevicepreview.core.DeviceConfiguration
import com.apadmi.cmpdevicepreview.core.DeviceOrientation
import com.apadmi.cmpdevicepreview.devices.DevicePresets
import com.apadmi.cmpdevicepreview.preview.DeviceFrameView
import com.apadmi.cmpdevicepreview.preview.DeviceScreenshots
import com.apadmi.cmpdevicepreview.preview.LocalDeviceScreenshots
import com.coding.meet.gaminiaikmp.theme.AppTheme
import com.coding.meet.gaminiaikmp.theme.AppThemeMode
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * UI / Screenshot Integration Tests running on the Desktop JVM engine via [runComposeUiTest].
 *
 * Captures pixel-perfect, native-resolution device screenshots without any outer window background
 * padding or blurriness:
 * - Android (Pixel 9 Native Resolution Frame, tightly cropped)
 * - iOS (iPhone 16 Pro Dynamic Island Frame, tightly cropped)
 * - macOS (Large Desktop Window Chassis, tightly cropped)
 * - Web (Chrome Browser Two-Pane Window, tightly cropped)
 * - Dialog Layers (Native Dialog with dark scrim rendered over device screen)
 */
@OptIn(ExperimentalTestApi::class)
class AllPlatformScreenshotTest {

    private val outputDir = File("build/test-screenshots").apply { mkdirs() }

    @Test
    fun captureAndroidPixel9ScreenshotInJvm() = runComposeUiTest {
        val screenshots = DeviceScreenshots()
        setContent {
            CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
                AppTheme(themeMode = AppThemeMode.DARK) {
                    DeviceFrameView(
                        device = DevicePresets.pixel9,
                        configuration = DeviceConfiguration(
                            orientation = DeviceOrientation.Portrait,
                            appearance = Appearance.Dark,
                            isFrameVisible = true
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AdaptiveAppContent(
                            state = AdaptiveScreenState.WELCOME
                        )
                    }
                }
            }
        }
        waitForIdle()

        val capturedImage = assertNotNull(
            screenshots.capture(includeFrame = true),
            "Pixel 9 device frame could not be captured"
        )
        val outputFile = File(outputDir, "test_android_pixel9_welcome.png")
        saveImageBitmap(capturedImage, outputFile)

        assertTrue(outputFile.exists() && outputFile.length() > 0, "Android screenshot was not saved!")
        println("✅ Captured Android UI Test Screenshot: ${outputFile.name} (${capturedImage.width}x${capturedImage.height})")
    }

    @Test
    fun captureIosIPhone16ProScreenshotInJvm() = runComposeUiTest {
        val screenshots = DeviceScreenshots()
        setContent {
            CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
                AppTheme(themeMode = AppThemeMode.DARK) {
                    DeviceFrameView(
                        device = DevicePresets.iPhone16Pro,
                        configuration = DeviceConfiguration(
                            orientation = DeviceOrientation.Portrait,
                            appearance = Appearance.Dark,
                            isFrameVisible = true
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AdaptiveAppContent(
                            state = AdaptiveScreenState.CHAT_RESPONSE
                        )
                    }
                }
            }
        }
        waitForIdle()

        val capturedImage = assertNotNull(
            screenshots.capture(includeFrame = true),
            "iPhone 16 Pro device frame could not be captured"
        )
        val outputFile = File(outputDir, "test_ios_iphone16_chat.png")
        saveImageBitmap(capturedImage, outputFile)

        assertTrue(outputFile.exists() && outputFile.length() > 0, "iOS screenshot was not saved!")
        println("✅ Captured iOS UI Test Screenshot: ${outputFile.name} (${capturedImage.width}x${capturedImage.height})")
    }

    @Test
    fun captureMacDesktopTwoPaneScreenshotInJvm() = runComposeUiTest {
        val screenshots = DeviceScreenshots()
        setContent {
            CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
                AppTheme(themeMode = AppThemeMode.DARK) {
                    DeviceFrameView(
                        device = DevicePresets.largeDesktopWindow,
                        configuration = DeviceConfiguration(
                            orientation = DeviceOrientation.Landscape,
                            appearance = Appearance.Dark,
                            isFrameVisible = true
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AdaptiveAppContent(
                            state = AdaptiveScreenState.WELCOME
                        )
                    }
                }
            }
        }
        waitForIdle()

        val capturedImage = assertNotNull(
            screenshots.capture(includeFrame = true),
            "macOS window frame could not be captured"
        )
        val outputFile = File(outputDir, "test_mac_desktop_two_pane.png")
        saveImageBitmap(capturedImage, outputFile)

        assertTrue(outputFile.exists() && outputFile.length() > 0, "macOS screenshot was not saved!")
        println("✅ Captured macOS UI Test Screenshot: ${outputFile.name} (${capturedImage.width}x${capturedImage.height})")
    }

    @Test
    fun captureWebBrowserTwoPaneScreenshotInJvm() = runComposeUiTest {
        val screenshots = DeviceScreenshots()
        setContent {
            CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
                AppTheme(themeMode = AppThemeMode.DARK) {
                    DeviceFrameView(
                        device = DevicePresets.chromeBrowser,
                        configuration = DeviceConfiguration(
                            orientation = DeviceOrientation.Landscape,
                            appearance = Appearance.Dark,
                            isFrameVisible = true
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AdaptiveAppContent(
                            state = AdaptiveScreenState.WELCOME
                        )
                    }
                }
            }
        }
        waitForIdle()

        val capturedImage = assertNotNull(
            screenshots.capture(includeFrame = true),
            "Web browser window frame could not be captured"
        )
        val outputFile = File(outputDir, "test_web_browser_two_pane.png")
        saveImageBitmap(capturedImage, outputFile)

        assertTrue(outputFile.exists() && outputFile.length() > 0, "Web screenshot was not saved!")
        println("✅ Captured Web UI Test Screenshot: ${outputFile.name} (${capturedImage.width}x${capturedImage.height})")
    }

    @Test
    fun captureDialogLayerScreenshotInJvm() = runComposeUiTest {
        val screenshots = DeviceScreenshots()
        setContent {
            CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
                AppTheme(themeMode = AppThemeMode.DARK) {
                    DeviceFrameView(
                        device = DevicePresets.pixel9,
                        configuration = DeviceConfiguration(
                            orientation = DeviceOrientation.Portrait,
                            appearance = Appearance.Dark,
                            isFrameVisible = true
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AdaptiveAppContent(
                            state = AdaptiveScreenState.NEW_GROUP_DIALOG
                        )
                    }
                }
            }
        }
        waitForIdle()

        val capturedImage = assertNotNull(
            screenshots.capture(includeFrame = true),
            "Dialog device frame could not be captured"
        )
        val outputFile = File(outputDir, "test_android_new_group_dialog.png")
        saveImageBitmap(capturedImage, outputFile)

        assertTrue(outputFile.exists() && outputFile.length() > 0, "Dialog screenshot was not saved!")
        println("✅ Captured Dialog UI Test Screenshot: ${outputFile.name} (${capturedImage.width}x${capturedImage.height})")
    }

    private fun saveImageBitmap(bitmap: ImageBitmap, file: File) {
        val skiaBitmap = bitmap.asSkiaBitmap()
        val pngBytes = Image.makeFromBitmap(skiaBitmap)
            .encodeToData(EncodedImageFormat.PNG)
            ?.bytes
        if (pngBytes != null) {
            file.writeBytes(pngBytes)
        }
    }
}
