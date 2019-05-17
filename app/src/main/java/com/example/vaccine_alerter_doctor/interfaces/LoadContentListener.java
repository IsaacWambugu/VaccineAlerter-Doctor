package com.example.vaccine_alerter_doctor.interfaces;

import android.util.Pair;

import org.json.JSONObject;

public interface LoadContentListener {

    void onLoadValidResponse(JSONObject response);
    void onLoadErrorResponse(Pair response);

}
