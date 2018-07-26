package com.udacity.gradle.builditbigger;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.jokedisplaylibrary.JokeActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;

/**
 * Test that checks that the Async task successfully retrieves a non-empty string. The string
 * retrieved by the Async task is passed as an extra to an intent, so in this class, that extra is
 * checked through Intent Verification.
 * There's no need for adding Idling Resources when working with AsyncTask, as mentioned here:
 * https://developer.android.com/reference/android/support/test/espresso/IdlingResource
 */
@RunWith(AndroidJUnit4.class)
public class AsyncTaskTest {

    // Rule that helps to initialize Espresso-Intents before each test annotated with @Test and
    // releases Espresso-Intents after each test run.
    @Rule
    public IntentsTestRule<MainActivity> mTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void asyncTaskResultTest() {
        // Perform a click on the "tell joke" button
        onView(withId(R.id.tell_joke_button)).perform(click());

        // Check if the intent has a not null nor empty extra. This means that Async task successfully
        // retrieves a not null nor empty value.
        // Help from: http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html
        intended(allOf(
                hasComponent(JokeActivity.class.getName()),
                hasExtra(equalTo(MainActivity.INTENT_KEY), not(isEmptyOrNullString()))
        ));

        // Check if the joke is displayed in the new activity, and if it is not an empty string.
        // Help from: https://stackoverflow.com/questions/46598149/test-a-textview-value-is-not-empty-using-espresso-and-fail-if-a-textview-value-i?rq=1
        onView(withId(R.id.tv_joke)).check(matches(not(withText(""))));
    }
}
