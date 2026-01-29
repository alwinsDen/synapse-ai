# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Claude-Specific Rules

### Commit and Documentation Policy
- **IMPORTANT**: Whenever you make a commit that affects or adds any new architectural changes, you MUST update the README.md file to reflect those changes.
- Keep README.md synchronized with architectural decisions, new features, and significant structural changes.
- README.md is user-facing documentation; CLAUDE.md is for Claude Code instructions only.

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
```

### Navigation System
- Uses `androidx.navigation.compose` with type-safe serialization
- Three main routes defined in `composeApp/src/commonMain/kotlin/com/alwinsden/dino/navigation/routeSerializers.kt`:
  - `LoginWindow`: Authentication screen
  - `BotWindow`: AI model selection
  - `BotChatWindow`: Chat interface
- All navigation managed through `NavigationController` singleton in `navHostClass.kt`

### Authentication Architecture
The app uses Google Sign-In with nonce-based security:

1. **Nonce Generation**: Client requests nonce from server → stored in Valkey with 60s TTL
2. **Platform Sign-In**:
   - Android: Uses `androidx.credentials` API with Google ID integration
   - iOS: Uses CocoaPods GoogleSignIn SDK (wrapped in `GoogleAuthenticator`)
3. **Token Verification**: Server validates Google ID token and checks nonce in Valkey cache
4. **JWT Generation**: TODO - currently returns success after validation

Key files:
- `composeApp/src/commonMain/kotlin/com/alwinsden/dino/sheets/authentication/ContinueWithGoogle.kt` (expect)
- `composeApp/src/androidMain/kotlin/...ContinueWithGoogle.android.kt` (actual)
- `composeApp/src/iosMain/kotlin/...ContinueWithGoogle.ios.kt` (actual)
- `server/src/main/kotlin/com/alwinsden/dino/googleAuthn/serverManager/serverManager.kt`

### HTTP Request Management
Centralized in `shared/src/commonMain/kotlin/com/alwinsden/dino/requestManager/`:
- `RequestManager`: Wraps Ktor HttpClient with platform-specific base URLs
- `IClientInterface`: Abstraction for platform-specific configuration
- Extension functions for endpoints: `healthCheck()`, `createNonce()`
- Platform-specific engines:
  - Android: OkHttp (`shared/src/androidMain`)
  - iOS: Darwin/URLSession (`shared/src/iosMain`)

### Error Handling
- Custom `CustomInAppException` hierarchy in `shared/src/commonMain/kotlin/com/alwinsden/dino/requestManager/utils/`
- Server uses Ktor `StatusPages` plugin to map exceptions to HTTP responses
- Serialized errors via `ErrorObjectCustom` for consistent client-side handling

### Configuration Management

**Build-time secrets** (via BuildKonfig plugin):
- Reads from `secret.properties` (git-ignored, use `refer.secret.properties` as template)
- Generates `BuildKonfig` object with platform-specific values:
  - `CLIENT_ID_GOOGLE_AUTH`
  - `KTOR_ENTRY_URL` (different for Android/iOS)
  - `IOS_CLIENT_ID`, `IOS_REVERSE_CLIENT_ID`
- For iOS: Auto-generates `iosApp/Configuration/Google.xcconfig` during build

**Runtime configuration**:
- Server: `server/src/main/resources/application.conf` for Ktor settings, Valkey connection, Google audience

## Key Technology Choices

### Compose Multiplatform (v1.9.3)
- Shared UI code between Android and iOS
- Material 3 design components
- Platform-specific implementations use `expect`/`actual` keywords

### Ktor (v3.3.3)
- **Client**: Multiplatform HTTP client with OkHttp (Android) and Darwin (iOS) engines
- **Server**: Netty-based backend with ContentNegotiation and StatusPages plugins

### Valkey/Glide (v2.2.5)
- Redis-compatible cache for session/nonce storage
- Server-side only (JVM target)
- **Important**: The dependency uses platform-specific classifiers. Current setup uses `osx-aarch_64`. Change classifier in `server/build.gradle.kts` when building for other platforms.

### CocoaPods Integration
- iOS native dependencies managed via `shared/build.gradle.kts` cocoapods block
- Currently integrates GoogleSignIn pod
- Podfile location: `iosApp/Podfile`

## Platform-Specific Considerations

### Android
- Target SDK 36, Min SDK 24
- Modern Credentials API for authentication (not legacy GoogleSignIn)
- JVM 11 compilation target

### iOS
- Deployment target: iOS 26.0
- Compose compiles to Swift-compatible framework (`SharedFramework`)
- SwiftUI wrapper bridges Kotlin Compose UI
- Native authentication via CocoaPods GoogleSignIn

### Server (JVM)
- Main class: `com.alwinsden.dino.KtorStartKt` in `server/src/main/kotlin/com/alwinsden/dino/KtorStart.kt`
- Lazy Valkey initialization on startup
- Endpoints:
  - `GET /health`: Health check
  - `GET /generate-nonce`: UUID generation with Valkey caching
  - `POST /login`: Google token verification

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
3. Example: See `ContinueWithGoogle.kt` authentication implementations

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