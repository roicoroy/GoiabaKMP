# GoiabaKMP - Kotlin Multiplatform Project

A modern Kotlin Multiplatform project showcasing cross-platform development for Android and iOS using Compose Multiplatform.

## Features

- 🎯 **Kotlin Multiplatform**: Share code between Android and iOS
- 🎨 **Compose Multiplatform**: Modern UI toolkit for both platforms
- 📱 **Native Integration**: Seamless integration with platform-specific features
- 🏗️ **Clean Architecture**: Well-structured codebase with clear separation of concerns
- 🔄 **State Management**: Using Kotlin Flow and ViewModel
- 🌐 **Networking**: Ktor for API communication
- 💉 **Dependency Injection**: Koin for clean and testable code
- 🗄️ **Backend Integration**: Strapi CMS for content management

## Project Structure

- `/composeApp` - Shared Compose Multiplatform code
- `/iosApp` - iOS application
- `/shared` - Common code and resources
- `/data` - Data layer implementation
- `/di` - Dependency injection setup
- `/navigation` - Navigation components
- `/feature` - Feature modules
  - `/home` - Home screen implementation
  - `/details` - Details screen implementation
- `/strapi-cms` - Backend CMS implementation

## Tech Stack

- **Kotlin Multiplatform** - Cross-platform development
- **Compose Multiplatform** - UI framework
- **Koin** - Dependency injection
- **Ktor** - Networking
- **Strapi** - Headless CMS
- **Kotlin Coroutines & Flow** - Asynchronous programming
- **Material 3** - Design system

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Xcode 15+ (for iOS development)
- JDK 17+
- Kotlin 2.1.21+

### Setup

1. Clone the repository
```bash
git clone https://github.com/yourusername/GoiabaKMP.git
```

2. Open the project in Android Studio

3. Run the backend (optional)
```bash
cd strapi-cms
npm install
npm run develop
```

4. Run the application
- For Android: Run the project from Android Studio
- For iOS: Open `iosApp/iosApp.xcworkspace` in Xcode and run

## Architecture

The project follows Clean Architecture principles with the following layers:

- **Presentation**: Compose UI components and ViewModels
- **Domain**: Business logic and models
- **Data**: Repository implementations and data sources
- **Common**: Shared utilities and resources

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details