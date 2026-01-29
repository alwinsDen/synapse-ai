# Project Synapse

A Kotlin Multiplatform application for conversing with AI models, featuring cross-platform mobile support (Android & iOS) with a Ktor backend.

**Author**: [Alwin's Den](https://alwinsden.com)

## Overview

Project Synapse is a modern multiplatform chat application that enables users to interact with various AI models. Built with Kotlin Multiplatform, it shares business logic across Android and iOS while maintaining native platform experiences.

## Features

- 🤖 **Multi-Model Support**: Interact with different AI models (Gemini, and more)
- 📱 **Cross-Platform**: Single codebase for Android and iOS using Compose Multiplatform
- 🔐 **Secure Authentication**: Google Sign-In with nonce-based security
- 💬 **Rich Chat Interface**: Clean, Material 3 design with smooth interactions
- ⚡ **Fast Backend**: Ktor-powered server with Valkey caching
- 🔄 **Real-time Sync**: Efficient HTTP-based communication

## Technology Stack

### Frontend
- **Kotlin Multiplatform**: Shared code across platforms
- **Compose Multiplatform (v1.9.3)**: Declarative UI framework
- **Material 3**: Modern design components
- **Ktor Client (v3.3.3)**: HTTP communication

### Backend
- **Ktor Server (v3.3.3)**: Lightweight, asynchronous server
- **Valkey/Glide (v2.2.5)**: Redis-compatible caching for sessions
- **Google Sign-In API**: OAuth 2.0 authentication

### Build Tools
- **Gradle**: Multi-module build system
- **BuildKonfig**: Build-time configuration management
- **CocoaPods**: iOS dependency management

## Prerequisites

- **JDK 11** or higher
- **Android Studio** (for Android development)
- **Xcode 14+** (for iOS development, macOS only)
- **CocoaPods** (for iOS dependencies)
- **Valkey/Redis** server (for backend caching)

## Project Structure

```
Project-Synapse/
├── composeApp/          # Cross-platform UI (Android/iOS)
│   ├── androidMain/     # Android-specific code
│   ├── iosMain/         # iOS-specific code
│   └── commonMain/      # Shared UI code
├── shared/              # Business logic & HTTP client
│   ├── androidMain/     # Android platform implementations
│   ├── iosMain/         # iOS platform implementations
│   └── commonMain/      # Shared business logic
├── server/              # Ktor backend
└── iosApp/              # iOS app wrapper
```

## Getting Started

### 1. Clone the Repository

```bash
git clone git@github.com:alwinsDen/Project-Synapse.git
cd Project-Synapse
```

### 2. Configure Secrets

Copy `refer.secret.properties` to `secret.properties` and fill in your credentials:

```bash
cp refer.secret.properties secret.properties
# Edit secret.properties with your API keys
```

Required keys:
- `CLIENT_ID_GOOGLE_AUTH`: Google OAuth client ID
- `KTOR_ENTRY_URL`: Backend server URL
- `IOS_CLIENT_ID`: iOS-specific Google client ID
- `IOS_REVERSE_CLIENT_ID`: iOS URL scheme

### 3. Start the Backend Server

```bash
./gradlew :server:run
```

Server starts on `http://127.0.0.1:5432` by default.

### 4. Build Mobile Apps

**Android:**
```bash
./gradlew :composeApp:assembleDebug
```

**iOS:**
```bash
cd iosApp && pod install && cd ..
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
# Open iosApp/iosApp.xcodeproj in Xcode and run
```

## Build Commands

### Development Builds

| Platform | Command |
|----------|---------|
| Android APK | `./gradlew :composeApp:assembleDebug` |
| iOS Framework | `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode` |
| Server | `./gradlew :server:run` |

### Testing

```bash
# Run all tests
./gradlew test

# Module-specific tests
./gradlew :composeApp:test
./gradlew :shared:test
./gradlew :server:test
```

### Clean Build

```bash
./gradlew clean build
```

## Architecture

### Module Interaction Flow

```
┌─────────────────┐
│   composeApp    │  UI Layer (Compose Multiplatform)
│  (Android/iOS)  │
└────────┬────────┘
         │ calls shared APIs
         ▼
┌─────────────────┐
│     shared      │  Business Logic & HTTP Client
└────────┬────────┘
         │ HTTP requests
         ▼
┌─────────────────┐     ┌─────────────┐
│   server        │◄───►│   Valkey    │
│  (Ktor Backend) │     │   (Cache)   │
└─────────────────┘     └─────────────┘
```

### Key Components

**Navigation System**
- Type-safe navigation using `androidx.navigation.compose`
- Three main routes: `LoginWindow`, `BotWindow`, `BotChatWindow`
- Centralized navigation through `NavigationController` singleton

**Authentication Flow**
1. Client requests nonce from server
2. Server generates UUID and stores in Valkey (60s TTL)
3. User signs in with Google (platform-specific implementation)
4. Server validates Google ID token and verifies nonce
5. JWT token generation (coming soon)

**HTTP Request Management**
- `RequestManager` wraps Ktor HttpClient
- Platform-specific engines: OkHttp (Android), Darwin (iOS)
- Extension functions for type-safe API calls

## Development Workflow

### Adding New Screens

1. Define route in `navigation/routeSerializers.kt`
2. Create composable in `composeApp/src/commonMain/kotlin/`
3. Register in `NavigationController`

### Adding Server Endpoints

1. Define route in `server/src/main/kotlin/com/alwinsden/dino/KtorStart.kt`
2. Add extension function in `shared/.../requestManager/`
3. Call from client using `RequestManager`

### Platform-Specific Code

Use `expect`/`actual` pattern:
```kotlin
// commonMain
expect fun platformSpecificFunction(): String

// androidMain
actual fun platformSpecificFunction() = "Android"

// iosMain
actual fun platformSpecificFunction() = "iOS"
```

## Common Issues

### iOS Build Failures
- Run `pod install` in `iosApp/` directory
- Verify `secret.properties` contains all iOS keys
- Check Xcode signing configuration

### Server Connection Issues
- **Android Emulator**: Use `10.0.2.2` instead of `localhost`
- **iOS Simulator**: Use `localhost` or `127.0.0.1`
- Ensure server is running before testing

### Valkey Platform Compatibility
Current config uses macOS ARM64. For other platforms, update `server/build.gradle.kts`:
- Linux x64: `linux-x86_64`
- Windows x64: `windows-x86_64`

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Commit Convention

Follow conventional commits:
- `feat:` New features
- `fix:` Bug fixes
- `docs:` Documentation changes
- `refactor:` Code refactoring
- `test:` Test additions/changes

## License

This project is currently private. Please contact the author for licensing information.

## Contact

**Alwin's Den**
- Website: [alwinsden.com](https://alwinsden.com)
- GitHub: [@alwinsDen](https://github.com/alwinsDen)

---

Built with ❤️ using Kotlin Multiplatform
