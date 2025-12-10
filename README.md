# SceneIt - TV Series Tracker 📺

Modern Android TV series tracking application with TMDB API integration.

## Features ✨

- 🔍 Search TV series using TMDB API
- 📚 Personal series library management
- ⭐ Favorite series marking (double-tap or long-press)
- 📊 Episode progress tracking
- 📝 Personal notes for each series
- 🗑️ Swipe to delete series
- 🌐 Bilingual support (English/Turkish)
- 🎨 Material Design UI

## Screenshots

*Coming soon - Run on emulator to see the app in action!*

## Tech Stack 🛠️

- **Language:** Kotlin
- **Min SDK:** 34 (Android 14)
- **Architecture:** MVVM
- **Database:** Room (SQLite)
- **Networking:** Retrofit + Gson
- **Image Loading:** Glide
- **UI:** Material Design Components, Data Binding, View Binding
- **Async:** Kotlin Coroutines + LiveData

## Project Structure 📁

```
app/src/main/java/com/duyguabbasoglu/sceneit/
├── model/              # Data models (Series, Episode)
├── database/           # Room database, DAOs, ViewModels
├── network/            # Retrofit API client and services
├── ui/                 # Activities
├── adapter/            # RecyclerView adapters
├── customview/         # Custom views (RatingIndicatorView, EpisodeProgressView)
├── binding/            # Data binding adapters
└── util/               # Constants and helpers
```

## Requirements Implemented 📋

| Requirement | Points | Status |
|------------|--------|--------|
| Custom App Icon | 5 | ✅ |
| Professional UI | 18 | ✅ |
| Material Design (2+ views) | - | ✅ |
| Gestures (Swipe, Double-tap) | 8 | ✅ |
| Custom Views (2) | 10 | ✅ |
| Data Binding | 10 | ✅ |
| View Binding | 4 | ✅ |
| Localization (EN/TR) | 6 | ✅ |
| RecyclerView (2 layouts) | 15 | ✅ |
| Room Database | 13 | ✅ |
| Retrofit JSON | 15 | ✅ |
| Extra Packages | 3 | ✅ |

**Total: 84 points** (Team member tasks excluded)

## Custom Views 🎨

### RatingIndicatorView
Circular rating indicator displaying TMDB rating (0-10) with color coding:
- 🟢 Green: 7.0+
- 🟡 Yellow: 5.0-6.9
- 🔴 Red: <5.0

### EpisodeProgressView
Circular progress indicator showing watched vs total episodes.

## Gestures 👆

- **Swipe Left/Right:** Delete series (with confirmation)
- **Double Tap:** Toggle favorite status
- **Long Press:** Toggle favorite status

## Setup & Installation 🚀

### Prerequisites
- Android Studio Hedgehog or newer
- Android SDK 34
- Emulator or physical device running Android 14+

### Steps
1. Clone the repository
```bash
git clone <your-repo-url>
cd SceneIt
```

2. Open in Android Studio
```bash
open -a "Android Studio" .
```

3. Sync Gradle
```bash
./gradlew sync
```

4. Build the project
```bash
./gradlew assembleDebug
```

5. Run on emulator or device
- Click Run in Android Studio, OR
- `./gradlew installDebug`

## How to Use 📖

1. **First Launch:** Tap the **+** button to search for series
2. **Search:** Enter series name and tap search
3. **Add Series:** Tap "Add to List" on any search result
4. **View Details:** Tap any series card
5. **Track Episodes:** Use +/- buttons in detail view
6. **Add Notes:** Type notes and tap "Save Notes"
7. **Mark Favorite:** Double-tap or long-press any series
8. **Delete Series:** Swipe left or right on a series

## API Key 🔑

This app uses TMDB (The Movie Database) API. The API key is currently hardcoded in `Constants.kt` for demo purposes.

For production, move the API key to `local.properties`:
```properties
tmdb.api.key=your_api_key_here
```

And load it in `build.gradle.kts`:
```kotlin
buildConfigField("String", "TMDB_API_KEY", "\"${properties["tmdb.api.key"]}\"")
```

## Languages 🌍

- English (default)
- Turkish (Türkçe)

Change device language to switch automatically.

## Database Schema 💾

### Series Table
- id, name, posterPath, backdropPath, overview
- voteAverage, firstAirDate, isFavorite
- watchedEpisodes, totalEpisodes, userRating, notes

### Episodes Table
- id, seriesId, seasonNumber, episodeNumber
- name, airDate, overview, isWatched

## Future Enhancements 🔮

- [ ] Episode list by season
- [ ] Mark individual episodes as watched
- [ ] Series recommendations
- [ ] Dark mode
- [ ] Home screen widget
- [ ] Export/import library

## Contributing 🤝

This is a course project. Contributions are welcome after course completion!

## License 📄

Educational project for CTIS 487 - Mobile Application Development

## Contact 📧

Duygu Abbasoğlu - CTIS 487 Project

---

**Made with ❤️ for CTIS 487**
