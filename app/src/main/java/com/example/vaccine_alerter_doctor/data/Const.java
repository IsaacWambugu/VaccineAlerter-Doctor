package com.example.vaccine_alerter_doctor.data;

public class Const {
    //API calls
    public static final String DOMAIN = "http://192.168.1.247:5657";
    public static final String GET_CHILDREN_URL = DOMAIN + "/api/children/";
    public static final String GET_CHILDREN_DETAILS_URL = DOMAIN + "/api/children/details/";
    public static final String LOGIN_URL = DOMAIN +"/api/doctor/signin";
    public static final String ADD_CHILD_URL = DOMAIN + "/api/children/add";
    public static final String GET_GUARDIAN_DETAILS = DOMAIN+"/api/guardians/";
    public static final String ADD_GUARDIAN_DETAILS = DOMAIN+"/api/guardian/add";
    //Notification
    public static final String CHANNEL_ID = "VaccineAlert";

}
