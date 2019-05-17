package com.example.vaccine_alerter_doctor.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PreferenceManager {

    final private int mode = Activity.MODE_PRIVATE;
    final private String myPrefs = "MyPreferences_001";
    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public PreferenceManager(Context context){

        sharedPref = context.getSharedPreferences(myPrefs, mode);

    }
    public void setApiKey(String apiKey){

        editor  = sharedPref.edit();
        editor.putString("api_key", apiKey);
        editor.apply();

    }
    public String getApiKey(){

        return sharedPref.getString("api_key", null);

    }
    public void setDoctorId(int id){

        editor  = sharedPref.edit();
        editor.putInt("doctor_id", id);
        editor.apply();

    }

    public int getDoctorId(){

        return sharedPref.getInt("doctor_id", -1);
    }

    public void setDate(){

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        editor  = sharedPref.edit();
        editor.putString("date", date);
        editor.apply();
    }
    public String getDate(){

        return sharedPref.getString("date", null);
    }

    public void setDoctorName(String name){

        editor  = sharedPref.edit();
        editor.putString("doctor_name", name);
        editor.apply();

    }
    public String getDoctorName(){

        return sharedPref.getString("doctor_name", null);
    }

    public void setDoctorNumber(String number){

        editor  = sharedPref.edit();
        editor.putString("doctor_number", number);
        editor.apply();

    }
    public String getDoctorNumber(){

        return sharedPref.getString("doctor_number", null);
    }

    public void setDoctorGender(String gender){

        editor  = sharedPref.edit();
        editor.putString("doctor_gender", gender);
        editor.apply();

    }
    public String getDoctorGender(){

        return sharedPref.getString("doctor_gender", null);
    }
}
