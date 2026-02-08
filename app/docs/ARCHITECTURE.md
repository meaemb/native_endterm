# Architecture — Campus Companion

The application follows the MVVM architecture pattern with Repository abstraction.

## Layers

UI (Jetpack Compose)
↓
ViewModel (StateFlow, business logic)
↓
Repository (data coordination)
↓
Data sources:
- Remote: TVMaze API (Retrofit)
- Local: Room database
- Realtime: Firebase Realtime Database

## Data Flow

User actions in UI trigger ViewModel events.
ViewModel requests data from Repository.
Repository decides whether to load data from:
- local cache (Room)
- remote API (TVMaze)
- realtime database (Firebase)

All data streams are exposed via Flow / StateFlow.

## Offline-first Strategy

- Remote data is cached locally
- Cached data is shown when offline
- Remote refresh happens when internet is available
- Local user-created events are preserved during sync
