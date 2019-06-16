package com.example.vaccine_alerter_doctor.network;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.vaccine_alerter_doctor.data.Const;
import com.example.vaccine_alerter_doctor.data.PreferenceManager;
import com.example.vaccine_alerter_doctor.interfaces.IdCheckerListener;
import com.example.vaccine_alerter_doctor.interfaces.LoadContentListener;
import com.example.vaccine_alerter_doctor.interfaces.UploadContentListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetWorker {

    private StringRequest stringRequest;
    private LoadContentListener loadContentListener;
    private IdCheckerListener idCheckerListener;
    private UploadContentListener uploadContentListener;

    public NetWorker() {


    }

    public void loadChildren(Context context, String id) {
        loadContentListener = (LoadContentListener) context;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, Const.GET_CHILDREN_URL + "33451647" + "/0/30", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        loadContentListener.onLoadValidResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loadContentListener.onLoadErrorResponse(checkErrorResponse(error));
                    }
                });

        NetworkSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void loadChildDetails(Context context, String id) {

        loadContentListener = (LoadContentListener) context;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, Const.GET_CHILDREN_DETAILS_URL + id, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        loadContentListener.onLoadValidResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loadContentListener.onLoadErrorResponse(checkErrorResponse(error));
                    }
                });

        NetworkSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public void userLoginRequest(Context context, String id, String password) {

        loadContentListener = (LoadContentListener) context;
        JSONObject jsonBodyObj = new JSONObject();

        try {

            jsonBodyObj.put("id_number", Integer.valueOf(id));
            jsonBodyObj.put("password", password);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, Const.LOGIN_URL, jsonBodyObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                        loadContentListener.onLoadValidResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loadContentListener.onLoadErrorResponse(checkErrorResponse(error));

                    }
                }) {

        };

        NetworkSingleton.getInstance(context).addToRequestQueue(postRequest);
    }

    public void loadDetails(Context context, int check, String guardianId) {

        idCheckerListener = (IdCheckerListener) context;
        String url = "";

        switch (check) {
            case 1:
                url = Const.GET_CHILDREN_DETAILS_URL;
                break;
            case 2:
                url = Const.GET_GUARDIAN_DETAILS_URL;
                break;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url + guardianId, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        idCheckerListener.onValidResponse(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        idCheckerListener.onErrorResponse(checkErrorResponse(error));
                    }
                });


        NetworkSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);


    }

    public void uploadChild(final Context context, int operation, String id, String fName, String lName, String dOB, String gender, String vaccines) {

        int requestMethod = 0;
        JSONObject jsonBodyObj = new JSONObject();
        String url = "";
        uploadContentListener = (UploadContentListener) context;


        if (operation == 1) {
            requestMethod = Request.Method.POST;
            url = Const.ADD_CHILD_URL;
        } else if (operation == 2) {
            requestMethod = Request.Method.PUT;
            url = Const.CHILD_URL + id;
        }

        try {

            if (operation == 1) {
                jsonBodyObj.put("guardian_id", id);
                jsonBodyObj.put("doctor_id", new PreferenceManager(context).getDoctorId());
            }

            jsonBodyObj.put("first_name", fName);
            jsonBodyObj.put("last_name", lName);
            jsonBodyObj.put("date_of_birth", dOB);
            jsonBodyObj.put("gender", gender);
            jsonBodyObj.put("vaccines", vaccines);


        } catch (JSONException e) {

            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(requestMethod, url, jsonBodyObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                        uploadContentListener.onUploadValidResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        uploadContentListener.onUploadErrorResponse(checkErrorResponse(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("auth-key", new PreferenceManager(context).getApiKey());

                return headers;
            }
        };

        NetworkSingleton.getInstance(context).addToRequestQueue(postRequest);

    }

    public void uploadGuardian(final Context context, int operation, String id, String fName, String lName, String phone, String dOB, String gender) {

        int requestMethod = 0;
        JSONObject jsonBodyObj = new JSONObject();
        String url = "";
        uploadContentListener = (UploadContentListener) context;

        if (operation == 1) {
            requestMethod = Request.Method.POST;
            url = Const.ADD_GUARDIAN_DETAILS_URL;
        } else if (operation == 2) {
            requestMethod = Request.Method.PUT;
            url = Const.GET_GUARDIAN_DETAILS_URL + id;
        }

        try {
            if (operation == 1) {
                jsonBodyObj.put("guardian_id", id);
            }
            jsonBodyObj.put("first_name", fName);
            jsonBodyObj.put("last_name", lName);
            jsonBodyObj.put("date_of_birth", dOB);
            jsonBodyObj.put("phone_number", phone);
            jsonBodyObj.put("gender", gender);

        } catch (JSONException e) {

            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(requestMethod, url, jsonBodyObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                        uploadContentListener.onUploadValidResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        uploadContentListener.onUploadErrorResponse(checkErrorResponse(error));

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("auth-key", new PreferenceManager(context).getApiKey());

                return headers;
            }
        };
        NetworkSingleton.getInstance(context).addToRequestQueue(postRequest);

    }
    public void deleteUser(final Context context, int operation, String id) {

        String url = "";
        uploadContentListener = (UploadContentListener) context;

        if (operation == 1)
            url = Const.DELETE_CHILD + id;
        else if (operation == 2)
            url = Const.DELETE_GUARDIAN + id;

        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                        uploadContentListener.onUploadValidResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        uploadContentListener.onUploadErrorResponse(checkErrorResponse(error));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("auth-key", new PreferenceManager(context).getApiKey());

                return headers;
            }
        };

        NetworkSingleton.getInstance(context).addToRequestQueue(deleteRequest);

    }

    private Pair<Integer, String> checkErrorResponse(VolleyError error) {

        String msg = "";
        NetworkResponse networkResponse = error.networkResponse;
        int code = 0;
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

            code = 1;
            msg = "Check message Internet connection and try again";
        } else if (error instanceof AuthFailureError) {
            code = 2;
            msg = "Check your login credentials and try again";
        } else if (error instanceof ServerError) {

            int statusCode = networkResponse.statusCode;
            try {

                msg = new JSONObject(new String(error.networkResponse.data, "UTF-8")).getString("message");

                if (statusCode == 404) {
                    code = 4;

                }if(statusCode == 409){
                    code = 1;

                }else {
                    code = 3;
                    // msg = "Server error try again later";
                }
            } catch (JSONException jE) {
                msg = "Check details and try again";

            } catch (java.io.UnsupportedEncodingException ioE) {
                msg = "Check details and try again";
            }
        } else if (error instanceof NetworkError) {
            code = 1;
            msg = "Check Internet connection and try again";
        } else if (error instanceof ParseError) {
            code = 2;
            msg = "Check you login credential and try again";

        }

        return Pair.create(code, msg);
    }
}



