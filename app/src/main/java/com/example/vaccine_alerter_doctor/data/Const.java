package com.example.vaccine_alerter_doctor.data;
public class Const {
    //API calls
    public static final String DOMAIN = "http://192.168.8.104:5657";
    public static final String GET_CHILDREN_URL = DOMAIN + "/api/children/";
    public static final String GET_CHILDREN_DETAILS_URL = DOMAIN + "/api/children/details/";
    public static final String LOGIN_URL = DOMAIN + "/api/doctor/signin";
    public static final String ADD_CHILD_URL = DOMAIN + "/api/children/add";
    public static final String GET_GUARDIAN_DETAILS = DOMAIN + "/api/guardians/";
    public static final String ADD_GUARDIAN_DETAILS = DOMAIN + "/api/guardian/add";
    //Notification
    public static final String CHANNEL_ID = "VaccineAlert";
    public static final String[] VACCINE_LIST = {
            "OPV1",
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
}
