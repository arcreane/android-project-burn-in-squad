# SnapQuest

SnapQuest is an Android photo scavenger hunt app where users complete quests by taking photos of specific objects or scenes. The app uses Gemini AI to validate photos, stores results locally, and tracks progress across 20 quests.

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
│   ├── AiContentRule.java             # Validates photo content against quest via Gemini AI
│   ├── GeminiValidationService.java   # HTTP client for Gemini 2.5 Flash REST API
│   └── PhotoValidator.java            # Runs all rules in sequence (SizeRule → AiContentRule)
├── util/
│   └── PermissionUtils.java           # Camera and location permission helpers
└── ui/
    ├── hub/HubActivity.java           # Launcher screen — navigate to Camera/Collection/Settings
    ├── camera/
    │   ├── CameraActivity.java        # CameraX preview and photo capture
    │   └── CameraViewModel.java       # Survives rotation — stores photo URI and quest ID
    ├── collection/
    │   ├── CollectionActivity.java    # RecyclerView list of all quests with filter/sort menu
    │   ├── QuestListAdapter.java      # RecyclerView adapter using Glide for thumbnails
    │   └── QuestViewHolder.java       # ViewHolder for quest items
    ├── review/
    │   ├── PhotoReviewFragment.java   # Reusable fragment: MODE_REVIEW (AI validation) / MODE_VIEW
    │   └── PhotoReviewViewModel.java  # Survives rotation — stores photo path and result
    └── setting/
        └── SettingsActivity.java      # Dark mode toggle
```

---

## Features

- **Camera** — CameraX live preview with back-camera capture, saves to app-private storage; quest title and hint shown as overlay while shooting
- **AI Photo Validation** — After capture, photo is validated asynchronously by Gemini 2.5 Flash: a spinner shows while the AI checks whether the photo matches the quest; Accept is enabled only on a YES response
- **Size Check** — Pre-AI guard: rejects photos below 800×600 before sending to Gemini
- **Quest Collection** — RecyclerView with filter (All / Completed / Pending) and sort (by points, category, status); tap a pending quest to open the camera, tap a completed quest to view the saved photo
- **Room Database** — Persists quests and photos; foreign key from photos → quests with CASCADE delete
- **Network Awareness** — BroadcastReceiver detects connectivity changes, shows a "No Connection" badge via LiveData
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
3. Add your Gemini API key to `local.properties` (create the file if it does not exist — it is git-ignored):
   ```
   GEMINI_API_KEY=your_key_here
   ```
   Get a free key at [aistudio.google.com](https://aistudio.google.com).
4. Sync Gradle (`File → Sync Project with Gradle Files`)
5. Run on a device or emulator with API 24+

> The API key is read at build time and injected into `BuildConfig.GEMINI_API_KEY`. It is never committed to the repository.

**Required permissions**: `CAMERA`, `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `INTERNET`, `ACCESS_NETWORK_STATE`

---

## Tech Stack

- **Language**: Java
- **Min SDK**: 24 | **Target SDK**: 36
- **Database**: Room 2.6.1
- **Camera**: CameraX 1.3.4
- **Image loading**: Glide 4.16.0
- **JSON parsing**: Gson 2.11.0
- **AI validation**: Gemini 2.5 Flash (REST API via HttpURLConnection)
- **Theme**: Material 3 DayNight
