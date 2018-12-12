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

import com.example.android.jokedisplayer.JokeDisplayActivity;
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
    private ProgressBar mJokeLoadingProgressBar;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mJokeLoadingProgressBar = root.findViewById(R.id.pb_joke_loading);
        mJokeLoadingProgressBar.setVisibility(View.GONE);

        root.findViewById(R.id.button_tell_joke).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tellJoke(v);
            }
        });

        return root;
    }

    public void tellJoke(View view) {

        mJokeLoadingProgressBar.setVisibility(View.VISIBLE);

        if(mAsyncTask != null) {
            mAsyncTask.cancel(true);
        }

        mAsyncTask = new EndpointGetJokeAsyncTask();
        mAsyncTask.execute(getContext());
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
