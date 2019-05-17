package com.example.vaccine_alerter_doctor.interfaces;

import android.util.Pair;

import org.json.JSONObject;

public interface IdCheckerListener {

    void onValidResponse(JSONObject response);
    void onErrorResponse(Pair response);
}
