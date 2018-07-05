package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPrimaryDevice {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    private List<PrimaryDevice> listPrimaryDevice;

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

    public List<PrimaryDevice> getListPrimaryDevice() {
        return listPrimaryDevice;
    }

    public void setListPrimaryDevice(List<PrimaryDevice> listPrimaryDevice) {
        this.listPrimaryDevice = listPrimaryDevice;
    }
}
