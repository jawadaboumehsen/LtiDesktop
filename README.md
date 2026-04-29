# LtiDesktop

![LtiDesktop Banner](composeApp/src/commonMain/composeResources/drawable/ltipatcher_wordmark.xml)

LtiDesktop is a premium, cross-platform desktop application built with **Compose Multiplatform**. It serves as a modern system dashboard, console interface, and administrative utility, featuring the signature high-contrast **"Antigravity"** dark aesthetic.

## Features

- **Modern System Dashboard**: A responsive bento-box grid layout displaying network health, system uptime, and real-time mock analytics.
- **Antigravity UI/UX**: A curated dark theme (`#000000` root background with `#0C0C0C` surface containers) accented by neon cyan (`#00E5FF`) and glowing micro-interactions.
- **Interactive Output Terminal**: A fully functional console output panel with syntax highlighting, custom terminal scrollbars, and auto-scroll capabilities.
- **Animated Splash Screen**: A professional 2.5-second launch sequence featuring scalable vector graphics (LtiPatcher logo and wordmark).
- **Settings Management**: A comprehensive preferences suite with custom-built scrollable panels and hover-aware UI elements.

## Tech Stack

- **Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- **Language**: Kotlin 2.0+
- **Architecture**: MVI (Model-View-Intent) pattern
- **Dependency Injection**: Koin
- **Asynchrony**: Kotlin Coroutines & StateFlow

## Getting Started

### Prerequisites
- JDK 17 or newer
- Kotlin 2.0+ 
- Gradle

### Building and Running

To run the application locally on your desktop:

```bash
# Clean the project to ensure fresh Compose Multiplatform compilation
./gradlew clean

# Run the desktop application
./gradlew :composeApp:run
```

To build a standalone executable distribution for your current operating system:

```bash
./gradlew :composeApp:createDistributable
```

## UI Components Highlights

- **DesktopButton**: Custom interactive buttons with scaling click animations and neon hover effects.
- **MainNavigation**: A collapsible sidebar with dynamic SVG icon rendering and hover tooltips.
- **SettingsScrollablePanel**: A highly polished, custom vertical scrollbar implementation ensuring smooth desktop UX without default OS scrollbar artifacts.

## License

© 2026 Lti Solutions. All rights reserved.