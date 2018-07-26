package com.example.android.jokedisplaylibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class JokeActivity extends AppCompatActivity {
    private static final String INTENT_KEY = "joke";
    public static final String RANDOM_NUMBER_KEY = "randomNumber";

    private int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        // Restore state from saved instance
        int[] background = getResources().getIntArray(R.array.joke_background);
        if (savedInstanceState != null) {
            randomNumber = savedInstanceState.getInt(RANDOM_NUMBER_KEY);
        } else {
            randomNumber = new Random().nextInt(background.length);
        }

        // Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() == null && !getIntent().hasExtra(INTENT_KEY)) {
            showError();
        }

        // Display joke
        String joke = getIntent().getExtras().getString(INTENT_KEY);
        TextView jokeTextView = findViewById(R.id.tv_joke);
        jokeTextView.setText(joke);

        // Set random background color
        FrameLayout layout = findViewById(R.id.joke_layout);
        layout.setBackgroundColor(background[randomNumber]);
    }

    // Fires when a configuration change occurs and fragment needs to save state
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(RANDOM_NUMBER_KEY, randomNumber);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Helper method to finish the activity when an error occur.
     */
    private void showError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        finish();
    }

    // Handle back button on toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
