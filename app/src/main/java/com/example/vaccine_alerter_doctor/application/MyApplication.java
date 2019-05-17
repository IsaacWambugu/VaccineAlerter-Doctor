package com.example.vaccine_alerter_doctor.application;

import android.app.Application;
import android.content.res.Configuration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}


