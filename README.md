# SnapQuest

SnapQuest is an Android photo scavenger hunt app where users complete quests by taking photos of specific objects or scenes. The app validates photos, stores results locally, and tracks progress across 20 quests.

---

## Project Structure

```
app/src/main/java/fr/epita/snapquest/
├── SnapQuestApp.java                  # Application class — registers BroadcastReceiver, seeds DB
├── model/
│   ├── Photo.java                     # Parcelable photo model
│   ├── Quest.java                     # Quest data model
│   └── QuestStatus.java               # Enum: ALL / COMPLETED / PENDING
├── data/
│   ├── asset/QuestLoader.java         # Loads quests.json into DB on first launch
│   ├── db/
│   │   ├── AppDatabase.java           # Room database singleton
│   │   ├── QuestEntity.java           # Room entity for quests table
│   │   ├── PhotoEntity.java           # Room entity for photos table
│   │   ├── QuestDao.java              # DAO for quest queries
│   │   └── PhotoDao.java              # DAO for photo queries
│   ├── photo/PhotoStorage.java        # Manages photo file storage (getExternalFilesDir)
│   └── repo/
│       ├── QuestRepository.java       # Single source of truth for quest/photo data
│       └── NetworkStateRepository.java# LiveData wrapper for network state
├── network/
│   └── ConnectivityReceiver.java      # BroadcastReceiver for connectivity changes
├── validation/
│   ├── ValidationRule.java            # Strategy pattern interface
│   ├── ValidationResult.java          # Holds pass/fail status and message
│   ├── SizeRule.java                  # Validates minimum photo resolution (800×600)
│   ├── ExifFreshnessRule.java         # Validates photo taken within last 30 seconds
│   └── PhotoValidator.java            # Runs all rules in sequence
├── util/
│   └── PermissionUtils.java           # Camera and location permission helpers
└── ui/
    ├── hub/HubActivity.java           # Launcher screen — navigate to Camera/Collection/Settings
    ├── camera/
    │   ├── CameraActivity.java        # CameraX preview and photo capture
    │   └── CameraViewModel.java       # Survives rotation — stores photo URI and quest ID
    ├── collection/
    │   ├── CollectionActivity.java    # RecyclerView list of all quests with filter menu
    │   ├── QuestListAdapter.java      # RecyclerView adapter using Glide for thumbnails
    │   └── QuestViewHolder.java       # ViewHolder for quest items
    ├── review/
    │   ├── PhotoReviewActivity.java   # Shows captured photo + validation result
    │   └── PhotoReviewViewModel.java  # Survives rotation — stores photo path and result
    └── setting/
        └── SettingsActivity.java      # Dark mode toggle
```

---

## Features

- **Camera** — CameraX live preview with back-camera capture, saves to app-private storage
- **Photo Validation** — Strategy pattern: checks minimum resolution (800×600) and EXIF freshness (≤30s)
- **Quest Collection** — RecyclerView with filter menu (All / Completed / Pending), landscape support
- **Room Database** — Persists quests and photos; foreign key from photos → quests with CASCADE delete
- **Network Awareness** — BroadcastReceiver detects connectivity changes, exposes LiveData
- **Dark Mode** — Material 3 DayNight theme with full light/dark color palettes
- **20 Quests** — Loaded from `assets/quests.json` on first launch

---

## Team

| Person | Area | Branch |
|--------|------|--------|
| Person 1 | Camera + Validation | `feature/camera-validation` |
| Person 2 | Data + Room DB | `feature/data-room` |
| Person 3 | Collection + RecyclerView | `feature/collection-recycler` |
| Person 4 | Network + BroadcastReceiver | `feature/network-receiver` |
| Person 5 | UI + Fragments + Themes | `feature/ui-fragment-themes` |

---

## Setup

1. Clone the repo
2. Open in Android Studio
3. Sync Gradle
4. Run on a device or emulator with API 24+

**Required permissions**: `CAMERA`, `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `INTERNET`, `ACCESS_NETWORK_STATE`

---

## Tech Stack

- **Language**: Java
- **Min SDK**: 24 | **Target SDK**: 36
- **Database**: Room 2.6.1
- **Camera**: CameraX 1.3.4
- **Image loading**: Glide 4.16.0
- **JSON parsing**: Gson 2.11.0
- **Theme**: Material 3 DayNight
