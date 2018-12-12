package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.jokedisplayer.JokeDisplayActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    private EndpointGetJokeAsyncTask mAsyncTask;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private ProgressBar mJokeLoadingProgressBar;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        root.findViewById(R.id.button_tell_joke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
            }
        });

        mJokeLoadingProgressBar = root.findViewById(R.id.pb_joke_loading);
        mJokeLoadingProgressBar.setVisibility(View.GONE);

        Context context = getContext();
        if(context != null) {
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
            loadInterstitial();

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    loadInterstitial();
                    tellJoke();
                }
            });
        }

        mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }

    private void loadInterstitial() {
        if(mInterstitialAd != null) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    private void showInterstitial(){
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(getContext(), "Ad did not load", Toast.LENGTH_SHORT).show();
            tellJoke();
        }
    }

    public void tellJoke() {

        mJokeLoadingProgressBar.setVisibility(View.VISIBLE);

        if(mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }

        mAsyncTask = new EndpointGetJokeAsyncTask();
        mAsyncTask.execute(getContext());
    }

    public AdView getAdView() {
        return mAdView;
    }

    public class EndpointGetJokeAsyncTask extends AsyncTask<Context, Void, String> {
        private MyApi myApiService = null;
        private Context context;

        @Override
        protected String doInBackground(Context... contexts) {
            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            context = contexts[0];

            try {
                return myApiService.getJoke().execute().getData();
            } catch (IOException e) {
                Log.e(TAG, "EndpointGetJokeAsyncTask: " + e.getMessage());
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            mJokeLoadingProgressBar.setVisibility(View.GONE);
            Intent intentToDisplayJoke = new Intent(context, JokeDisplayActivity.class);
            intentToDisplayJoke.putExtra(JokeDisplayActivity.EXTRA_JOKE_TEXT, result);
            startActivity(intentToDisplayJoke);
        }
    }
}
