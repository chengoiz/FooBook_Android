package com.example.foobook_android.activities;

import android.content.Intent;
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

import java.util.Objects;

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
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getloginResponseLiveData().observe(this, loginResponse -> {
            if (Objects.equals(loginResponse.getResult(), SUCCESS)) {
                navigateToFeedActivity();
            } else if (Objects.equals(loginResponse.getResult(), FAILURE)) {
                Toast.makeText(getApplicationContext(), loginResponse.getReason(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setupListeners() {
        btnSignup.setOnClickListener(v -> {
            Intent signup = new Intent(this, SignUpActivity.class);
            startActivity(signup);
        });
        btnLogin.setOnClickListener(v -> attemptLogin(inputUsername.getText().toString(),
                inputPassword.getText().toString()));
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