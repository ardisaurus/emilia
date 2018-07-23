package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAdminDevice {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    private List<AdminDevice> listAdminDevice;

    public GetAdminDevice(String message, String status, List<AdminDevice> listAdminDevice) {
        this.message = message;
        this.status = status;
        this.listAdminDevice = listAdminDevice;
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

    public List<AdminDevice> getListAdminDevice() {
        return listAdminDevice;
    }

    public void setListAdminDevice(List<AdminDevice> listAdminDevice) {
        this.listAdminDevice = listAdminDevice;
    }
}
