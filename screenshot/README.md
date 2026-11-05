# Screenshot Module 📸

This module is an internal developer tool for generating realistic, device-framed screenshots of the **Gemini AI KMP App** across multiple platforms (Android, iOS, macOS, and Web) in both **Dark Mode** and **Light Mode**.

---

## ⚠️ Important Note: Keep Commented in `settings.gradle.kts`

By default, the `:screenshot` and `:cmpdevicepreview` modules **must remain commented out** in [`settings.gradle.kts`](../settings.gradle.kts):

```kotlin
// Screenshot and Preview functionality only for developer internal screenshot capture
//include(":screenshot")
//include(":cmpdevicepreview")
//project(":cmpdevicepreview").projectDir = file("CMPDevicePreview/cmpdevicepreview")
```

> [!WARNING]
> **Do not uncomment these lines for regular app builds, contributions, or CI/CD pipelines.**
> If uncommented without the local `CMPDevicePreview` source present, Gradle synchronization and builds will fail.

---

## 🔍 Why is This Module Local & Commented Out?

1. **Official Library Limitations**:
   - The screenshot pipeline uses [CMPDevicePreview](https://github.com/Apadmi-Engineering/CMPDevicePreview) to render realistic device hardware bezels (e.g., Google Pixel 9, iPhone 16 Pro Dynamic Island, macOS desktop windows, and Chrome browser frames).
   - In the **official published version** of `cmpdevicepreview`, Compose Multiplatform Desktop has issues intercepting and rendering `AlertDialog`, `Dialog`, and underlying `ComposeSceneLayer` elements inside the off-screen simulated canvas.
   - Furthermore, upstream dialog appearance animations run asynchronously, causing captured dialog snapshots to render with low opacity / semi-transparent scrims.

2. **Custom Local Fixes in `CMPDevicePreview`**:
   - To overcome these upstream issues, we use a locally patched version of `cmpdevicepreview` located in `CMPDevicePreview/cmpdevicepreview`.
   - **Fixes Applied**:
     - Intercepted all `ComposeSceneLayer` instances directly inside `SimulatedPlatform.skiko.kt` and `SimulatedScreenLayers.skiko.kt`.
     - Rendered dialog and popup layers directly within the simulated screen container with proper background scrim overlays.
     - Advanced Skiko rendering time (`nanoTime`) over multiple frames to allow Material 3 `DialogAppearanceController` animations to fully settle at 100% solid opacity.

3. **Isolated from Production**:
   - Because these fixes are maintained locally and are not yet published to Maven Central, keeping `:screenshot` and `:cmpdevicepreview` commented out ensures that the core app (`:androidApp`, `:desktopApp`, `:webApp`, `:shared`) remains clean, independent, and builds seamlessly on any machine.

---

## 🛠️ How to Export Screenshots (Developer Guide)

When you need to regenerate screenshots for documentation or repository updates:

### Step 1: Ensure Local Submodule Exists
Verify that `CMPDevicePreview/cmpdevicepreview` exists in the project root.

### Step 2: Uncomment in `settings.gradle.kts`
Open [`settings.gradle.kts`](../settings.gradle.kts) and temporarily uncomment:

```kotlin
include(":screenshot")
include(":cmpdevicepreview")
project(":cmpdevicepreview").projectDir = file("CMPDevicePreview/cmpdevicepreview")
```

### Step 3: Run the Export Task or JVM UI Tests

#### Option A: Bulk Export All Screenshots (92 Assets)
Execute the Gradle export task in your terminal:

```bash
./gradlew exportDevicePreviewScreenshot
```

This will automatically render and save all **92 framed screenshots** (13 dark + 13 light Android, 13 dark + 13 light iOS, 10 dark + 10 light macOS, 10 dark + 10 light Web) into the `image/` directory.

#### Option B: Run JVM UI Tests (Automated Regression & Verification)
Execute the Compose UI test suite in your terminal:

```bash
./gradlew :screenshot:test
```

This runs [`AllPlatformScreenshotTest`](src/test/kotlin/com/coding/meet/gaminiaikmp/screenshot/AllPlatformScreenshotTest.kt) using Compose Multiplatform's `runComposeUiTest` directly inside the JVM without needing an emulator or physical device. It validates:
- Phone frames (Google Pixel 9, iPhone 16 Pro)
- macOS desktop window chassis
- Web Chrome browser chassis
- Native `ComposeSceneLayer` dialog rendering and scrims

---

### Step 4: Re-comment Before Committing
Before pushing your changes to GitHub, comment the lines back out in [`settings.gradle.kts`](../settings.gradle.kts):

```kotlin
// Screenshot and Preview functionality only for developer internal screenshot capture
//include(":screenshot")
//include(":cmpdevicepreview")
//project(":cmpdevicepreview").projectDir = file("CMPDevicePreview/cmpdevicepreview")
```

