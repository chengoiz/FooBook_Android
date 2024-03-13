package com.example.foobook_android.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foobook_android.Api.LoginRequest;
import com.example.foobook_android.Api.WebServiceApi;
import com.example.foobook_android.ViewModels.LoginViewModel;
import com.example.foobook_android.models.User;
import com.example.foobook_android.utility.FieldValidation;
import com.example.foobook_android.R;
import com.example.foobook_android.utility.UserDetails;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity {

    public static final String FAILURE = "Failure";
    public static final String SUCCESS = "Success";
    private EditText inputUsername, inputPassword;
    private Retrofit retrofit;
    WebServiceApi webServiceApi;
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
                String userId = loginResponse.getUserId();
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build(); // Ensure you change the baseUrl as needed
                webServiceApi = retrofit.create(WebServiceApi.class);

                // Define the callback inside the observe method
                Callback<UserDetails> callback = new Callback<UserDetails>() {
                    @Override
                    public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                        if (response.isSuccessful()) {
                            UserDetails userDetails = response.body();
                            if (userDetails != null) {
                                String displayName = userDetails.getDisplayName();
                                String profilePicUrl = userDetails.getProfilePic();
                                String[] friendListArray = userDetails.getFriendList().toArray(new String[0]);
                                // Now, save the userDetails
                                saveUserDetails(userId, loginResponse.getToken(), displayName, friendListArray, profilePicUrl);
                                navigateToFeedActivity(); // Navigation is now correctly placed after successful user detail retrieval
                            }
                        } else {
                            Log.e("getUserDetails", "Failed to fetch user details: " + response.code());
                            Toast.makeText(getApplicationContext(), "Failed to load user details.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDetails> call, Throwable t) {
                        Log.e("getUserDetails", "Error fetching user details", t);
                        Toast.makeText(getApplicationContext(), "Error fetching user details.", Toast.LENGTH_LONG).show();
                    }
                };

                // Enqueue the callback with your Retrofit call to get user details
                webServiceApi.getUserDetails(userId, "Bearer " + loginResponse.getToken()).enqueue(callback);

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

    private void setUpViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getloginResponseLiveData().observe(this, loginResponse -> {
            if (Objects.equals(loginResponse.getResult(), SUCCESS)) {
                String userId = loginResponse.getUserId();
                retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build(); // change the baseUrl later
                webServiceApi = retrofit.create(WebServiceApi.class);

                // Define the callback right here
                Callback<UserDetails> callback = new Callback<UserDetails>() {
                    @Override
                    public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                        if (response.isSuccessful()) {
                            UserDetails userDetails = response.body();
                            if (userDetails != null) {
                                String displayName = userDetails.getDisplayName();
                                String profilePicUrl = userDetails.getProfilePic();
                                String[] friendListArray = userDetails.getFriendList().toArray(new String[0]);
                                saveUserDetails(loginResponse.getUserId(), loginResponse.getToken(), displayName, friendListArray, profilePicUrl);
                                navigateToFeedActivity(); // Move navigation inside callback on successful retrieval
                            }
                        } else {
                            Log.e("getUserDetails", "Failed to fetch user details: " + response.code());
                            Toast.makeText(getApplicationContext(), "Failed to load user details.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDetails> call, Throwable t) {
                        Log.e("getUserDetails", "Error fetching user details", t);
                        Toast.makeText(getApplicationContext(), "Error fetching user details.", Toast.LENGTH_LONG).show();
                    }
                };

                // Now, enqueue the callback with your Retrofit call
                webServiceApi.getUserDetails(userId, "Bearer " + loginResponse.getToken()).enqueue(callback);

                // Note: Moved navigateToFeedActivity() into the callback above
            } else if (Objects.equals(loginResponse.getResult(), FAILURE)) {
                Toast.makeText(getApplicationContext(), loginResponse.getReason(), Toast.LENGTH_LONG).show();
            }
        });
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
        editor.putString("userId", userId);
        editor.putString("token", token);
        editor.putString("displayName", displayName);
        editor.putStringSet("friendList", new HashSet<>(Arrays.asList(friendList))); // Convert array to Set
        editor.putString("profilePicUrl", profilePicUrl);
        editor.apply();
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