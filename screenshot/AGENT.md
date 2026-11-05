# 🤖 CMPDevicePreview JVM Engine — Agent & Developer Integration Guide

This guide is designed for **AI coding agents** and **human developers** integrating [`screenshot/libs/cmpdevicepreview-jvm.jar`](libs/cmpdevicepreview-jvm.jar) into any Compose Multiplatform (Desktop / JVM) project.

---

## 1. Overview & Key Capabilities

`cmpdevicepreview-jvm.jar` is an enhanced, standalone build compiled from [Coding-Meet/CMPDevicePreview](https://github.com/Coding-Meet/CMPDevicePreview) (fork of Apadmi's CMPDevicePreview with native Dialog layer interception and realistic Chrome Web browser framing). It allows you to wrap any Jetpack Compose / Compose Multiplatform screen inside realistic hardware chassis bezels (Pixel, iPhone, Mac, Web Browser) and export pixel-perfect screenshots directly from JVM memory without needing an emulator or physical device.

### 🌟 Exclusive Enhancements in This JAR (vs. Upstream):
1. **`ComposeSceneLayer` Dialog & Popup Interception**:
   - Upstream CMPDevicePreview fails to capture `AlertDialog`, `Dialog`, and popups because Skiko renders them in separate window scene layers.
   - This JAR intercepts all `ComposeSceneLayer` instances in `SimulatedPlatform.skiko.kt` and renders them directly inside the simulated device container with proper background scrims.
2. **Solid Opacity Dialog Scrims**:
   - Fast-forwards Material 3 dialog entrance animations so captured snapshots have solid, high-contrast 100% opacity scrims rather than semi-transparent flashes.
3. **Realistic Chrome Web Browser Frame**:
   - Built-in `WebBrowserFrame` and `WebBrowserTopBar` with macOS traffic lights, active tab, Omnibox address bar, SSL lock, and dark/light styling.
4. **Native High-Res Chassis Capture**:
   - `LocalDeviceScreenshots.capture(includeFrame = true)` captures the internal hardware chassis layer at native pixel density (e.g. 1196×2539 for Pixel 9, 1308×2724 for iPhone 16 Pro) with clean transparent corner cutouts.

---

## 2. Quick Setup in an External Project

To use this JAR in any external Compose Multiplatform / Desktop project:

### Step 1: Copy the JAR
Copy `cmpdevicepreview-jvm.jar` into your target project's module `libs/` directory:
```bash
my-project/
  └── my-module/
       ├── libs/
       │    └── cmpdevicepreview-jvm.jar
       └── build.gradle.kts
```

### Step 2: Configure `build.gradle.kts`
In `my-module/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.kotlinJvm) // or kotlin("jvm")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    // 1. Link the pre-compiled JAR
    implementation(files("libs/cmpdevicepreview-jvm.jar"))

    // 2. Compose Desktop & Foundation (Required by Skiko runtime)
    implementation(compose.desktop.currentOs)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.components.resources)

    // 3. Coroutines Swing (Required for Desktop Dispatchers.Main)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.1")

    // 4. Testing dependencies (Optional, for JVM UI tests)
    testImplementation(kotlin("test"))
    testImplementation(compose.uiTest)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
```

---

## 3. Public API Reference

All core classes live under the package: `com.apadmi.cmpdevicepreview`

### 📱 3.1 Device Presets (`com.apadmi.cmpdevicepreview.devices.DevicePresets`)
Access hardware presets through `DevicePresets.<presetName>`:

| Platform | Presets Available |
| :--- | :--- |
| **Android** | `DevicePresets.pixel9`, `DevicePresets.pixel10`, `DevicePresets.pixel10ProFold`, `DevicePresets.galaxyS24`, `DevicePresets.galaxyS25`, `DevicePresets.galaxyZFold8`, `DevicePresets.galaxyTabS10Plus` |
| **iOS / iPadOS** | `DevicePresets.iPhone16`, `DevicePresets.iPhone16Pro`, `DevicePresets.iPhone16ProMax`, `DevicePresets.iPhone17Pro`, `DevicePresets.iPadPro11M4`, `DevicePresets.iPadAir13M4` |
| **macOS Desktop** | `DevicePresets.largeDesktopWindow` (1600×923 dp), `DevicePresets.smallDesktopWindow` |
| **Web Browser** | `DevicePresets.chromeBrowser` (1600×900 dp), `DevicePresets.webBrowser` |
| **Utilities** | `DevicePresets.all: List<DevicePreset>`, `DevicePresets.byId(id: String): DevicePreset?` |

---

### 🎨 3.2 Configuration Models (`com.apadmi.cmpdevicepreview.core.*`)

#### `DeviceConfiguration`
Controls the orientation, theme, and hardware bezels of the simulated device:
```kotlin
data class DeviceConfiguration(
    val orientation: DeviceOrientation = DeviceOrientation.Portrait, // Portrait, Landscape
    val appearance: Appearance = Appearance.Light,                   // Light, Dark
    val isFrameVisible: Boolean = true,                              // Draws outer chassis & bezels
    val isSystemUiVisible: Boolean = true,                           // Draws status bar & gesture pill
    val isKeyboardVisible: Boolean = false,                          // Shows software keyboard band
    val fontScale: Float = 1f,
    val displayScale: Float = 1f,
    val layoutDirection: LayoutDirection? = null,
    val language: String? = null,                                    // BCP 47 language tag (e.g. "en", "hi")
)
```

#### `SimulatedDevice`
Pairs a `DevicePreset` with a `DeviceConfiguration`:
```kotlin
val simulatedDevice = SimulatedDevice(
    device = DevicePresets.pixel9,
    configuration = DeviceConfiguration(
        appearance = Appearance.Dark,
        orientation = DeviceOrientation.Portrait
    )
)
```

---

### 🖼️ 3.3 Core Composables (`com.apadmi.cmpdevicepreview.preview.*`)

#### 1. `DeviceFrame(...)`
The foundational component that renders the device hardware chassis, status bars, and content:
```kotlin
@Composable
fun DeviceFrame(
    device: Device,
    modifier: Modifier = Modifier,
    configuration: DeviceConfiguration = DeviceConfiguration(),
    isMouseAsTouch: Boolean = device.isMouseAsTouchByDefault,
    onResize: ((FreeformDevice) -> Unit)? = null,
    onLayoutScale: ((Float) -> Unit)? = null,
    content: @Composable () -> Unit,
)
```

#### 2. `SimulatedDevicePreview(...)`
Embeds `DeviceFrame` on top of a customizable canvas backdrop (dot grid, light, dark):
```kotlin
@Composable
fun SimulatedDevicePreview(
    modifier: Modifier = Modifier,
    simulatedDevice: SimulatedDevice = SimulatedDevice(DevicePresets.galaxyS25),
    previewSettings: PreviewSettings = PreviewSettings(), // backdrop, isDotGridVisible
    onResize: ((SimulatedDevice) -> Unit)? = null,
    content: @Composable () -> Unit,
)
```

#### 3. `DeviceWithConfigurationView(...)`
Full-featured interactive UI preview with a collapsable side settings panel (device picker, orientation toggle, dark/light switch, font slider):
```kotlin
@Composable
fun DeviceWithConfigurationView(
    modifier: Modifier = Modifier,
    initialDevice: Device = DevicePresets.galaxyS25,
    initialConfiguration: DeviceConfiguration = DeviceConfiguration(),
    initialPreviewSettings: PreviewSettings = PreviewSettings(),
    isPersistenceEnabled: Boolean = true,
    languages: List<PreviewLanguage> = emptyList(),
    content: @Composable () -> Unit,
)
```

#### 4. `WebBrowserFrame(...)` & `WebBrowserTopBar(...)`
Wraps any composable in a realistic Google Chrome web browser window:
```kotlin
@Composable
fun WebBrowserFrame(
    title: String = "Web App",
    url: String = "https://localhost:8080/",
    isDark: Boolean = true,
    modifier: Modifier = Modifier,
    favicon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
)
```

---

### 📸 3.4 Screenshot Capture Engine (`com.apadmi.cmpdevicepreview.preview.*`)

```kotlin
val LocalDeviceScreenshots: ProvidableCompositionLocal<DeviceScreenshots?>

class DeviceScreenshots {
    val canCapture: Boolean
    fun capture(includeFrame: Boolean): ImageBitmap?
}
```
- Pass `includeFrame = true`: Captures the whole device with hardware bezels and rounded transparent corners.
- Pass `includeFrame = false`: Captures only the internal screen content.

---

## 4. End-to-End Implementation Recipes

### Recipe 1: Headless Bulk Screenshot Exporter (CLI Script)
Render screens directly to PNG files on disk without launching an interactive GUI window:

```kotlin
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.unit.Density
import com.apadmi.cmpdevicepreview.core.Appearance
import com.apadmi.cmpdevicepreview.core.DeviceConfiguration
import com.apadmi.cmpdevicepreview.devices.DevicePresets
import com.apadmi.cmpdevicepreview.preview.DeviceFrame
import com.apadmi.cmpdevicepreview.preview.DeviceScreenshots
import com.apadmi.cmpdevicepreview.preview.LocalDeviceScreenshots
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
fun exportScreenshot(outputFile: File) {
    val device = DevicePresets.pixel9
    val config = DeviceConfiguration(appearance = Appearance.Dark)
    val screenshots = DeviceScreenshots()

    // 1. Setup off-screen Skiko scene with generous bounds
    val scene = ImageComposeScene(width = 1600, height = 3000, density = Density(1f))
    try {
        scene.setContent {
            CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
                DeviceFrame(device = device, configuration = config) {
                    MyScreenContent()
                }
            }
        }

        // 2. Fast-forward animation clock so Dialogs and UI settle
        repeat(5) { frame ->
            scene.render(nanoTime = frame * 16_000_000L)
        }

        // 3. Capture native-resolution framed bitmap
        val capturedImage = checkNotNull(screenshots.capture(includeFrame = true)) {
            "Failed to capture frame screenshot"
        }

        // 4. Encode to PNG
        val skiaBitmap = capturedImage.asSkiaBitmap()
        val pngData = Image.makeFromBitmap(skiaBitmap).encodeToData(EncodedImageFormat.PNG)
        outputFile.parentFile?.mkdirs()
        outputFile.writeBytes(pngData!!.bytes)
    } finally {
        scene.close()
    }
}
```

---

### Recipe 2: Automated JVM UI Tests (No Emulator)
Run UI regression tests inside the JVM with `runComposeUiTest`:

```kotlin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.apadmi.cmpdevicepreview.core.Appearance
import com.apadmi.cmpdevicepreview.core.DeviceConfiguration
import com.apadmi.cmpdevicepreview.devices.DevicePresets
import com.apadmi.cmpdevicepreview.preview.DeviceFrame
import com.apadmi.cmpdevicepreview.preview.DeviceScreenshots
import com.apadmi.cmpdevicepreview.preview.LocalDeviceScreenshots
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class DeviceScreenshotUiTest {

    @Test
    fun testPixel9DialogScreenshot() = runComposeUiTest {
        val screenshots = DeviceScreenshots()

        setContent {
            CompositionLocalProvider(LocalDeviceScreenshots provides screenshots) {
                DeviceFrame(
                    device = DevicePresets.pixel9,
                    configuration = DeviceConfiguration(appearance = Appearance.Dark),
                ) {
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("Delete Chat?") },
                        text = { Text("Are you sure you want to delete this chat?") },
                        confirmButton = { TextButton(onClick = {}) { Text("Delete") } },
                    )
                }
            }
        }

        waitForIdle()

        // Capture high-res native chassis
        val image = screenshots.capture(includeFrame = true)
        assertNotNull(image)
        assertTrue(image.width > 1000)
        assertTrue(image.height > 2000)
    }
}
```

---

### Recipe 3: Web Browser Frame Wrapper
Wrap Web / Desktop layouts with the Chrome frame:

```kotlin
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.apadmi.cmpdevicepreview.preview.WebBrowserFrame

@Composable
fun WebAppPreview() {
    WebBrowserFrame(
        title = "Gemini AI Assistant",
        url = "https://gemini-ai.web.app/",
        isDark = true,
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Your Web App Content Here")
        }
    }
}
```

---

## 5. Critical Gotchas & Pro-Tips

### ⚠️ Gotcha 1: `Ambiguous semantics node interaction: Found 2 nodes matching (isRoot)`
- **Why it happens**: When a Compose `AlertDialog` or `Dialog` opens, Compose creates a second root semantic node for the dialog popup window. Calling `onRoot().captureToImage()` will crash with an assertion error.
- **Solution**: Always use `LocalDeviceScreenshots.capture(includeFrame = true)`. It reads the internal `GraphicsLayer` directly and captures both the background screen, scrim, and dialog window perfectly merged.

### ⚠️ Gotcha 2: Blurred Snapshots with `onRoot().captureToImage()`
- **Why it happens**: Compose's test root window has a fixed default size of 1024×768. If you try to fit a 1196×2539 Pixel 9 inside it and capture the root window, it downsamples and creates grey letterbox padding.
- **Solution**: `screenshots.capture(includeFrame = true)` captures at the device's native pixel density (1196×2539), yielding razor-sharp text and transparent corner cutouts.

### 💡 Pro-Tip 3: Adaptive Layout Breakpoints
Use `BoxWithConstraints` inside your screen content to adapt dynamically to whichever device is simulated:
```kotlin
BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    if (maxWidth >= 840.dp) {
        TwoPaneLayout() // Automatically selected for macOS, Web, and Foldables
    } else {
        SinglePaneLayout() // Automatically selected for Pixel 9, iPhone 16 Pro
    }
}
```

