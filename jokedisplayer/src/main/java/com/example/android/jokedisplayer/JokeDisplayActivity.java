package com.example.android.jokedisplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class JokeDisplayActivity extends AppCompatActivity {

    public static final String EXTRA_JOKE_TEXT = "joke-text";
    private TextView mJokeDisplayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);

        if(savedInstanceState == null) {
            Intent intentFromCallingActivity = getIntent();
            if(intentFromCallingActivity != null && intentFromCallingActivity.hasExtra(EXTRA_JOKE_TEXT)) {
                String jokeTextFromIntent = intentFromCallingActivity.getStringExtra(EXTRA_JOKE_TEXT);

                mJokeDisplayTextView = findViewById(R.id.tv_joke_display);

                mJokeDisplayTextView.setText(jokeTextFromIntent);

            } else {
                Toast.makeText(this, "Error: No joke text found!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
