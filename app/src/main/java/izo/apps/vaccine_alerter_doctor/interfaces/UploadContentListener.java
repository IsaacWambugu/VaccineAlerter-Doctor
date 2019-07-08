package izo.apps.vaccine_alerter_doctor.interfaces;

import android.util.Pair;

import org.json.JSONObject;

public interface UploadContentListener {
    void onUploadValidResponse(JSONObject response);
    void onUploadErrorResponse(Pair response);
}
