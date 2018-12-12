package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public AdView getAdView() {
        MainActivityFragment fragment =
                (MainActivityFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment);
        if(fragment != null) {
            return fragment.getAdView();
        } else {
            return null;
        }

    }
}
