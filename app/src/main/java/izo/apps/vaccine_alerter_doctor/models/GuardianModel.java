package izo.apps.vaccine_alerter_doctor.models;

public class GuardianModel {
    String phone;
    String name;
    String id;
    String gender;

    public GuardianModel(String id, String phone, String fname, String lname, String gender){

        this.name = fname + " " +lname;
        this.phone = phone;
        this.id = id;
        this.gender = gender;

    }

    public String getNumber() {
        return phone;
    }
    public String getName(){
        return this.name;
    }
    public String getGender(){
        return  this.gender;

    }
    public String getId(){
       return this.id;
    }

}
