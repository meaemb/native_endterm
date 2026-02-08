# Campus Companion

Campus Companion is a native Android mobile application built with Kotlin and Jetpack Compose.
The app demonstrates modern Android architecture, offline-first data handling,
external API integration, Firebase authentication, and realtime features.

The project was developed as an Endterm and Final Capstone project
for the **Native Mobile Development** course at **Astana IT University**.

---

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
- Manual retry via Refresh / Load More buttons

---

## External API
- **TVMaze API**
    - Used to fetch TV shows as events
    - Public JSON-based API
    - Supports search and pagination
    - Documentation: https://www.tvmaze.com/api

---

## Architecture
The application follows the **MVVM (Model–View–ViewModel)** architecture pattern.

- UI layer: Jetpack Compose screens
- ViewModel layer: business logic and state management
- Repository layer: data coordination
- Data layer:
    - Remote: TVMaze API (Retrofit)
    - Local: Room database
    - Realtime: Firebase Realtime Database

The UI layer does not directly access API, database, or Firebase.

---

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

---

## Offline-First Strategy
- Remote data is cached locally using Room
- Cached content is available in offline mode
- Remote data is refreshed when internet is available
- Duplicate prevention using ID-based replace strategy
- Local user-created events are preserved during sync

---

## Performance
Performance was improved by:
- Paginated loading with page size = 20
- Image downsampling using Coil `AsyncImage`
- Non-blocking background operations using coroutines

Profiling was performed using **Android Studio Profiler**.
CPU and memory usage remain stable during feed scrolling and pagination.

---

## Security
### Secrets
- `google-services.json` is **not included** in the repository
- Firebase configuration must be added locally

### Firebase Rules (documented behavior)
Firebase Realtime Database uses **user-scoped paths**.
Users can only read and write their own favorites and comments,
identified by their authenticated user ID.

### Input Validation
- Empty event titles are rejected
- Empty comments are ignored
- Invalid input does not crash the application

---

## Setup
1. Clone the repository
2. Open the project in Android Studio
3. Create a Firebase project
4. Enable **Firebase Authentication (Email/Password)**
5. Enable **Firebase Realtime Database**
6. Add `google-services.json` to the `app/` directory
7. Sync Gradle files
8. Run the app on an emulator or real device

---

## How to Run
- Android Studio: click **Run**
- Minimum SDK: as specified in Gradle
- Internet connection required for first data load

---

## Testing
- **10 unit tests** for business logic (ViewModel and Mapper)
- Manual release checklist (15+ cases)
- QA log with documented issues and fixes
- No crashes on common scenarios:
    - No internet
    - Empty data
    - Wrong credentials

---

## Release & Deployment
- Separate Debug and Release configurations
- Debug logs disabled in Release build
- Signed Android App Bundle (.aab) generated
- Draft store listing included in `/docs/STORE_LISTING.md`

---

## AI Usage
ChatGPT was used for:
- Conceptual explanations
- Debugging assistance
- Test setup guidance
- Documentation structuring

All generated content was reviewed, adapted, and fully understood by the author.
