package com.example.foobook_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foobook_android.utility.FieldValidation;
import com.example.foobook_android.R;

public class LogInActivity extends AppCompatActivity {

    private EditText inputUsername, inputPassword;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsername = findViewById(R.id.loginUsername);
        inputPassword = findViewById(R.id.loginPassword);
        btnSignup = findViewById(R.id.loginBenSignup);

        // Signup button
        btnSignup.setOnClickListener(v -> {
            Intent signup = new Intent(this, SignUpActivity.class);
            startActivity(signup);
        });
        Log.i("LogInActivity", "onCreate");

        // Login button
        Button btnLogin = findViewById(R.id.loginBtnLogin);
        btnLogin.setOnClickListener(v -> logIn(inputUsername.getText().toString(),
                                    inputPassword.getText().toString()));

        FieldValidation.setupFieldValidation(inputUsername);
        FieldValidation.setupFieldValidation(inputPassword);
    }

    public void logIn(String username, String password) {
        if (username.equals("Tomer") && password.equals("a5k8b123")) {
            // Display a success message
            Toast.makeText(this, "You're successfully logged in.", Toast.LENGTH_SHORT).show();

            // Proceed with navigation to FeedActivity
            Intent feed = new Intent(this, FeedActivity.class);
            startActivity(feed);
        } else {
            Toast.makeText(this, "Oops! We couldn't log you in. Please check your details and try again.",                    Toast.LENGTH_SHORT).show();
        }
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