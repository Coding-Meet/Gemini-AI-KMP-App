# Screenshot Module 📸

This module is an internal developer tool for generating realistic, device-framed screenshots of the **Gemini AI KMP App** across multiple platforms (Android, iOS, macOS, and Web) in both **Dark Mode** and **Light Mode**.

---

## ⚠️ Important Note: Keep Commented in `settings.gradle.kts`

By default, the screenshot-related modules **must remain commented out** in [`settings.gradle.kts`](../settings.gradle.kts):

```kotlin
// Screenshot and Preview functionality only for developer internal screenshot capture
//include(":screenshot")
//include(":cmpdevicepreview")
//project(":cmpdevicepreview").projectDir = file("CMPDevicePreview/cmpdevicepreview")
```

> [!WARNING]
> **Do not uncomment these lines for regular app builds, contributions, or CI/CD pipelines.**
> Keeping them commented out ensures standard builds remain lightweight and fast without loading screenshot export dependencies.

---

## 🔍 Why is This Module Local & Commented Out?

1. **Official Library Limitations**:
   - The screenshot pipeline uses [CMPDevicePreview](https://github.com/Apadmi-Engineering/CMPDevicePreview) to render realistic device hardware bezels (e.g., Google Pixel 9, iPhone 16 Pro Dynamic Island, macOS desktop windows, and Chrome browser frames).
   - In the **official published version** of `cmpdevicepreview`, Compose Multiplatform Desktop has issues intercepting and rendering `AlertDialog`, `Dialog`, and underlying `ComposeSceneLayer` elements inside the off-screen simulated canvas.
   - Furthermore, upstream dialog appearance animations run asynchronously, causing captured dialog snapshots to render with low opacity / semi-transparent scrims.

2. **Custom Fixes & Pre-built JVM JAR (`screenshot/libs/cmpdevicepreview-jvm.jar`)**:
   - To overcome upstream issues without complicating Gradle builds, we built a standalone JVM JAR from our patched fork [Coding-Meet/CMPDevicePreview](https://github.com/Coding-Meet/CMPDevicePreview):
     - **Location**: [`screenshot/libs/cmpdevicepreview-jvm.jar`](libs/cmpdevicepreview-jvm.jar)
     - **Interception**: Direct `ComposeSceneLayer` capture for dialogs and popups in `SimulatedPlatform.skiko.kt`.
     - **Chrome Web Frame**: Added realistic web browser header with window controls, back/forward/refresh buttons, URL pill, and dark/light styling.
     - **Animation Settling**: Fast-forwarded Skiko frame clock so Material 3 dialog appearance animations settle at solid 100% opacity.

---

## 🚀 Two Ways to Use CMPDevicePreview

You can use either the pre-compiled JAR (recommended) or the direct source submodule:

### Way 1: Using Pre-compiled JAR (Default & Recommended)
Use this for everyday screenshot exports, JVM tests, or when you want zero build friction without managing submodule Gradle compatibility.

1. In [`settings.gradle.kts`](../settings.gradle.kts), **uncomment only `:screenshot`**:
   ```kotlin
   include(":screenshot")
   //include(":cmpdevicepreview")
   //project(":cmpdevicepreview").projectDir = file("CMPDevicePreview/cmpdevicepreview")
   ```
2. In [`screenshot/build.gradle.kts`](build.gradle.kts), the JAR dependency is enabled by default:
   ```kotlin
   implementation(files("libs/cmpdevicepreview-jvm.jar"))
   ```

### Way 2: Using Source Submodule (For Debugging & Developing New Frames)
If you hit an issue, need to inspect internal logs, or want to modify `CMPDevicePreview` code directly (e.g., adding new device specs, tweaking bezels, or Skiko layers):

1. In [`settings.gradle.kts`](../settings.gradle.kts), **uncomment all lines**:
   ```kotlin
   include(":screenshot")
   include(":cmpdevicepreview")
   project(":cmpdevicepreview").projectDir = file("CMPDevicePreview/cmpdevicepreview")
   ```
2. In [`screenshot/build.gradle.kts`](build.gradle.kts), switch to the project dependency:
   ```kotlin
   // implementation(files("libs/cmpdevicepreview-jvm.jar"))
   implementation(project(":cmpdevicepreview"))
   ```

---

## 📦 Reusing the JAR in Other Projects

The compiled JAR ([`screenshot/libs/cmpdevicepreview-jvm.jar`](libs/cmpdevicepreview-jvm.jar)) is completely portable:

- **No submodule needed**: If you have another Compose Multiplatform app and want realistic Pixel, iPhone, Mac, and Web browser screenshots, you don't need to clone or configure the `CMPDevicePreview` submodule.
- **How to use**:
  1. Copy `cmpdevicepreview-jvm.jar` into your other project's `libs/` directory.
  2. In your module's `build.gradle.kts`, add:
     ```kotlin
     implementation(files("libs/cmpdevicepreview-jvm.jar"))
     ```
  3. You immediately get all custom features: Compose Dialog interception, 100% solid opacity scrims, and realistic Chrome web browser frames!

> [!TIP]
> **Complete Integration Guide for AI Agents & External Projects**:
> See [`AGENT.md`](AGENT.md) for full public API references (`DevicePresets`, `SimulatedDevice`, `DeviceFrame`, `WebBrowserFrame`, `LocalDeviceScreenshots`), Gradle setup, headless export recipes, and JVM UI testing patterns.

---

## 🛠️ How to Export Screenshots & Run Tests (Developer Guide)

When you need to regenerate screenshots or run UI verification:

### Step 1: Enable the Screenshot Module
In [`settings.gradle.kts`](../settings.gradle.kts), uncomment `include(":screenshot")`.

### Step 2: Run the Export Task or JVM UI Tests

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

This runs [`AllPlatformScreenshotTest`](src/test/kotlin/com/coding/meet/gaminiaikmp/screenshot/AllPlatformScreenshotTest.kt) using Compose Multiplatform's `runComposeUiTest` directly inside the JVM without needing an emulator or physical device.

---

### Step 3: Re-comment Before Committing
Before pushing your changes to GitHub, re-comment the lines in [`settings.gradle.kts`](../settings.gradle.kts):

```kotlin
// Screenshot and Preview functionality only for developer internal screenshot capture
//include(":screenshot")
//include(":cmpdevicepreview")
//project(":cmpdevicepreview").projectDir = file("CMPDevicePreview/cmpdevicepreview")
```

---

## 🔄 How to Rebuild the JVM JAR (Advanced)

If you modify source code or device specifications inside `CMPDevicePreview` and want to update the JAR:

1. Build the JVM JAR inside `CMPDevicePreview`:
   ```bash
   cd CMPDevicePreview
   ./gradlew :cmpdevicepreview:jvmJar
   ```
2. Copy the newly generated JAR to `screenshot/libs/`:
   ```bash
   cp cmpdevicepreview/build/libs/cmpdevicepreview-jvm.jar ../screenshot/libs/
   ```
3. Test the updated JAR:
   ```bash
   cd ..
   ./gradlew :screenshot:test
   ```

