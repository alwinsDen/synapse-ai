# AGENTS.md

This file provides guidance to AI coding agents (Claude Code, Gemini CLI, Codex, Cursor, etc.) when working with code in this repository.

> **Note for agents**: This is the source of truth. `CLAUDE.md` is auto-generated from this file during Gradle sync — do not edit it directly.

## Agent Rules

### Commit and Documentation Policy
- **IMPORTANT**: Whenever you make a commit that affects or adds any new architectural changes, you MUST update the README.md file to reflect those changes.
- Keep README.md synchronized with architectural decisions, new features, and significant structural changes.
- README.md is user-facing documentation; AGENTS.md is for AI agent instructions only.
- **NEVER commit automatically**: Always stage files with `git add`, draft the commit message, then ask the user to review before executing `git commit`. Wait for explicit approval before committing.
- **Do NOT include Co-Authored-By line**: Do not add "Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>" or any similar attribution in commit messages.

## Project Overview

Project Dino is a Kotlin Multiplatform project with three modules:
- **composeApp**: Cross-platform UI (Android/iOS) using Compose Multiplatform
- **shared**: Platform-agnostic business logic and HTTP client
- **server**: Ktor-based backend for authentication and API services

## Build Commands

### Android
Build debug APK:
```bash
./gradlew :composeApp:assembleDebug
```

Run app (use IDE run configuration or install APK to device/emulator)

### iOS
Build and sync iOS framework:
```bash
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

Open `iosApp/iosApp.xcodeproj` in Xcode and run from there.

Note: The build automatically triggers `XCodeBuildKonfigGenerator` task which generates `iosApp/Configuration/Google.xcconfig` from `secret.properties`.

### Server
Run Ktor server:
```bash
./gradlew :server:run
```

Server starts on `http://127.0.0.1:5432` by default (configurable in `server/src/main/resources/application.conf`).

### Tests
Run all tests:
```bash
./gradlew test
```

Run specific module tests:
```bash
./gradlew :composeApp:test
./gradlew :server:test
./gradlew :shared:test
```

### Clean Build
```bash
./gradlew clean
./gradlew build
```

## Architecture

### Module Interaction Flow
```
composeApp (UI Layer)
    ↓ calls shared APIs
shared (Business Logic & HTTP Client)
    ↓ makes HTTP requests
server (Ktor Backend) ↔ Valkey (Cache/Session Store)
                      ↔ PostgreSQL (User Data)
```

### Navigation System
- Uses `androidx.navigation.compose` with type-safe serialization
- Routes defined in `composeApp/src/commonMain/kotlin/com/alwinsden/dino/navigation/routeSerializers.kt`:
  - `LoginWindow`: Authentication / startup screen
  - `BotWindow`: AI model selection screen
  - `BotChatWindow`: Main chat interface
- All navigation managed through `NavigationController` singleton in `navHostClass.kt`
- **Start destination**: `BotChatWindow` (chat screen loads first by design)

### Screens

**UserStartupPage** (`startup/UserStartupPage.kt`)
- Yellow background (`0xffF3DB00`), EB Garamond branding
- Google + Apple sign-in options
- Calls `createNonce()` on load to prefetch a nonce from server

**BotInterface** (`botInterface/BotInterface.kt`)
- AI model selection screen with greeting and `BotTextField`
- Routes to `BotChatWindow` on send

**BotChatInterface** (`botChatInterface/BotChatInterface.kt`)
- Three-zone layout:
  - **Top bar (fixed)**: Settings, date, logout, history buttons
  - **Chat content (scrollable)**: Alternating `UserCreatedField` / `AiUpdatedField` bubbles, 70% width constraint
  - **Bottom input (fixed)**: `BotTextField` with shadow and rounded top corners
- Wraps content in `HistoryDrawer`

**SettingsInterface** (`settingsInterface/SettingsInterface.kt`)
- Added but currently empty — not yet implemented

### Chat Components

**`UserCreatedField`** (`botChatInterface/components/UserInputFields.kt`)
- Right-aligned message bubble, gray background (`0xffF2F2F2`)
- Rounded corners except bottom-right (15dp radius)

**`AiUpdatedField`** (`botChatInterface/components/AiUpdatedField.kt`)
- Left-aligned AI response with diamond icon
- Action buttons: thumbs up, thumbs down, copy

**`BotTextField`** (`botInterface/components/BotTextField.kt`)
- Expandable multi-line input (max 200dp)
- Image attachment button (no-op currently)
- Send button: gray when empty, green (`0xff23D76E`) when filled
- AI model selection bottom sheet (modal); default model: `ModelDefinitions.CLAUDE`

**`HistoryDrawer`** (`historyDrawer/HistoryDrawer.kt`)
- `ModalNavigationDrawer` wrapper, 80% screen width
- Scrollable conversation history list (`weight(1f)` + `verticalScroll`)
- Fixed bottom section: user email + settings icon

### Authentication Architecture
The app supports Google Sign-In and Apple Sign-In with nonce-based security:

