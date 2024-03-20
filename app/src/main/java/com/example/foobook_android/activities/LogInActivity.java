package com.example.foobook_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import retrofit2.Retrofit;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.foobook_android.Api.LoginRequest;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.ViewModels.LoginViewModel;
import com.example.foobook_android.utility.FieldValidation;
import com.example.foobook_android.R;
import java.util.Arrays;
import java.util.HashSet;

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


    private void setupListeners() {
        btnSignup.setOnClickListener(v -> {
            Intent signup = new Intent(this, SignUpActivity.class);
            startActivity(signup);
        });
        btnLogin.setOnClickListener(v -> attemptLogin(inputUsername.getText().toString(),
                inputPassword.getText().toString()));
    }

    private void setUpViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getloginResponseLiveData().observe(this, loginResponse -> {
            if (SUCCESS.equals(loginResponse.getResult())) {
                // Assuming userDetails are now fetched within the ViewModel, you might not need further action here.
            } else if (FAILURE.equals(loginResponse.getResult())) {
                Toast.makeText(getApplicationContext(), loginResponse.getReason(), Toast.LENGTH_LONG).show();
            }
        });

        loginViewModel.getUserDetailsLiveData().observe(this, userDetails -> {
            if (userDetails != null) {
                saveUserDetails(loginViewModel.getloginResponseLiveData().getValue().getUserId(), loginViewModel.getloginResponseLiveData().getValue().getToken(),
                        userDetails.getDisplayName(), userDetails.getFriendList().toArray(new String[0]),
                        userDetails.getProfilePic());
                Toast.makeText(getApplicationContext(), "You're in! Let's get started.", Toast.LENGTH_SHORT).show();
                navigateToFeedActivity();
            }
        });

        loginViewModel.getErrorLiveData().observe(this, error -> Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show());
    }

    private void setupFieldValidation() {
        FieldValidation.setupFieldValidation(inputUsername);
        FieldValidation.setupFieldValidation(inputPassword);
    }

    private void initializeViews() {
        inputUsername = findViewById(R.id.loginUsername);
        inputPassword = findViewById(R.id.loginPassword);
        btnSignup = findViewById(R.id.loginBenSignup);
        btnLogin = findViewById(R.id.loginBtnLogin);
    }

    public void attemptLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        loginViewModel.login(loginRequest);
    }

    private void saveUserDetails(String userId, String token, String displayName, String[] friendList, String profilePicUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // This clears all data
        editor.putString("userId", userId);
        editor.putString("token", token);
        editor.putString("displayName", displayName);
        editor.putStringSet("friendList", new HashSet<>(Arrays.asList(friendList))); // Convert array to Set
        editor.putString("profilePicUrl", profilePicUrl);
        editor.commit();

    }

    private void navigateToFeedActivity() {
        startActivity(new Intent(this, FeedActivity.class));
    }

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