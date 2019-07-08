package izo.apps.vaccine_alerter_doctor.models;

public class MenuModel {
    private String menuItem;
    private String color;
    private int option;

    public MenuModel(int option, String menuItem, String color){
        this.menuItem = menuItem;
        this.color = color;
        this.option = option;

    }
    public String getColor(){
        return this.color;
    }
    public String getMenuItem() {
        return this.menuItem;
    }
    public int getOption(){
        return this.option;
    }
}
