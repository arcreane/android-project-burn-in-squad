# SnapQuest

SnapQuest is an Android-based photo scavenger hunt application where users complete "quests" by taking photos of specific objects or scenes.

## Today's Progress

### 1. Project Initialization & Configuration
- **Build System**: Updated `build.gradle.kts` (project and app levels) to configure the Android environment (SDK 36) and namespace (`fr.epita.snapquest`).
- **Dependency Management**: Integrated essential libraries for the app's core functionality:
    - **CameraX**: For photo capture capabilities.
    - **Room**: For local data persistence.
    - **Glide**: For efficient image loading and caching.
    - **Gson**: For parsing quest data from JSON.
    - **Material Design & ConstraintLayout**: For building a modern, responsive UI.
- **Project Hygiene**: Updated `.gitignore` and `local.properties` to manage environment-specific settings.

### 2. Data Infrastructure
- **Quest Dataset**: Created `app/src/main/assets/quest.json` containing 20 diverse scavenger hunt items. Each quest includes:
    - Unique ID and Title
    - Category (e.g., Architecture, Nature, School Supplies)
    - Hints to help the user find the object.
    - Point values for gamification.

### 3. UI and Resource Development
- **Material 3 Theming**: Implemented a comprehensive Material 3 theme (`Theme.SnapQuest`) with support for both **Light** and **Dark (Night)** modes.
- **Custom Color Palette**: Defined a core color system in `colors.xml` including primary, secondary, surface, and error colors consistent with Material Design 3.
- **App Visuals**: Set up launcher icons (`ic_launcher_background.xml` and `ic_launcher_foreground.xml`).
- **Navigation & Menus**: Created `collection_menu.xml` to support filtering quests by status (All, Completed, Pending).
- **Comprehensive String Resources**: Expanded `strings.xml` to include UI labels for core features:
    - **Navigation**: Start Hunting, View Collection, Settings.
    - **Actions**: Capture Photo, Accept, Retake, Share.
    - **Status**: Completed, Pending, No Connection.
- **Layouts**: Initialized `MainActivity` with basic Edge-to-Edge support.

### 4. Manifest & Rules
- **Permissions & Rules**: Configured `AndroidManifest.xml` and defined `data_extraction_rules.xml` to manage backups and app behavior.

---
*Documentation updated based on recent activity.*
