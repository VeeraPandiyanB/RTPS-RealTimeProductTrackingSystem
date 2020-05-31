package com.example.integratedapplication;

public class Constants {

    private String barCode;
    private String coordinates;

//    public Constants(){}

    public Constants(String barCode, String coordinates) {
        this.barCode = barCode;
        this.coordinates = coordinates;
    }

    String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
