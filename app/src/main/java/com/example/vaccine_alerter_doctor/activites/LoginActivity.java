package com.example.vaccine_alerter_doctor.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vaccine_alerter_doctor.R;
import com.example.vaccine_alerter_doctor.data.PreferenceManager;
import com.example.vaccine_alerter_doctor.interfaces.LoadContentListener;
import com.example.vaccine_alerter_doctor.network.NetWorker;
import com.example.vaccine_alerter_doctor.network.Mtandao;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements LoadContentListener {

    private TextView id_text,password_text;
    private Button loginButton;
    private View rootView;
    private ProgressBar login_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUIConfig();

    }
    private void setUIConfig(){

        rootView = (View) findViewById(R.id.view_activity_login);
        id_text = (TextView) findViewById(R.id.id_text);
        password_text = (TextView) findViewById(R.id.password_text);
        loginButton = (Button) findViewById(R.id.button_login);
        login_progressBar = (ProgressBar)findViewById(R.id.login_progress_bar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String logInId = "";
                String logInPassword  = "";

                logInId = id_text.getText().toString();
                logInPassword = password_text.getText().toString();

                if(logInId == "" || logInPassword == ""){

                    showSnackBar("Fill in all fields and try again");

                }else if(logInId.length()<5 ){

                    showSnackBar("Invalid Id number!");

                }else{

                    if(Mtandao.checkInternet(LoginActivity.this)){

                        confirmLoginCredentials(logInId, logInPassword);

                    }else{

                        showSnackBar("Check your internet connection then try again!");
                    }

                }

            }
        });

    }
    private void confirmLoginCredentials(String id, String password){

        login_progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);

        new NetWorker().userLoginRequest(LoginActivity.this, id, password);

    }

    @Override
    public void onLoadValidResponse(JSONObject response){

        String apiKey = null;
        int id = -1;
        String names = "";
        String gender ="";
        String phoneNo= "";


        try {

            apiKey = response.getString("token");
            id = response.getInt("id");
            names = response.getString("full_name");
            gender = response.getString("gender");
            phoneNo = String.valueOf(response.getString("phone_number"));

        }catch (JSONException jsonE) {


            Log.d("Err---->", jsonE.toString());
        }

        saveDoctorDetails(id, names, gender, phoneNo, apiKey);
        Intent intent = new Intent(LoginActivity.this, VaccinationActivity.class);
        startActivity(intent);

    }

    @Override
    public void onLoadErrorResponse(Pair response) {

        login_progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        showSnackBar(response.second.toString());
    }

    public void showSnackBar(String msg){

        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();

    }
    private void saveDoctorDetails(int id, String names, String gender, String phoneNumber, String apiKey){

        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        preferenceManager.setDoctorId(id);
        preferenceManager.setDoctorName(names);
        preferenceManager.setDoctorGender(gender);
        preferenceManager.setDoctorNumber(phoneNumber);
        preferenceManager.setApiKey(apiKey);

    }
}

