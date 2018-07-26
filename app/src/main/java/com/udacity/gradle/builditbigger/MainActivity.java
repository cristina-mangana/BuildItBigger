package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.jokedisplaylibrary.JokeActivity;


public class MainActivity extends AppCompatActivity {

    public static final String INTENT_KEY = "joke";
    private ProgressBar mLoadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingSpinner = findViewById(R.id.loading_spinner);

        // Progress bar color
        // https://stackoverflow.com/questions/26962136/indeterminate-circle-progress-bar-on-android-is-white-despite-coloraccent-color
        if (mLoadingSpinner.getIndeterminateDrawable() != null) {
            mLoadingSpinner.getIndeterminateDrawable()
                    .setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
                            android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        mLoadingSpinner.setVisibility(View.VISIBLE);
        new EndpointsAsyncTask(this, new EndpointsAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String joke) {
                Intent openJokeActivity = new Intent(getApplicationContext(), JokeActivity.class);
                if (joke != null && !joke.isEmpty()) openJokeActivity.putExtra(INTENT_KEY, joke);
                startActivity(openJokeActivity);
            }
        }).execute();
        mLoadingSpinner.setVisibility(View.GONE);
    }
}
