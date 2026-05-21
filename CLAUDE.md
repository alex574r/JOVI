# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Build release APK
./gradlew assembleRelease
```

## Testing

```bash
# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew test --tests "com.example.jovi.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Run a single instrumented test class
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.jovi.ExampleInstrumentedTest
```

## Architecture

Single-module Android app using Jetpack Compose with a single-activity architecture.

- **Entry point**: `app/src/main/java/com/example/jovi/MainActivity.kt` — the sole Activity; sets up edge-to-edge display and hosts the Compose UI tree inside `JoviTheme`.
- **Theme**: `app/src/main/java/com/example/jovi/ui/theme/` — Material3 theme (`JoviTheme`) with dynamic color support on Android 12+ (API 31+), falling back to static purple/pink palette on older devices.
- **Dependencies**: managed via version catalog at `gradle/libs.versions.toml`. Add new libraries there, not inline in `build.gradle.kts`.

## Key Config

- `minSdk = 29`, `targetSdk = 36`, `compileSdk = 36`
- Kotlin 2.0.21, AGP 8.13.2
- Java 11 source/target compatibility
- Package: `com.example.jovi`