1. **Nonce Generation**: Client calls `createNonce()` → server generates UUID → stored in Valkey with 60s TTL
2. **Platform Sign-In**:
   - Android: `androidx.credentials` API (`GoogleAuthProvider.android.kt`)
   - iOS: CocoaPods GoogleSignIn SDK + native Sign in with Apple (`GoogleAuthProvider.ios.kt`, `AppleAuthProvider.ios.kt`)
3. **Token Verification**: Server validates Google ID token and checks nonce in Valkey cache
4. **User Persistence**: Verified user stored/updated in PostgreSQL via `UserInfoDbActions`
5. **JWT Generation**: TODO — currently returns placeholder text after validation

Key files:
- `composeApp/src/commonMain/kotlin/com/alwinsden/dino/sheets/authentication/GoogleAuthProvider.kt` (expect)
- `composeApp/src/commonMain/kotlin/com/alwinsden/dino/sheets/authentication/AppleAuthProvider.kt` (expect)
- `composeApp/src/androidMain/kotlin/.../GoogleAuthProvider.android.kt` (actual)
- `composeApp/src/iosMain/kotlin/.../GoogleAuthProvider.ios.kt` (actual)
- `composeApp/src/iosMain/kotlin/.../AppleAuthProvider.ios.kt` (actual)
- `composeApp/src/commonMain/kotlin/.../SignInMethod.kt` — `ClickableContinueWithGoogle`, `ClickableContinueWithApple` composables
- `composeApp/src/commonMain/kotlin/.../handleForwardAuth.kt` — `handleReceivedGoogleTokenId()` (TODO: send token to server)
- `server/src/main/kotlin/com/alwinsden/dino/googleAuthn/serverManager/serverManager.kt`

### HTTP Request Management
Centralized in `shared/src/commonMain/kotlin/com/alwinsden/dino/requestManager/`:
- `RequestManager`: Wraps Ktor HttpClient with platform-specific base URLs
- `IClientInterface`: Abstraction for platform-specific configuration
- Extension functions for endpoints: `healthCheck()`, `createNonce()` (in `get/generalManagement.kt`)
- Platform-specific engines:
  - Android: OkHttp (`shared/src/androidMain`)
  - iOS: Darwin/URLSession (`shared/src/iosMain`)
- `ClientKtorConfiguration` (in `utilities/UI/keyConfigurations.kt`) — implements `IClientInterface`, reads `BuildKonfig.KTOR_ENTRY_URL`

### Error Handling
- Custom `CustomInAppException(appCode, message)` hierarchy in `shared/src/commonMain/kotlin/com/alwinsden/dino/requestManager/utils/`
- `ErrorObjectCustom` — serializable error DTO returned by server
- `ErrorTypeEnums` — CUSTOM(0), UNCONTROLLED_EXCEPTION(1), UNCONTROLLED_STATE(2), UNCONTROLLED_THROWABLE(3)
- `ErrorString` map — error code → human-readable message (e.g. 1000: "Invalid security token.")
- Server uses Ktor `StatusPages` plugin to map exceptions to HTTP responses:
  - `CustomInAppException` → 400
  - `IllegalArgumentException` → 400
  - `IllegalStateException` → 401
  - `Throwable` → 500

### Configuration Management

**Build-time secrets** (via BuildKonfig plugin):
- Reads from `secret.properties` (git-ignored, use `refer.secret.properties` as template)
- Generates `BuildKonfig` object with platform-specific values:
  - `CLIENT_ID_GOOGLE_AUTH`
  - `KTOR_ENTRY_URL` (different for Android/iOS)
  - `IOS_CLIENT_ID`, `IOS_REVERSE_CLIENT_ID`
- For iOS: Auto-generates `iosApp/Configuration/Google.xcconfig` during build

**Runtime configuration**:
- Server: `server/src/main/resources/application.conf` for Ktor settings, Valkey connection, Google audience, PostgreSQL credentials

## Key Technology Choices

### Compose Multiplatform (v1.9.3)
- Shared UI code between Android and iOS
- Material 3 design components
- Platform-specific implementations use `expect`/`actual` keywords

### Ktor (v3.3.3)
- **Client**: Multiplatform HTTP client with OkHttp (Android) and Darwin (iOS) engines
- **Server**: Netty-based backend with ContentNegotiation and StatusPages plugins

### Valkey/Glide (v2.2.5)
- Redis-compatible cache for nonce/session storage
- Server-side only (JVM target)
- `ValkeyManager` singleton in `server/src/main/kotlin/com/alwinsden/dino/valkeyManager/glideInitiator.kt`
- **Important**: The dependency uses platform-specific classifiers. Current setup uses `osx-aarch_64`. Change classifier in `server/build.gradle.kts` when building for other platforms.

### PostgreSQL + Exposed ORM
- User data persistence on the server
- `UserInfo` table: `id` (UUID PK), `googleSubjectId` (unique), `userFullName`, `userGoogleProfile`, `userEmail`
- Table defined in `server/src/main/kotlin/com/alwinsden/dino/googleAuthn/serverManager/tables/userInfo_table.kt`
- Connection credentials read from `application.conf`

