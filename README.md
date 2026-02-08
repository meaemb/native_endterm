# Campus Companion

Campus Companion is a native Android mobile application built with Kotlin and Jetpack Compose.
The app demonstrates modern Android architecture, offline-first data handling,
external API integration, Firebase authentication, and realtime features.

The project was developed as an endterm project for the Native Mobile Development course
at Astana IT University.

## Features
- User authentication (Sign In / Sign Up / Sign Out) with Firebase
- Persistent user session after app restart
- Event feed with pagination
- Event details screen
- Debounced search (no request on every keystroke)
- Offline-first cache using Room
- Create and edit local events
- Favorites (user-specific)
- Realtime comments using Firebase Realtime Database
- Graceful handling of empty states and network errors

## External API
- **TVMaze API**
    - Used to fetch TV shows as events
    - JSON-based public API
    - Supports search and pagination
    - Documentation: https://www.tvmaze.com/api

## Architecture
- MVVM (Model–View–ViewModel)
- Repository pattern
- UI does not directly call API or Firebase
- Clear separation of UI, domain, and data layers

## Tech Stack
- Kotlin
- Jetpack Compose
- MVVM + Repository
- Retrofit + OkHttp
- Room (local persistence)
- Firebase Authentication
- Firebase Realtime Database
- Coil (image loading)
- Coroutines + Flow / StateFlow
- Hilt (dependency injection)

## Offline-First Strategy
- Remote data is cached locally using Room
- Cached content is available in offline mode
- Remote data is refreshed when internet is available
- Duplicate prevention using ID-based replace strategy
- Local user-created events are preserved during sync

## Setup
1. Clone the repository
2. Open the project in Android Studio
3. Create a Firebase project
4. Enable Firebase Authentication (Email/Password)
5. Enable Firebase Realtime Database
6. Add `google-services.json` to the `app/` directory
7. Sync Gradle files
8. Run the app on an emulator or real device

## How to Run
- Android Studio: click **Run**
- Minimum SDK: as specified in Gradle
- Internet connection required for first data load

## Testing
- Unit tests for business logic (ViewModel and Mapper)
- Minimum 5 unit tests implemented
- Manual test checklist (10+ cases) included in the report
- No crashes on common scenarios:
    - No internet
    - Empty data
    - Wrong credentials

## AI Usage
ChatGPT was used for:
- Conceptual explanations
- Debugging assistance
- Test setup guidance

All generated content was reviewed, modified, and fully understood by the author.
