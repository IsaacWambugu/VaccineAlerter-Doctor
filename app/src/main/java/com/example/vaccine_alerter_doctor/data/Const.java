package com.example.vaccine_alerter_doctor.data;

import android.util.Log;

public class Const {
    //API calls
    public static String domain ;
    public static final String GET_CHILDREN_PATH = "/api/children/";
    public static final String GET_CHILDREN_DETAILS_PATH =  "/api/children/details/";
    public static final String LOGIN_PATH = "/api/doctor/signin";
    public static final String ADD_CHILD_PATH =  "/api/children/add";
    public static final String CHILD_PATH = "/api/children/";
    public static final String GET_GUARDIAN_DETAILS_PATH = "/api/guardians/";
    public static final String ADD_GUARDIAN_DETAILS_PATH = "/api/guardian/add";
    public static final String DELETE_GUARDIAN_PATH = "/api/guardians/";
    public static final String DELETE_CHILD_PATH = "/api/children/";
    //Notification
    public static final String CHANNEL_ID = "VaccineAlert";
    //Vaccine list
    public static final String[] VACCINE_LIST = {
            "opv1",
            "bcg1",
            "hepB1",
            "dpt1",
            "hibB1",
            "hepB2",
            "opv2",
            "pneu",
            "rota1",
            "dpt2",
            "hibB2",
            "hepB3",
            "opv3",
            "vitA1",
            "rota2",
            "vitA2",
            "measles",
            "yellow"
    };
    public static void setDomain(String domain){

        Const.domain = domain;
        Log.d("---Setting domain:", domain);
    }
}