### CocoaPods Integration
- iOS native dependencies managed via `shared/build.gradle.kts` cocoapods block
- Currently integrates GoogleSignIn pod
- Podfile location: `iosApp/Podfile`

## Utilities

### Font Library (`utilities/UI/fontLibrary.kt`)
- `DefaultFontStylesDataClass` — configuration for text styles (fontSize, fontFamily, colorInt, lineHeight)
- `defaultFontStyle()` — composable returning `TextStyle`
- `FontLibrary` object: `ebGaramond()` (4 weights), `inter()` (6 weights)

### Key Configurations (`utilities/UI/keyConfigurations.kt`)
- `ClientKtorConfiguration` — implements `IClientInterface`
- `Defaults` object — constants (e.g. `DEFAULT`)
- `PageDefaults` object — mode constants for `BotTextField`
- `listModelDefinitions` — list of available AI models

### Model Definitions (`botInterface/components/enums.kt`)
```kotlin
enum class ModelDefinitions {
    CLAUDE,   // Claude Opus
    LOCL_V1   // Locl V1
}
```

### User Blocking / Loaders (`utilities/UI/userBlocking.kt`)
- `DialogLoader(isLoading: Boolean)` — full-screen transparent dialog with `CircularProgressIndicator`

## Platform-Specific Considerations

### Android
- Target SDK 36, Min SDK 24
- Modern Credentials API for authentication (not legacy GoogleSignIn)
- JVM 11 compilation target
- Entry point: `composeApp/src/androidMain/kotlin/.../MainActivity.kt`

### iOS
- Deployment target: iOS 26.0
- Compose compiles to Swift-compatible framework (`SharedFramework`)
- SwiftUI wrapper bridges Kotlin Compose UI
- Native authentication via CocoaPods GoogleSignIn + Sign in with Apple
- Entry point: `composeApp/src/iosMain/kotlin/.../MainViewController.kt`

### Server (JVM)
- Main class: `com.alwinsden.dino.KtorStartKt` in `server/src/main/kotlin/com/alwinsden/dino/KtorStart.kt`
- Lazy Valkey initialization on startup
- Endpoints:
  - `GET /health`: Health check → `"Ktor is healthy."`
  - `GET /generate-nonce`: UUID generation with Valkey caching
  - `POST /login`: Google token verification + user upsert in PostgreSQL

## Development Workflow

### Adding New Screens
1. Define serializable route in `navigation/routeSerializers.kt`
2. Add composable screen in `composeApp/src/commonMain/kotlin/`
3. Register route in `NavigationController` (navHostClass.kt)

### Adding Server Endpoints
1. Define route in `server/src/main/kotlin/com/alwinsden/dino/KtorStart.kt`
2. Add corresponding extension function in `shared/src/commonMain/kotlin/com/alwinsden/dino/requestManager/get/`
3. Use `RequestManager` instance from client to call endpoint

### Platform-Specific Code
1. Declare `expect` function/class in `commonMain`
2. Provide `actual` implementation in `androidMain`, `iosMain`, or `jvmMain`
3. Example: See `GoogleAuthProvider.kt` / `AppleAuthProvider.kt` authentication implementations

### Managing Secrets
- Never commit `secret.properties` to git
- Use `refer.secret.properties` as a template for required keys
- iOS builds require `XCodeBuildKonfigGenerator` task to sync secrets to Xcode
- Server reads secrets from environment or config files

## Multiplatform Best Practices

### Code Sharing Strategy
- Business logic and data models → `commonMain`
- Platform-specific UI behavior → platform-specific `Main` folders
- Server-specific code → `jvmMain` (in `shared` module) or `server` module
- Prefer `expect`/`actual` over conditional compilation

### Dependency Management
- Common dependencies in `commonMain.dependencies`
- Platform-specific dependencies in `androidMain.dependencies`, `iosMain.dependencies`, etc.
- Use version catalogs (`gradle/libs.versions.toml`) for version management

### Testing Strategy
- Unit tests in `commonTest` for shared business logic
- Platform-specific tests in `androidTest`, `iosTest`, `jvmTest`
- Server integration tests use Ktor's `testApplication` in `server/src/test/`

## Common Issues

### iOS Build Failures
- Ensure `pod install` has been run in `iosApp/` directory
- Check that `XCodeBuildKonfigGenerator` task completed successfully
- Verify `secret.properties` contains all iOS-specific keys

### Server Connection Issues
- Android emulator: Use `10.0.2.2` instead of `localhost` for host machine access
- iOS simulator: Use `localhost` or `127.0.0.1` directly
- Ensure server is running before testing authentication flows

### Valkey/Glide Platform Compatibility
- Current configuration uses macOS ARM64 classifier
- For other platforms, update the classifier in `server/build.gradle.kts`:
  - Linux x64: `linux-x86_64`
  - Windows x64: `windows-x86_64`
- See Valkey Glide documentation for full platform support

### Authentication Nonce Expiration
- Nonces expire after 60 seconds in Valkey cache
- If sign-in takes too long, request a new nonce
- One-time use: Nonce cleared from cache after first validation attempt
