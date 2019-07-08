package izo.apps.vaccine_alerter_doctor.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;
import izo.apps.vaccine_alerter_doctor.R;
import izo.apps.vaccine_alerter_doctor.data.Const;
import izo.apps.vaccine_alerter_doctor.data.PreferenceManager;
import izo.apps.vaccine_alerter_doctor.network.Mtandao;
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

    @Override
    protected void onResume() {
        super.onResume();
       // getDomain();

    }


    private void getDomain() {


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (Mtandao.checkInternet(getApplicationContext())) {

                    mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                    mFirebaseRemoteConfig.fetch(0)
                            .addOnCompleteListener(SplashActivity.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        mFirebaseRemoteConfig.activateFetched();
                                        String domain = mFirebaseRemoteConfig.getString("domain");
                                        Log.d("----->", "domain from firebase");
                                        Log.d("----->", "domain:" + domain);
                                        Const.setDomain(domain);
                                        new PreferenceManager(getApplicationContext()).setDomain(domain);
                                        // mFirebaseRemoteConfig.activateFetched();

                                        moveToHome();
                                    } else {

                                        showSnackBar();              }

                                }
                            });
                } else
                    showSnackBar();

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
    private void exitApp(){

        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);

    }
    private void moveToHome(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
