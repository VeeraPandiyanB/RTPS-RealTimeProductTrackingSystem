package com.example.integratedapplication;

public class ListModel {

    private String  barcode;
    private String  GpsLocation;
    private String rfid;
    private String compassValue;

    public ListModel(String barcode, String gpsLocation,String rfId,String compass) {
        this.barcode = barcode;
        GpsLocation = gpsLocation;
        rfid = rfId;
        compassValue = compass;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGpsLocation() {
        return GpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        GpsLocation = gpsLocation;
    }

    public String getCompassValue() {
        return compassValue;
    }

    public void setCompassValue(String compassValue) {
        this.compassValue = compassValue;
    }
}
