package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetRegisteredDevice {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    private List<RegisteredDevice> listRegisteredDevice;

    public GetRegisteredDevice(String message, String status, List<RegisteredDevice> listRegisteredDevice) {
        this.message = message;
        this.status = status;
        this.listRegisteredDevice = listRegisteredDevice;
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

    public List<RegisteredDevice> getListRegisteredDevice() {
        return listRegisteredDevice;
    }

    public void setListRegisteredDevice(List<RegisteredDevice> listRegisteredDevice) {
        this.listRegisteredDevice = listRegisteredDevice;
    }
}
