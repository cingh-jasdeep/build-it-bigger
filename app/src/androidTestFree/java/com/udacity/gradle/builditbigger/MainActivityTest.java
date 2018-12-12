package com.udacity.gradle.builditbigger;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

// source: firebase github
// https://github.com/firebase/quickstart-android/blob/master/admob/app/src/androidTest/java/com/google/samples/quickstart/admobexample/InterstitialAdTest.java

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private AdViewIdlingResource mAdResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mAdResource = new AdViewIdlingResource(mActivityTestRule
                .getActivity().getAdView());
        Espresso.registerIdlingResources(mAdResource);
    }

    @After
    public void tearDown() {
        if(mAdResource != null) {
            Espresso.unregisterIdlingResources(mAdResource);
        }
    }

    @Test
    public void clickButton_RetrievesNonEmptyJoke() {
        // Wait for ad to load
        mAdResource.setIsLoadingAd(true);

        // Confirm that banner ad appears
        onView(withId(R.id.adView))
                .check(matches(isDisplayed()));

        //click the interstitial button
        onView(withId(R.id.button_tell_joke))
                .perform(click());

        // Click close interstitial button
        ViewInteraction closeInterstitialButton = onView(
                allOf(
                        withContentDescription("Interstitial close button"),
                        isDisplayed()
                ));

        closeInterstitialButton.perform(click());

        // Confirm that we're on the second activity and joke is displayed
        onView(withId(R.id.tv_joke_display))
                .check(matches(not(withText(""))));
    }

}
