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

3. **Make sure you have an emulator or device set up.**
4. **Build and run the application.**

### Login
To login, use our hardcoded credentials:
- Username: Tomer
- Password: a5k8b123

### Registration
If you register and then try to register with the same username, you will get an error because we used a local session for this step before implementing a hardcoded user. All the fields must to

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
