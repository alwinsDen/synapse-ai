# synapse - shared UI experiment

## Development Setup

- Intellij with KMP Plugin and Android SDKs + emulators
- If facing **SDK iPhones not found**:
  - RUN`sudo xcode-select -switch /Applications/Xcode.app/Contents/Developer`

## For AI Coding Agents

This project uses [`AGENTS.md`](./AGENTS.md) as the single source of truth for agent instructions — compatible with Claude Code, Gemini CLI, Codex, Cursor, and any tool that supports the [AGENTS.md open standard](https://agents.md).

> `CLAUDE.md` is auto-generated from `AGENTS.md` during Gradle sync and is git-ignored. **Edit `AGENTS.md` only.**

---

A Kotlin Multiplatform application for conversing with AI models, featuring cross-platform mobile support (Android &
iOS) with a Go backend.

**Author**: [alwinsDen.com](https://alwinsden.com)

## Overview

Project Synapse is a modern multiplatform chat application that enables users to interact with various AI models. Built
with Kotlin Multiplatform, it shares business logic across Android and iOS while maintaining native platform
experiences.

## Features

- **Multi-Model Support**: Interact with different AI models (Gemini, and more)
- **Cross-Platform**: Single codebase for Android and iOS using Compose Multiplatform
- **Secure Authentication**: Google Sign-In with nonce-based security
- **Rich Chat Interface**: Clean, Material 3 design with smooth interactions
- **Fast Backend**: Go server (net/http) with Valkey caching
- **Real-time Sync**: Efficient HTTP-based communication

## Technology Stack

### Frontend

- **Kotlin Multiplatform**: Shared code across platforms
- **Compose Multiplatform (v1.9.3)**: Declarative UI framework
- **Material 3**: Modern design components
- **Ktor Client (v3.3.3)**: HTTP communication

### Backend

- **Go (1.23+)**: Server using stdlib `net/http`, no framework
- **go-redis/v9**: Valkey/Redis client for session caching
- **google.golang.org/api/idtoken**: Google ID token verification
- **Google Sign-In API**: OAuth 2.0 authentication

### Build Tools

- **Gradle**: Multi-module build system
- **BuildKonfig**: Build-time configuration management
- **CocoaPods**: iOS dependency management

## Prerequisites

- **JDK 11** or higher
- **Android Studio** (for Android development)
- **Xcode 14+** (for iOS development, macOS only)
- **Go 1.23+** (for backend development)
- **Valkey/Redis** server (for backend caching)

## Getting Started

### 1. Clone the Repository

```bash
git clone git@github.com:alwinsDen/Project-Synapse.git
cd Project-Synapse
```

### 2. External dependencies

* Live PSQL server (CLI / Postgres.app) [else could give Connection Refused error.]
* Valkey CLI setup and running local server.

### 3. Configure Secrets

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

### 4. Start the Backend Server

```bash
cp server/.env.example server/.env
# Edit server/.env with your credentials
make -C server run
```

Or with Docker:
```bash
cd server && docker-compose up
```

Server starts on `http://localhost:3001` by default.

### 5. Build Mobile Apps

**Android:**

```bash
./gradlew :composeApp:assembleDebug
```

## Contact

**Alwin's Den**

- Website: [alwinsden.com](https://alwinsden.com)
- GitHub: [@alwinsDen](https://github.com/alwinsDen)

---

Built with using Kotlin Multiplatform
