# TouchPad Cursor Overlay Pro

A production-ready Android application that provides a floating desktop-style touchpad and cursor overlay system across the entire Android device. Works system-wide on top of all apps, browsers, and system UI.

## Features

- **Floating Touchpad Overlay** - Draggable touchpad panel that stays above all apps
- **System-Wide Cursor** - Circular cursor with smooth, high-precision movement
- **Left & Right Click** - Single click, double click, long press, and context menu
- **Drag & Drop Support** - Mouse down, move, and up operations
- **Adjustable Size** - Real-time width and height controls for the overlay
- **Transparency Control** - Adjust overlay opacity
- **Cursor Speed** - Configurable cursor sensitivity
- **Web Touchpad Mode** - JavaScript injection for browser-based touchpad
- **Material Design 3** - Modern, premium UI with dark/light mode
- **Persistent Settings** - Remembers position, size, and preferences

## Permissions Required

1. **Display Over Other Apps** - SYSTEM_ALERT_WINDOW for overlay rendering
2. **Accessibility Service** - For system-wide gesture dispatching
3. **Notification Permission** - For foreground service (Android 13+)
4. **Ignore Battery Optimization** - Prevents service from being killed

## Installation

### Pre-built APK
Download the latest release APK from the Releases page.

### Build from Source
```bash
git clone https://github.com/haiderfxbot-ai/TouchPadCursorOverlayPro.git
cd TouchPadCursorOverlayPro
./gradlew assembleRelease
```

The APK will be generated at:
`app/build/outputs/apk/release/app-release.apk`

## Usage

1. Launch the app and complete the onboarding wizard
2. Grant all required permissions through the Permission screen
3. Tap "Start Overlay" on the home screen
4. Use the floating touchpad to control your cursor:
   - **Touchpad surface**: Move your finger to move the cursor
   - **L button**: Left click
   - **R button**: Right click
   - **W slider**: Adjust overlay width
   - **H slider**: Adjust overlay height
   - **Spd slider**: Adjust cursor speed
   - **Op slider**: Adjust overlay transparency

## Architecture

- **Pattern**: MVVM (Model-View-ViewModel)
- **DI**: Hilt (Dagger)
- **Async**: Kotlin Coroutines + Flow
- **Storage**: Jetpack DataStore
- **UI**: Jetpack Compose + Material Design 3
- **Overlay**: WindowManager (TYPE_APPLICATION_OVERLAY)
- **Gestures**: AccessibilityService.dispatchGesture()

## Project Structure

```
app/src/main/java/com/touchpad/cursoroverlay/
├── TouchPadApp.kt              # Application class
├── MainActivity.kt             # Main activity with navigation
├── di/
│   └── AppModule.kt            # Hilt DI module
├── data/
│   ├── PreferencesManager.kt   # DataStore preferences
│   ├── SettingsRepository.kt   # Settings repository
│   └── models/
│       └── OverlaySettings.kt  # Settings data class
├── overlay/
│   ├── FloatingOverlayService.kt  # Main overlay service
│   ├── OverlayTouchpadLayout.kt   # Draggable overlay container
│   ├── TouchpadSurface.kt         # Touch-sensitive surface
│   └── CursorView.kt              # Circular cursor view
├── service/
│   └── TouchPadAccessibilityService.kt  # Accessibility service
├── ui/
│   ├── theme/                   # Material 3 theme
│   └── screens/                 # Compose screens
├── util/
│   ├── Constants.kt             # App constants
│   ├── PermissionHelper.kt      # Permission utilities
│   └── WebTouchpadBridge.kt     # JavaScript injection module
└── viewmodel/
    ├── MainViewModel.kt         # Home screen VM
    ├── SettingsViewModel.kt     # Settings screen VM
    └── OverlayServiceController.kt  # Service lifecycle
```

## Troubleshooting

### Overlay not appearing
- Ensure "Display over other apps" permission is granted
- Verify the service is running (check notification)
- Restart the app

### Cursor not clicking
- Ensure Accessibility Service is enabled
- Go to Settings > Accessibility > TouchPad Cursor Overlay
- Toggle the service off and on

### Overlay disappears after some time
- Disable battery optimization for this app
- Check if recent app clean-up is killing the service

## Build Requirements

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Gradle 8.4

## License

Copyright 2024 TouchPad Labs. All rights reserved.
