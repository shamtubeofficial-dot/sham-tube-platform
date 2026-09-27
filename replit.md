# Sham Tube

Native Arabic-first Android video-sharing application inspired by YouTube, with local Room data and a backend-ready repository boundary.

## Run & Operate

- `cd android && gradle assembleDebug` — build the Android debug APK
- Open `android/` in Android Studio with JDK 17 and Android SDK 35 — run the native app
- Existing workspace services remain available through their managed workflows

## Stack

- Kotlin, Android SDK 35, Gradle 8.9
- Jetpack Compose, Material 3, Navigation Compose
- Media3 / ExoPlayer for playback
- Coil for remote and local image loading
- Room + Kotlin Flow for local persistence
- MVVM with repository and data-source boundaries

## Where things live

- `android/app/src/main/java/com/shamtube/app/data/` — domain models, Room database, sample data, repositories, and local authentication
- `android/app/src/main/java/com/shamtube/app/ui/` — Compose theme, reusable components, screens, and navigation
- `android/app/src/main/java/com/shamtube/app/viewmodel/` — MVVM state and user actions
- `android/app/src/main/res/values/` — Arabic strings and theme resources
- `android/app/src/main/res/values-en/` — English localization

## Architecture decisions

- The app starts with a local Room-backed repository so the home feed and interactions work without private keys or a network backend.
- `ShamTubeRepository` and `AuthRepository` are interfaces; Firebase or Supabase implementations can replace the local implementations without changing Compose screens.
- Arabic is the default resource language and the entire Compose tree is forced to RTL.
- Sample videos use public Google test media URLs so Media3 playback works immediately.

## Product

Sham Tube provides a modern Arabic video feed, search, playback, comments, local likes and saves, subscriptions, channels, notifications, authentication screens, settings, and local video upload selection.

## User preferences

- Arabic-first experience with English resources prepared for later language switching.

## Gotchas

- The native project is under `android/` and is built with Android Studio or a machine with the Android SDK and JDK 17.
- Selected upload URIs are intentionally local in the first version; cloud storage is a repository replacement, not a UI rewrite.

## Pointers

- See the `pnpm-workspace` skill for workspace structure, TypeScript setup, and package details
