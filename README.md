# StudentOS

StudentOS is a modern Android student productivity app built with Jetpack Compose and Material 3. It helps students manage courses, assignments, study sessions, and academic progress in one clean dashboard.

## ✨ Features

- Dashboard with weekly progress and study streak
- Course tracking and academic overview
- Assignment management with active/completed views
- Study session flow for focused work
- Profile and settings screens
- Firebase and local persistence support

## 📸 Screenshots

Add your app screenshots in the folder below:

- screenshots/01-dashboard.png
- screenshots/02-courses.png
- screenshots/03-assignments.png
- screenshots/04-profile.png

> Replace the placeholder files in the screenshots/ folder with your final images when ready.

## 🛠️ Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Room Database
- Firebase Auth / Firestore
- ViewModel + StateFlow

## 🚀 Getting Started

### Prerequisites

- Android Studio
- JDK 11
- Android SDK with API 35

### Run the app

1. Open the project in Android Studio.
2. Sync Gradle.
3. Select an emulator or connected device.
4. Run the app.

### Build

```bash
./gradlew assembleDebug
```

## 📁 Project Structure

```text
app/
  src/main/java/com/studentos/app/
    ui/           # Screens and UI components
    navigation/   # Navigation routes
    viewmodel/    # ViewModels
    data/         # Repositories, Room, Firebase integration
```

## 🤝 Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a pull request.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
