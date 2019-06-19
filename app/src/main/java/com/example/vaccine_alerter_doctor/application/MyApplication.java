package com.example.vaccine_alerter_doctor.application;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.example.vaccine_alerter_doctor.activites.SplashActivity;
import com.example.vaccine_alerter_doctor.data.Const;
import com.example.vaccine_alerter_doctor.data.PreferenceManager;
import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        if(!domainCheck()){

            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else{

            Const.setDomain(new PreferenceManager(getApplicationContext()).getDomain());
            Log.d("--->Application Domain", Const.domain);
        }

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private boolean domainCheck(){

         String domain = new PreferenceManager(getApplicationContext()).getDomain();

         if(domain == null)
             return false;
         else
             return true;
    }

}


