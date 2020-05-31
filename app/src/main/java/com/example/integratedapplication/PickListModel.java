package com.example.integratedapplication;

public class PickListModel {

    private String  pickListID;
    private String  pickListName;
    private String pickListDate;

    public PickListModel(String pickListID, String pickListName, String pickListDate) {
        this.pickListID = pickListID;
        this.pickListName = pickListName;
        this.pickListDate = pickListDate;
    }

    public String getPickListID() {
        return pickListID;
    }

    public void setPickListID(String pickListID) {
        this.pickListID = pickListID;
    }

    public String getPickListName() {
        return pickListName;
    }

    public void setPickListName(String pickListName) {
        this.pickListName = pickListName;
    }

    public String getPickListDate() {
        return pickListDate;
    }

    public void setPickListDate(String pickListDate) {
        this.pickListDate = pickListDate;
    }
}
