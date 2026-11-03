# Gemini AI Kotlin Multiplatform App - Chat with Gemini AI 💬✨

[![Gemini AI KMP App Preview](image/Gemini-AI-KMP-App.png)](https://youtu.be/YhWhR6IVs_Y)

Click the image above to watch a demo of the Gemini AI KMP App on YouTube.

---

## 🌟 Overview

**Gemini AI KMP App** is a production-ready, open-source **Kotlin Multiplatform (KMP)** and **Compose Multiplatform** application targeting **Android, iOS, Desktop (macOS, Windows, Linux)**, and **Web (Wasm)**. 

Powered by the latest **Google Gemini AI models** (including Gemini 3.8 Flash and Gemini 4 Argon), this app allows you to have intelligent, multimodal conversations using text and images. Conversations are stored locally with **SQLDelight** and persistent key-value configuration is handled cleanly with **Multiplatform Settings** and **KStore**.

---

## 🚀 Key Features

- 💬 **Multimodal Conversational AI**: Seamless chat experience supporting text prompts and image attachments.
- ⚡ **Latest Gemini Models**:
  - Live model discovery from the official Gemini API (`/v1beta/models`).
  - Flagship defaults: `gemini-3.8-flash` (Recommended), `gemini-4-argon`, `gemini-3.5-flash-lite`, and `gemini-3.6-flash`.
  - Context & token limits display with visual badges (e.g. *Recommended*, *Thinking*, *Gemini 4*, *Fast & Lite*).
- 🎨 **Adaptive & Responsive Material 3 UI**:
  - Adaptive Navigation Suite adapting to Phones, Foldables, Tablets, and Desktops.
  - Collapsible desktop drawer for chat history.
  - Staggered adaptive grid layout for model selection.
- 🌓 **Dynamic Theme Management**:
  - One-click switching between **System Default**, **Light**, and **Dark** themes.
  - Reactive theme state managed cleanly via `SettingRepository` and `AppViewModel`.
- 📜 **Cross-Platform Vertical Scrollbars (`AppVerticalScrollbar`)**:
  - Desktop & Web draggable scrollbars styled with Material 3 tokens.
  - Native touch-overscroll on mobile without conflicts.
  - Reverse-layout sync for chat messages.
- 🔐 **API Key & Storage Management**:
  - Easily configure, mask/reveal, or change your Google Gemini API key at runtime.
  - Data & Storage controls with a safe **Delete All Conversations** confirmation flow.
- 🏗️ **Clean Architecture & MVI**:
  - Strict separation of concerns: `UI (Compose)` ➔ `ViewModel (MVI with Intent / State / Effect)` ➔ `Domain (Use Cases)` ➔ `Data (Repositories & Data Sources)`.
  - Lifecycle-aware one-off effect handling with `ObserveAsEvents`.

---

## 🛠️ Tech Stack & Architecture

| Layer / Domain | Technology |
| :--- | :--- |
| **Language & Platform** | Kotlin Multiplatform (2.4.x), Compose Multiplatform (1.12.x) |
| **Build Tools** | Android Gradle Plugin (AGP 9.1.x), Gradle 9.x |
| **Architecture** | Clean Architecture + MVI (Model-View-Intent) + Single Source of Truth |
| **UI & Theming** | Jetpack Compose Multiplatform, Material 3, Adaptive Layouts, Jetpack Navigation 3 |
| **Dependency Injection** | Koin 4.2.x (`koin-compose-viewmodel`) |
| **Networking** | Ktor 3.6.x (Content Negotiation, Kotlinx Serialization) |
| **Local Database** | SQLDelight 2.4.x (SQLite on Mobile/Desktop, SQL.js on Web) |
| **Key-Value Persistence**| Multiplatform Settings & KStore |
| **File / Image Picking**| FileKit 0.17.x (Native cross-platform image picker) |
| **Logging** | Touchlab Kermit |
| **BuildConfig** | BuildKonfig |

---

## 📦 Project Structure

The project follows a modern modular KMP setup:

```text
Gemini-AI-KMP-App/
├── androidApp/        # Android application entry point & Manifest
├── desktopApp/        # JVM Desktop application entry point (macOS, Windows, Linux)
├── webApp/            # Web (WasmJs) application entry point & webpack configurations
├── iosApp/            # iOS Xcode project (SwiftUI wrapper)
└── shared/            # Shared multiplatform core module
    ├── commonMain/    # Core domain models, use cases, repositories, screens & UI
    ├── androidMain/   # Android-specific drivers & platform utilities
    ├── jvmMain/       # Desktop JVM implementations (Scrollbars, DB drivers)
    ├── wasmJsMain/    # WebAssembly implementations
    └── iosMain/       # iOS platform hooks
```

---

## 🚀 Getting Started

### Prerequisites

- **JDK 21** (e.g. Azul Zulu JDK 21)
- **Android Studio** Ladybug Feature Drop (or newer)
- Xcode (for iOS builds on macOS)

### Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Coding-Meet/Gemini-AI-KMP-App.git
   cd Gemini-AI-KMP-App
   ```

2. **Configure Gemini API Key**:
   - Obtain an API key from [Google AI Studio](https://aistudio.google.com).
   - Create a `local.properties` file in the project root:
     ```properties
     GEMINI_API_KEY=YOUR_GEMINI_API_KEY
     ```
   *(Note: You can also configure or update your API key directly within the app's Settings screen at any time).*

3. **Generate SQLDelight Interfaces**:
   Run the following task in the terminal or Android Studio Gradle panel:
   ```bash
   ./gradlew :shared:generateCommonMainGeminiApiChatDBInterface
   ```

---

### Running the App

- **Android**:
  Select the `androidApp` run configuration in Android Studio and hit **Run** (`Shift + F10`).
  Or via terminal:
  ```bash
  ./gradlew :androidApp:installDebug
  ```

- **Desktop (macOS / Windows / Linux)**:
  ```bash
  ./gradlew :desktopApp:run
  ```

- **Web (Wasm / Browser)**:
  ```bash
  ./gradlew :webApp:wasmJsBrowserDevelopmentRun
  ```

- **iOS**:
  Open `iosApp/iosApp.xcodeproj` in Xcode and press **Run**, or run the `iosApp` configuration from Android Studio with the KMP plugin.

---

## 📸 Screenshots

### Android
<table>
   <tr>
    <td><img src="image/android/android1.png" alt="android"></td>
    <td><img src="image/android/android2.png" alt="android"></td>
   </tr>
   <tr>
    <td><img src="image/android/android3.png" alt="android"></td>
    <td><img src="image/android/android4.png" alt="android"></td>
   </tr>
   <tr>
    <td><img src="image/android/android5.png" alt="android"></td>
    <td><img src="image/android/android6.png" alt="android"></td>
   </tr>
   <tr>
    <td><img src="image/android/android7.png" alt="android"></td>
    <td><img src="image/android/android8.png" alt="android"></td>
   </tr>
   <tr>
    <td><img src="image/android/android9.png" alt="android"></td>
   </tr>
</table>

### Windows & Desktop
<table>
  <tr>
    <td><img src="image/windows/win1.png" alt="win"></td>
    <td><img src="image/windows/win2.png" alt="win"></td>
  </tr>
  <tr>
    <td><img src="image/windows/win3.png" alt="win"></td>
    <td><img src="image/windows/win4.png" alt="win"></td>
  </tr>
  <tr>
    <td><img src="image/windows/win5.png" alt="win"></td>
    <td><img src="image/windows/win6.png" alt="win"></td>
  </tr>
</table>

### macOS
<table>
   <tr>
      <td><img src="image/mac/mac1.jpeg" alt="mac"></td>
      <td><img src="image/mac/mac2.png" alt="mac"></td>
   </tr>
   <tr>
      <td><img src="image/mac/mac3.jpeg" alt="mac"></td>
      <td><img src="image/mac/mac4.jpeg" alt="mac"></td>
   </tr>
</table>

### Web (Wasm)
<table>
   <tr>
      <td><img src="image/web/web1.png" alt="web"></td>
      <td><img src="image/web/web2.png" alt="web"></td>
   </tr>
   <tr>
      <td><img src="image/web/web3.png" alt="web"></td>
      <td><img src="image/web/web4.png" alt="web"></td>
   </tr>
</table>

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/Coding-Meet/Gemini-AI-KMP-App/issues).

---

## ❤️ Show Your Support

Give a ⭐️ if this project helped you!

<a href="https://www.buymeacoffee.com/codingmeet" target="_blank">
<img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" width="160">
</a>

---

## 🌐 Connect with Me

[![](https://img.shields.io/badge/Youtube-red?style=for-the-badge&logo=youtube&logoColor=white)](https://youtube.com/@CodingMeet26?si=FuKwU-aBaf_5kukR)
[![](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/coding-meet-a74933273/)
[![](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/CodingMeet)

---

## 👤 Author

**Meet**
