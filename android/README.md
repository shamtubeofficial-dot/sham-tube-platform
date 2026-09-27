# Sham Tube — شام تيوب

Native Android video-sharing application built with Kotlin, Jetpack Compose, Material 3, Room, Navigation Compose, Coil, and Media3/ExoPlayer.

## Open and build

Open the `android` directory in Android Studio Hedgehog or newer with JDK 17 and Android SDK 35 installed. Android Studio will resolve the Gradle 8.9 distribution declared in `gradle/wrapper/gradle-wrapper.properties`.

From a machine with the Android SDK, JDK 17, and Gradle 8.9 available:

```bash
cd android
gradle assembleDebug
```

Android Studio can use the same Gradle version from the wrapper properties during project sync.

The debug APK is emitted to `app/build/outputs/apk/debug/app-debug.apk`.

## Product surface

- Arabic-first RTL home feed with category filters and sample videos.
- Search for videos and channels with recent searches.
- Media3/ExoPlayer watch screen with playback controls, metadata, reactions, save, share, subscribe, comments, and recommendations.
- Local Room persistence for videos, channels, comments, likes, saved videos, and subscriptions.
- Local upload flow using Android's document picker for videos and thumbnails.
- Channel pages, subscriptions, library, notifications, login/register/forgot-password screens, and settings.
- Runtime light/dark theme switching and English string resources in `values-en`.

## Structure

```text
android/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/shamtube/app/
│       │   ├── MainActivity.kt
│       │   ├── ShamTubeApplication.kt
│       │   ├── data/
│       │   │   ├── local/                 # Room entities, DAOs, database
│       │   │   ├── model/                 # Domain models
│       │   │   ├── LocalAuthRepository.kt # Firebase/Supabase-ready auth boundary
│       │   │   ├── SampleData.kt
│       │   │   └── ShamTubeRepository.kt  # Replaceable repository boundary
│       │   ├── ui/
│       │   │   ├── components/             # Reusable Compose components
│       │   │   ├── navigation/
│       │   │   ├── screens/
│       │   │   └── theme/
│       │   └── viewmodel/
│       └── res/
│           ├── drawable/
│           ├── values/
│           └── values-en/
├── build.gradle.kts
├── gradle.properties
└── settings.gradle.kts
```

## Backend replacement point

The UI depends on `ShamTubeRepository` and `AuthRepository`, not directly on Room or local auth. A Firebase or Supabase implementation can be added later and supplied from `ShamTubeApplication` without changing the Compose screens.