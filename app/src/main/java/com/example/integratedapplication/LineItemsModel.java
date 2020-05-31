package com.example.integratedapplication;

public class LineItemsModel {

    private String  barcode;
    private String  GpsLocation;
    private String rfid;
    public boolean isPicked;



    public LineItemsModel(String barcode, String gpsLocation, String rfid, boolean isPicked) {
        this.barcode = barcode;
        GpsLocation = gpsLocation;
        this.rfid = rfid;
        this.isPicked = isPicked;
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

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public boolean setPicked(boolean picked) {
       return isPicked=picked;
    }
}
