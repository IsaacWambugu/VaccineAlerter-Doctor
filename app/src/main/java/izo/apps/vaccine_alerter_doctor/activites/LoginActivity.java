package izo.apps.vaccine_alerter_doctor.activites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import izo.apps.vaccine_alerter_doctor.R;
import izo.apps.vaccine_alerter_doctor.data.PreferenceManager;
import izo.apps.vaccine_alerter_doctor.interfaces.LoadContentListener;
import izo.apps.vaccine_alerter_doctor.network.Mtandao;
import izo.apps.vaccine_alerter_doctor.network.NetWorker;
import izo.apps.vaccine_alerter_doctor.util.SoftKeyBoard;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LoginActivity extends AppCompatActivity implements LoadContentListener {

    private TextView id_text,password_text;
    private Button loginButton;
    private View rootView;
    private SpinKitView spinKitView;
    private int backPressCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUIConfig();

    }
    private void setUIConfig(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle("Login");
        spinKitView = (SpinKitView) findViewById(R.id.login_spin_kit);
        rootView = (View) findViewById(R.id.view_activity_login);
        id_text = (TextView) findViewById(R.id.id_text);
        password_text = (TextView) findViewById(R.id.password_text);
        loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String logInId = "";
                String logInPassword  = "";

                logInId = id_text.getText().toString();
                logInPassword = password_text.getText().toString();
                ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                if(logInId == "" || logInPassword == ""){

                    showSnackBar("Fill in all fields and try again");

                }else if(logInId.length()<5 ){

                    showSnackBar("Invalid Id number!");

                }else{

                    if(Mtandao.checkInternet(LoginActivity.this)){

                       // SoftKeyBoard.hideSoftKeyBoard(LoginActivity.this);

                        confirmLoginCredentials(logInId, logInPassword);

                    }else{

                        showSnackBar("Check your internet connection then try again!");
                    }

                }

            }
        });

    }
    private void confirmLoginCredentials(String id, String password){

        spinKitView.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        SoftKeyBoard.hideSoftKeyBoard(LoginActivity.this);
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

        }

        saveDoctorDetails(id, names, gender, phoneNo, apiKey);
        backPressCount = 0;
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onLoadErrorResponse(Pair response) {

        spinKitView.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        showSnackBar(response.second.toString());
    }

    public void showSnackBar(String msg){

        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).setActionTextColor(Color.RED)
                .setActionTextColor(Color.YELLOW).setAction(R.string.ok, new View.OnClickListener() {
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

    @Override
    public void onBackPressed() {

        backPressCount++;

        if(backPressCount>1){

            Intent start = new Intent(Intent.ACTION_MAIN);
            start.addCategory(Intent.CATEGORY_HOME);
            start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(start);

        }

    }
}

