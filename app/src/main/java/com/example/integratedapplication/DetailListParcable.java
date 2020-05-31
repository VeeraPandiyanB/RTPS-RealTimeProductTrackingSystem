package com.example.integratedapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailListParcable implements Parcelable {

    private String barCodeParce,coordinatesParce;

    public DetailListParcable(String barCodeParce, String coordinatesParce) {
        this.barCodeParce = barCodeParce;
        this.coordinatesParce = coordinatesParce;
    }

//
//    public DetailListParcable(String barCodeParce, String coordinatesParce) {
//        this.barCodeParce = barCodeParce;
//        this.coordinatesParce = coordinatesParce;
//    }

    public String getBarCodeParce() {
        return barCodeParce;
    }

    public void setBarCodeParce(String barCodeParce) {
        this.barCodeParce = barCodeParce;
    }

    public String getCoordinatesParce() {
        return coordinatesParce;
    }

    public void setCoordinatesParce(String coordinatesParce) {
        this.coordinatesParce = coordinatesParce;
    }

//    public static Creator<DetailListParcable> getCREATOR() {
//        return CREATOR;
//    }

    protected DetailListParcable(Parcel in) {
    this.barCodeParce=in.readString();
    this.coordinatesParce=in.readString();
    }


    public static final Creator<DetailListParcable> CREATOR = new Creator<DetailListParcable>() {
        @Override
        public DetailListParcable createFromParcel(Parcel in) {
            return new DetailListParcable(in);
        }

        @Override
        public DetailListParcable[] newArray(int size) {
            return new DetailListParcable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.barCodeParce);
        parcel.writeString(this.coordinatesParce);

    }
}
