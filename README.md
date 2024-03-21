# FooBook_Android: Part 3 - Database Integration and Enhanced Features
Welcome to Part 3 of the FooBook_Android project. This phase brings critical updates to the app, integrating MongoDB for backend storage and Room for local database management. We've also incorporated JWT for secure authentication, enhanced UI components, and introduced a complete friend system, alongside other robust features.

## Getting Started
To get started with these new features, please follow the instructions below.

### Prerequisites
Ensure that you have the latest version of Android Studio.
Verify that your MongoDB Atlas cluster is active and accessible.
Update the Android SDK to the latest available version.

### Installation
1. **Clone the Repository:**
   **git clone https://github.com/RoeiMesi/FooBook_Android**
   
2. **Open Project: Launch Android Studio and open the cloned project.**

3. **Add Dependencies: Update your build.gradle with MongoDB, Room, Retrofit, and other required libraries.**

4. **Sync Gradle: Use the 'Sync Now' feature in Android Studio to sync your project with the updated dependencies.**

### Configuration
- **MongoDB:**- Configure your MongoDB Atlas with the necessary collections and link it to the app with the correct URI.
- **Room Database:**- Set up Room entities, DAOs, and database instances to mirror the MongoDB structure for local storage.
- **JWT Authentication:**- Ensure your server-side authentication system is ready to issue JWT tokens for secure communication.

## New Features and Improvements

### User Authentication and Security
- Integrated JWT-based authentication for secure login and registration.
- Ensured all backend endpoints require valid JWT for access.

### Friend System
- Implemented a comprehensive friend system allowing users to send, view, and manage friend requests.
- Integrated friend features into user profiles with functionality to view and manage friends and friend lists.

### UI and UX Enhancements
- Refined UI components for improved navigation and aesthetics, including a dark mode setting.
- Implemented photo selection in post creation and profile editing for a more personalized user experience.
- Applied consistent theming and data binding for smoother UI updates.

### Database Management
- Implemented MongoDB for storing posts and user data.
- Integrated Room for local data persistence and offline functionality.
- Established mechanisms for syncing local and remote databases.

### Error Handling and Data Compliance
- Improved error handling for a smoother user experience.
- Ensured data handling complies with privacy standards.

## Development and Testing
### Development Approach
- Mapped out the user journey and data flow to ensure a seamless experience.
- Followed a modular development approach for independent feature testing and integration.
- Adopted MVVM architecture for maintainable and testable code.

### Testing
- Performed thorough unit and integration tests on individual features.
- Conducted end-to-end testing to ensure feature compatibility and data integrity.
- Tested the app on various devices and Android versions to ensure consistent performance.

### Wrap-Up
The latest update to FooBook_Android represents a significant leap forward in functionality and user experience. We invite you to test these new features, provide feedback, and contribute to further improvements.
For more detailed information on each update, refer to the individual Jira tasks linked within this document.
