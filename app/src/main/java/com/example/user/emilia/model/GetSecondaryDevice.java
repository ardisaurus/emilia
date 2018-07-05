package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSecondaryDevice {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    private List<SecondaryDevice> listSecondaryDevice;

    public GetSecondaryDevice(String message, String status, List<SecondaryDevice> listSecondaryDevice) {
        this.message = message;
        this.status = status;
        this.listSecondaryDevice = listSecondaryDevice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SecondaryDevice> getListSecondaryDevice() {
        return listSecondaryDevice;
    }

    public void setListSecondaryDevice(List<SecondaryDevice> listSecondaryDevice) {
        this.listSecondaryDevice = listSecondaryDevice;
    }
}
