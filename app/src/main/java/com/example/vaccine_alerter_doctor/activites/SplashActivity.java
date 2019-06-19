package com.example.vaccine_alerter_doctor.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;
import com.example.vaccine_alerter_doctor.R;
import com.example.vaccine_alerter_doctor.data.Const;
import com.example.vaccine_alerter_doctor.data.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        getDomain();
    }

    private void showSplashScreen() {

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(2 * 1000);
                    Intent intent  = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //Remove activity
                    finish();
                } catch (Exception e) {


                }
            }
        };

        background.start();

    }

    private void getDomain(){

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.reset();
        mFirebaseRemoteConfig.ensureInitialized();
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(SplashActivity.this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            String currentDomain = new PreferenceManager(getApplicationContext()).getDomain();
                            if(updated){

                                Log.d("--->","fetch and updated");
                                String domain = mFirebaseRemoteConfig.getString("domain");
                                Const.setDomain(domain);
                                new PreferenceManager(getApplicationContext()).setDomain(domain);
                                Log.d("--->",mFirebaseRemoteConfig.getString("domain"));
                            }else{
                                Log.d("--->","fetch but not updated");
                                Const.setDomain(currentDomain);

                            }

                            showSplashScreen();
                        } else {
                            Log.d("--->","fetch failed");
                           showSnackBar();
                        }

                    }
                });
    }

    public void showSnackBar(){
        View rootView = (View) findViewById(R.id.activity_splash_view);
        Snackbar.make(rootView, "App could not initialize, check internet connection", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.YELLOW)
                .setAction(R.string.Retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getDomain();
                    }
                }).show();

    }

}
