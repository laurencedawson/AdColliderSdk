package com.adcollider.demo;

import android.app.Activity;
import android.os.Bundle;

import com.adcollider.sdk.AdCollider;
import com.adcollider.sdk.views.AdView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdCollider.init(this, "replace-with-your-own-key");

        setContentView(R.layout.activity_main);

        AdView adView = findViewById(R.id.adColliderAdView);
        adView.requestAd();
    }

}
