# FooBook_Android
## Part 2 of the Final Project in the Advanced Systems Programming Course

### Getting Started
Follow these instructions to get a copy of the project up and running on your local machine for development.

### Installing
A step-by-step series to get a development environment running:

1. **Clone the repo or download the ZIP file**
    - Git clone `https://github.com/RoeiMesi/FooBook_Android`
    - Open Android Studio and press "Get from VCS" and paste the URL.
    - Or download and extract the ZIP file from the GitHub repository page and open this folder from Android Studio.

2. **Android SDK**
    - Your Android Studio SDK needs to be configured to:
        - Minimal API Level: 29.
        - Optimal API Level: 34.

3. **Sync project with gradle files and add configuration**
   - After cloning, at the top right of your IDE, press the "Sync Project with Gradle Files".
   - At the top of your screen click on "Add configuration" -> Add new -> Android App
   - -> Pick a name for the app, and in the 'Module' category, choose: FooBook_Android.app.main

4. **Make sure you have an emulator or device set up.**

5. **Build and run the application.**

### Login
To login, use our hardcoded credentials:
- Username: Tomer
- Password: a5k8b123

### Registration
- All the fields are required for the registration.
- Your password must be of size 8 characters and include at least one letter and one number.
- Note: For this part of the project registration is not saved and you can login only with the hardcoded details, valid registration will transport you back to the login page.

### Feed
- **Adding a Post**: To add a post, click the "PLUS" button at the top of the screen. This will transport you to a new page where you can manage all your input. Blank posts are permitted.
- **Editing/Deleting a Post**: To edit or delete a post, click the edit button located in the upper right corner of the post.
- **Editing functionalities**: Editing will transport you to a new page where you can change the text if wanted, change the photo if wanted, or remove the current photo of the post. If no photo exists, there will be no button to remove it.
- **Commenting on a Post**: To comment on a post, you need to click on the comment button, this will transport you to a new page where you will see all the comments.
- **Adding a Comment**: To add a comment, type your text into the input box and press the send icon.
- **Editing a Comment**: To edit a comment, click the edit button, make your changes, and then click save icon.
- **Deleting a Comment**: To delete a comment, click the delete button.
- **Liking a Post**: To like a post, simply click the like button.
- **Logging Out**: To logout, click the logout button located on the lower right side of the page.
- **Dark Mode**: To switch to dark mode, press the dark mode button on the top of the screen.

# Project Development Approach

## Starting Out

- Initial inspiration came from examining the Facebook app to determine the design elements and views we wanted to incorporate.
- With a vision for our basic structure, we began crafting the activity layouts using XML within Android Studio.
- We drew each page on a piece of paper to give us a sense of direction as for the design part.

## Adding Functionality

- With our main layouts established, we turned our attention to incorporating various functionalities across the app.
- To maintain organization and adhere to the principle of separation of concerns, we introduced new classes responsible for distinct functionalities such as managing posts and comments, implementing validators, and more.
- Efforts were also placed on enhancing user interactions, adding navigational flows between different app pages, managing permissions, among other enhancements.

## Updating the Design

- After embedding the core functionalities, we revisited our design, implementing adjustments to refine the app's aesthetics and user experience.

## Testing and Fixing

- The app underwent rigorous testing on a variety of devices and Android versions to identify and rectify any issues, ensuring compatibility and a smooth user experience across different platforms.

## Wrap-up

- Our development journey began with a simple conceptual sketch to capture the envisioned look and feel of the app. Following this, we laid out the foundational design, progressively building upon it with essential functionalities. Subsequent phases focused on design refinement, extensive testing on multiple devices, and necessary adjustments to deliver an app that performs seamlessly across a wide range of devices, embodying our initial vision and objectives.