# Gemini Project Context: Project Synapse (Dino)

This document provides a comprehensive overview of "Project Synapse," a Kotlin Multiplatform application designed for interacting with AI models.

## Project Overview

Project Synapse is a multiplatform chat application enabling users to converse with various AI models. It features a shared user interface and business logic for Android and iOS, powered by a Ktor backend. Key features include multi-model support, secure Google Sign-In, and a rich chat interface built with Material 3.

## Technology Stack

The project leverages a modern, Kotlin-centric technology stack:

- **Core Framework:** Kotlin (`v2.3.0`)
- **Build System:** Gradle
- **Frontend (UI):**
    - Compose Multiplatform (`v1.9.3`) for cross-platform UI.
    - Material 3 for design components.
    - `androidx.navigation.compose` (`v2.9.1`) for type-safe navigation.
- **Backend:**
    - Ktor (`v3.3.3`) for the server and HTTP client.
    - Netty embedded server.
    - Valkey/Glide (`v2.2.5`) for Redis-compatible session caching.
- **Authentication:** Google Sign-In (OAuth 2.0)
- **Configuration:** BuildKonfig (`v0.17.1`) for build-time secrets management.
- **Android:**
    - Min SDK: 24, Target SDK: 36
- **iOS:**
    - CocoaPods for dependency management.

## Project Structure

The codebase is organized into three primary Gradle modules:

- `:composeApp`: Contains all the user interface code written with Compose Multiplatform. It includes `androidMain`, `iosMain`, and `commonMain` source sets for platform-specific and shared UI logic.
- `:shared`: The core module for shared business logic, including networking (Ktor client), data models, and platform-specific implementations using the `expect`/`actual` pattern.
- `:server`: A Ktor-based backend server that handles API requests, authentication, and session management.

An `iosApp` directory also exists, containing the Xcode project wrapper for the iOS application.

## Building and Running

The project is built and managed using Gradle. The following are key commands for development:

- **Run Backend Server:**
  ```bash
  ./gradlew :server:run
  ```

- **Build Android App (Debug):**
  ```bash
  ./gradlew :composeApp:assembleDebug
  ```

- **Prepare iOS Framework:**
  1. Install Pods: `cd iosApp && pod install && cd ..`
  2. Generate framework: `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode`
  3. Open `iosApp/iosApp.xcodeproj` in Xcode and run.

- **Run All Tests:**
  ```bash
  ./gradlew test
  ```

- **Clean and Build:**
  ```bash
  ./gradlew clean build
  ```

## Development Conventions

- **Configuration:** API keys and sensitive data are managed via a `secret.properties` file, which is not checked into source control. Configuration is exposed to the app via the BuildKonfig plugin.
- **Platform-Specific Code:** The `expect`/`actual` pattern is used within the `:shared` module to abstract platform-specific functionality.
- **Commit Messages:** The project adheres to the Conventional Commits specification.
