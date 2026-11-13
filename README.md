# Gemini AI Kotlin Multiplatform App - Chat with Gemini AI 💬✨

[![Gemini AI KMP App Preview](image/Gemini-AI-KMP-App.png)](https://youtu.be/YhWhR6IVs_Y)

Click the image above to watch a demo of the Gemini AI KMP App on YouTube.

---

## 🌟 Overview

**Gemini AI KMP App** is a production-ready, open-source **Kotlin Multiplatform (KMP)** and **Compose Multiplatform** application targeting **Android, iOS, Desktop (macOS, Windows, Linux)**, and **Web (Wasm)**.

Powered by the latest **Google Gemini AI models** (including Gemini 3.8 Flash and Gemini 4 Argon), this app allows you to have intelligent, multimodal conversations using text and images. Conversations are stored locally with **SQLDelight** and persistent key-value configuration is handled cleanly with **Multiplatform Settings**.

---

## 🚀 Key Features

- 💬 **Multimodal Conversational AI**: Seamless chat experience supporting text prompts and image attachments.
- ⚡ **Latest Gemini Models**:
  - Live model discovery from the official Gemini API (`/v1beta/models`).
  - Flagship defaults: `gemini-3.8-flash` (Recommended), `gemini-4-argon`, `gemini-3.5-flash-lite`, and `gemini-3.6-flash`.
  - Context & token limits display with visual badges (e.g. _Recommended_, _Thinking_, _Gemini 4_, _Fast & Lite_).
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

| Layer / Domain            | Technology                                                                        |
| :------------------------ | :-------------------------------------------------------------------------------- |
| **Language & Platform**   | Kotlin Multiplatform (2.4.x), Compose Multiplatform (1.12.x)                      |
| **Build Tools**           | Android Gradle Plugin (AGP 9.1.x), Gradle 9.x                                     |
| **Architecture**          | Clean Architecture + MVI (Model-View-Intent) + Single Source of Truth             |
| **UI & Theming**          | Jetpack Compose Multiplatform, Material 3, Adaptive Layouts, Jetpack Navigation 3 |
| **Dependency Injection**  | Koin 4.2.x (`koin-compose-viewmodel`)                                             |
| **Networking**            | Ktor 3.6.x (Content Negotiation, Kotlinx Serialization)                           |
| **Local Database**        | SQLDelight 2.4.x (SQLite on Mobile/Desktop, SQL.js on Web)                        |
| **Key-Value Persistence** | Multiplatform Settings                                                            |
| **File / Image Picking**  | FileKit 0.17.x (Native cross-platform image picker)                               |
| **Logging**               | Touchlab Kermit                                                                   |
| **BuildConfig**           | BuildKonfig                                                                       |

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
     _(Note: You can also configure or update your API key directly within the app's Settings screen at any time)._

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

### Android (Google Pixel 9)

#### 🌙 Dark Mode
<table>
   <tr>
    <td><img src="image/android/dark/01_welcome_screen.png" alt="Welcome Screen"></td>
    <td><img src="image/android/dark/02_add_api_key_dialog.png" alt="Add API Key Dialog"></td>
    <td><img src="image/android/dark/03_new_group_dialog.png" alt="New Group Dialog"></td>
   </tr>
   <tr>
    <td><img src="image/android/dark/04_conversations_list.png" alt="Conversations List"></td>
    <td><img src="image/android/dark/05_empty_chat_screen.png" alt="Empty Chat Screen"></td>
    <td><img src="image/android/dark/06_question_sending_loading.png" alt="Sending & Loading"></td>
   </tr>
   <tr>
    <td><img src="image/android/dark/07_gemini_markdown_response.png" alt="Gemini Markdown Response"></td>
    <td><img src="image/android/dark/08_multimodal_image_prompt.png" alt="Multimodal Prompt"></td>
    <td><img src="image/android/dark/09_gemini_vision_response.png" alt="Gemini Vision Response"></td>
   </tr>
   <tr>
    <td><img src="image/android/dark/10_delete_chat_dialog.png" alt="Delete Chat Dialog"></td>
    <td><img src="image/android/dark/11_settings_screen.png" alt="Settings Screen"></td>
    <td><img src="image/android/dark/12_edit_api_key_dialog.png" alt="Edit API Key Dialog"></td>
   </tr>
   <tr>
    <td><img src="image/android/dark/13_delete_all_chats_dialog.png" alt="Delete All Chats Dialog"></td>
    <td></td>
    <td></td>
   </tr>
</table>

#### ☀️ Light Mode
<table>
   <tr>
    <td><img src="image/android/light/01_welcome_screen.png" alt="Welcome Screen"></td>
    <td><img src="image/android/light/02_add_api_key_dialog.png" alt="Add API Key Dialog"></td>
    <td><img src="image/android/light/03_new_group_dialog.png" alt="New Group Dialog"></td>
   </tr>
   <tr>
    <td><img src="image/android/light/04_conversations_list.png" alt="Conversations List"></td>
    <td><img src="image/android/light/05_empty_chat_screen.png" alt="Empty Chat Screen"></td>
    <td><img src="image/android/light/06_question_sending_loading.png" alt="Sending & Loading"></td>
   </tr>
   <tr>
    <td><img src="image/android/light/07_gemini_markdown_response.png" alt="Gemini Markdown Response"></td>
    <td><img src="image/android/light/08_multimodal_image_prompt.png" alt="Multimodal Prompt"></td>
    <td><img src="image/android/light/09_gemini_vision_response.png" alt="Gemini Vision Response"></td>
   </tr>
   <tr>
    <td><img src="image/android/light/10_delete_chat_dialog.png" alt="Delete Chat Dialog"></td>
    <td><img src="image/android/light/11_settings_screen.png" alt="Settings Screen"></td>
    <td><img src="image/android/light/12_edit_api_key_dialog.png" alt="Edit API Key Dialog"></td>
   </tr>
   <tr>
    <td><img src="image/android/light/13_delete_all_chats_dialog.png" alt="Delete All Chats Dialog"></td>
    <td></td>
    <td></td>
   </tr>
</table>

### iOS (iPhone 16 Pro)

#### 🌙 Dark Mode
<table>
   <tr>
    <td><img src="image/ios/dark/01_welcome_screen.png" alt="Welcome Screen"></td>
    <td><img src="image/ios/dark/02_add_api_key_dialog.png" alt="Add API Key Dialog"></td>
    <td><img src="image/ios/dark/03_new_group_dialog.png" alt="New Group Dialog"></td>
   </tr>
   <tr>
    <td><img src="image/ios/dark/04_conversations_list.png" alt="Conversations List"></td>
    <td><img src="image/ios/dark/05_empty_chat_screen.png" alt="Empty Chat Screen"></td>
    <td><img src="image/ios/dark/06_question_sending_loading.png" alt="Sending & Loading"></td>
   </tr>
   <tr>
    <td><img src="image/ios/dark/07_gemini_markdown_response.png" alt="Gemini Markdown Response"></td>
    <td><img src="image/ios/dark/08_multimodal_image_prompt.png" alt="Multimodal Prompt"></td>
    <td><img src="image/ios/dark/09_gemini_vision_response.png" alt="Gemini Vision Response"></td>
   </tr>
   <tr>
    <td><img src="image/ios/dark/10_delete_chat_dialog.png" alt="Delete Chat Dialog"></td>
    <td><img src="image/ios/dark/11_settings_screen.png" alt="Settings Screen"></td>
    <td><img src="image/ios/dark/12_edit_api_key_dialog.png" alt="Edit API Key Dialog"></td>
   </tr>
   <tr>
    <td><img src="image/ios/dark/13_delete_all_chats_dialog.png" alt="Delete All Chats Dialog"></td>
    <td></td>
    <td></td>
   </tr>
</table>

#### ☀️ Light Mode
<table>
   <tr>
    <td><img src="image/ios/light/01_welcome_screen.png" alt="Welcome Screen"></td>
    <td><img src="image/ios/light/02_add_api_key_dialog.png" alt="Add API Key Dialog"></td>
    <td><img src="image/ios/light/03_new_group_dialog.png" alt="New Group Dialog"></td>
   </tr>
   <tr>
    <td><img src="image/ios/light/04_conversations_list.png" alt="Conversations List"></td>
    <td><img src="image/ios/light/05_empty_chat_screen.png" alt="Empty Chat Screen"></td>
    <td><img src="image/ios/light/06_question_sending_loading.png" alt="Sending & Loading"></td>
   </tr>
   <tr>
    <td><img src="image/ios/light/07_gemini_markdown_response.png" alt="Gemini Markdown Response"></td>
    <td><img src="image/ios/light/08_multimodal_image_prompt.png" alt="Multimodal Prompt"></td>
    <td><img src="image/ios/light/09_gemini_vision_response.png" alt="Gemini Vision Response"></td>
   </tr>
   <tr>
    <td><img src="image/ios/light/10_delete_chat_dialog.png" alt="Delete Chat Dialog"></td>
    <td><img src="image/ios/light/11_settings_screen.png" alt="Settings Screen"></td>
    <td><img src="image/ios/light/12_edit_api_key_dialog.png" alt="Edit API Key Dialog"></td>
   </tr>
   <tr>
    <td><img src="image/ios/light/13_delete_all_chats_dialog.png" alt="Delete All Chats Dialog"></td>
    <td></td>
    <td></td>
   </tr>
</table>

### macOS (Two-Pane Desktop)

#### 🌙 Dark Mode
<table>
   <tr>
      <td><img src="image/mac/dark/01_two_pane_welcome.png" alt="macOS Welcome"></td>
      <td><img src="image/mac/dark/02_two_pane_add_api_key_dialog.png" alt="macOS Add Key"></td>
   </tr>
   <tr>
      <td><img src="image/mac/dark/03_two_pane_new_group_dialog.png" alt="macOS New Group"></td>
      <td><img src="image/mac/dark/04_two_pane_chat_loading.png" alt="macOS Sending & Loading"></td>
   </tr>
   <tr>
      <td><img src="image/mac/dark/05_two_pane_chat_response.png" alt="macOS Chat Markdown"></td>
      <td><img src="image/mac/dark/06_two_pane_multimodal_vision.png" alt="macOS Vision"></td>
   </tr>
   <tr>
      <td><img src="image/mac/dark/07_two_pane_delete_chat_dialog.png" alt="macOS Delete Chat"></td>
      <td><img src="image/mac/dark/08_two_pane_settings_screen.png" alt="macOS Settings"></td>
   </tr>
   <tr>
      <td><img src="image/mac/dark/09_two_pane_edit_api_key_dialog.png" alt="macOS Edit Key"></td>
      <td><img src="image/mac/dark/10_two_pane_delete_all_chats_dialog.png" alt="macOS Delete All"></td>
   </tr>
</table>

#### ☀️ Light Mode
<table>
   <tr>
      <td><img src="image/mac/light/01_two_pane_welcome.png" alt="macOS Welcome"></td>
      <td><img src="image/mac/light/02_two_pane_add_api_key_dialog.png" alt="macOS Add Key"></td>
   </tr>
   <tr>
      <td><img src="image/mac/light/03_two_pane_new_group_dialog.png" alt="macOS New Group"></td>
      <td><img src="image/mac/light/04_two_pane_chat_loading.png" alt="macOS Sending & Loading"></td>
   </tr>
   <tr>
      <td><img src="image/mac/light/05_two_pane_chat_response.png" alt="macOS Chat Markdown"></td>
      <td><img src="image/mac/light/06_two_pane_multimodal_vision.png" alt="macOS Vision"></td>
   </tr>
   <tr>
      <td><img src="image/mac/light/07_two_pane_delete_chat_dialog.png" alt="macOS Delete Chat"></td>
      <td><img src="image/mac/light/08_two_pane_settings_screen.png" alt="macOS Settings"></td>
   </tr>
   <tr>
      <td><img src="image/mac/light/09_two_pane_edit_api_key_dialog.png" alt="macOS Edit Key"></td>
      <td><img src="image/mac/light/10_two_pane_delete_all_chats_dialog.png" alt="macOS Delete All"></td>
   </tr>
</table>

### Web (Wasm Two-Pane Browser)

#### 🌙 Dark Mode
<table>
   <tr>
      <td><img src="image/web/dark/01_web_welcome.png" alt="Web Welcome"></td>
      <td><img src="image/web/dark/02_web_add_api_key_dialog.png" alt="Web Add Key"></td>
   </tr>
   <tr>
      <td><img src="image/web/dark/03_web_new_group_dialog.png" alt="Web New Group"></td>
      <td><img src="image/web/dark/04_web_chat_loading.png" alt="Web Sending & Loading"></td>
   </tr>
   <tr>
      <td><img src="image/web/dark/05_web_chat_response.png" alt="Web Chat Markdown"></td>
      <td><img src="image/web/dark/06_web_multimodal_vision.png" alt="Web Vision"></td>
   </tr>
   <tr>
      <td><img src="image/web/dark/07_web_delete_chat_dialog.png" alt="Web Delete Chat"></td>
      <td><img src="image/web/dark/08_web_settings_screen.png" alt="Web Settings"></td>
   </tr>
   <tr>
      <td><img src="image/web/dark/09_web_edit_api_key_dialog.png" alt="Web Edit Key"></td>
      <td><img src="image/web/dark/10_web_delete_all_chats_dialog.png" alt="Web Delete All"></td>
   </tr>
</table>

#### ☀️ Light Mode
<table>
   <tr>
      <td><img src="image/web/light/01_web_welcome.png" alt="Web Welcome"></td>
      <td><img src="image/web/light/02_web_add_api_key_dialog.png" alt="Web Add Key"></td>
   </tr>
   <tr>
      <td><img src="image/web/light/03_web_new_group_dialog.png" alt="Web New Group"></td>
      <td><img src="image/web/light/04_web_chat_loading.png" alt="Web Sending & Loading"></td>
   </tr>
   <tr>
      <td><img src="image/web/light/05_web_chat_response.png" alt="Web Chat Markdown"></td>
      <td><img src="image/web/light/06_web_multimodal_vision.png" alt="Web Vision"></td>
   </tr>
   <tr>
      <td><img src="image/web/light/07_web_delete_chat_dialog.png" alt="Web Delete Chat"></td>
      <td><img src="image/web/light/08_web_settings_screen.png" alt="Web Settings"></td>
   </tr>
   <tr>
      <td><img src="image/web/light/09_web_edit_api_key_dialog.png" alt="Web Edit Key"></td>
      <td><img src="image/web/light/10_web_delete_all_chats_dialog.png" alt="Web Delete All"></td>
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
