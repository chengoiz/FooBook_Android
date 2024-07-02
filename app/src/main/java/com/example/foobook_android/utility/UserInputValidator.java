package com.example.foobook_android.utility;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

// Utility class for validating user input.
public class UserInputValidator {
    private final EditText inputUserName, inputPassword, inputPasswordVer, inputDisplayName;
    private boolean isPhotoSelected;
    private final Context context;

    // Modify the constructor to include Context
    public UserInputValidator(Context context, EditText inputUserName, EditText inputPassword, EditText inputPasswordVer, EditText inputDisplayName, Boolean isPhotoSelected) {
        this.context = context;
        this.inputUserName = inputUserName;
        this.inputPassword = inputPassword;
        this.inputPasswordVer = inputPasswordVer;
        this.inputDisplayName = inputDisplayName;
        this.isPhotoSelected = isPhotoSelected;
    }

    // Setter for photo selection status
    public void setPhotoSelected(boolean photoSelected) {
        this.isPhotoSelected = photoSelected;
    }

    // Validates user input
    public boolean isInputValid() {
        String userName = inputUserName.getText().toString();
        String password = inputPassword.getText().toString();
        String passwordVer = inputPasswordVer.getText().toString();
        String displayName = inputDisplayName.getText().toString();

        if (userName.isEmpty()) {
            Toast.makeText(context, "Must insert username", Toast.LENGTH_SHORT).show();
        } else if (!PasswordValidator.validatePassword(password)) {
            Toast.makeText(context, "Password must be at least 8 figures\n" +
                            "and contain at least 1 letter and 1 number.",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!passwordVer.equals(password)) {
            Toast.makeText(context, "Password not equal", Toast.LENGTH_SHORT).show();
        } else if (displayName.isEmpty()) {
            Toast.makeText(context, "Must insert display name", Toast.LENGTH_SHORT).show();

        } else if (!isPhotoSelected) {
            Toast.makeText(context, "Please select or capture a photo.", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }
}
