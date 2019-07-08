package izo.apps.vaccine_alerter_doctor.data;

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
            "bcg1",
            "opv1",
            "hepB1",

            "dpt1",
            "opv2",
            "hepB2",
            "hibB1",
            "pneu",
            "rota1",

            "opv3",
            "hepB3",
            "dpt2",
            "hibB2",
            "vitA1",
            "rota2",

            "vitA2",

            "measles",
            "yellow"
    };
    public static void setDomain(String domain){

        Const.domain = domain;
    }
}
