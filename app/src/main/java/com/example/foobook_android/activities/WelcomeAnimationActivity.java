package com.example.foobook_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.foobook_android.R;

public class WelcomeAnimationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_animation_activity);

        TextView welcomeText = findViewById(R.id.welcomeText);
        TextView enjoyYourStayText = findViewById(R.id.enjoyYourStayText);
        ImageView foobookLogo = findViewById(R.id.foobookLogo);
        Animation welcomeAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_animation);
        foobookLogo.startAnimation(welcomeAnimation);
        welcomeText.startAnimation(welcomeAnimation);
        enjoyYourStayText.startAnimation(welcomeAnimation);

        welcomeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation ended, start the login activity
                Intent intent = new Intent(WelcomeAnimationActivity.this, LogInActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeats
            }
        });
    }
}
