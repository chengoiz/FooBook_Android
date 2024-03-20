package com.example.foobook_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.foobook_android.Api.LoginRequest;
import com.example.foobook_android.ViewModels.LoginViewModel;
import com.example.foobook_android.utility.FieldValidation;
import com.example.foobook_android.R;
import java.util.Arrays;
import java.util.HashSet;

/**
 * LogInActivity handles user authentication including login and sign-up functionalities.
 * It provides inputs for username and password, and buttons for logging in and signing up.
 * The activity leverages a ViewModel for authentication processes, observing login status
 * and handling success or failure cases accordingly. Successful login attempts lead to saving
 * user details and navigating to the FeedActivity. It also implements basic field validation.
 */
public class LogInActivity extends AppCompatActivity {

    public static final String FAILURE = "Failure";
    public static final String SUCCESS = "Success";
    private EditText inputUsername, inputPassword;
    Button btnSignup, btnLogin;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        setupFieldValidation();
        setupListeners();
        Log.i("LogInActivity", "onCreate");
        setUpViewModel();
    }

    // Initializes UI components
    private void initializeViews() {
        inputUsername = findViewById(R.id.loginUsername);
        inputPassword = findViewById(R.id.loginPassword);
        btnSignup = findViewById(R.id.loginBenSignup);
        btnLogin = findViewById(R.id.loginBtnLogin);
    }

    // Sets up click listeners for signup and login actions
    private void setupListeners() {
        btnSignup.setOnClickListener(v -> {
            // Navigate to the SignUpActivity
            Intent signup = new Intent(this, SignUpActivity.class);
            startActivity(signup);
        });
        btnLogin.setOnClickListener(v -> {
            // Attempt login with the provided username and password
            attemptLogin(inputUsername.getText().toString(), inputPassword.getText().toString());
        });
    }

    // Sets up the ViewModel and observes LiveData objects for authentication responses
    private void setUpViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observes login response LiveData
        loginViewModel.getloginResponseLiveData().observe(this, loginResponse -> {
            if (SUCCESS.equals(loginResponse.getResult())) {
                // Handle success case
            } else if (FAILURE.equals(loginResponse.getResult())) {
                // Display failure reason
                Toast.makeText(getApplicationContext(), loginResponse.getReason(), Toast.LENGTH_LONG).show();
            }
        });

        // Observes user details LiveData after successful login
        loginViewModel.getUserDetailsLiveData().observe(this, userDetails -> {
            if (userDetails != null) {
                // Save user details and navigate to FeedActivity
                saveUserDetails(loginViewModel.getloginResponseLiveData().getValue().getUserId(),
                        loginViewModel.getloginResponseLiveData().getValue().getToken(),
                        userDetails.getDisplayName(), userDetails.getFriendList().toArray(new String[0]),
                        userDetails.getProfilePic());
                Toast.makeText(getApplicationContext(), "You're in! Let's get started.", Toast.LENGTH_SHORT).show();
                navigateToFeedActivity();
            }
        });

        // Observes LiveData for any errors during login process
        loginViewModel.getErrorLiveData().observe(this, error ->
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show());
    }

    // Sets up validation for input fields
    private void setupFieldValidation() {
        FieldValidation.setupFieldValidation(inputUsername);
        FieldValidation.setupFieldValidation(inputPassword);
    }

    // Attempts login with the specified username and password
    public void attemptLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        loginViewModel.login(loginRequest);
    }

    // Saves user details into SharedPreferences
    private void saveUserDetails(String userId, String token, String displayName, String[] friendList, String profilePicUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear existing data
        editor.putString("userId", userId);
        editor.putString("token", token);
        editor.putString("displayName", displayName);
        editor.putStringSet("friendList", new HashSet<>(Arrays.asList(friendList))); // Convert array to Set
        editor.putString("profilePicUrl", profilePicUrl);
        editor.commit(); // Commit changes
    }

    // Navigates to FeedActivity
    private void navigateToFeedActivity() {
        startActivity(new Intent(this, FeedActivity.class));
    }

    // Lifecycle methods with log statements for debugging
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LogInActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LogInActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LogInActivity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LogInActivity", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LogInActivity", "onRestart");
    }
}