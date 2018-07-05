package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class PostSecondaryDevice {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private SecondaryDevice mSecondaryDevice;

    public PostSecondaryDevice(String status, String message, SecondaryDevice mSecondaryDevice) {
        this.status = status;
        this.message = message;
        this.mSecondaryDevice = mSecondaryDevice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SecondaryDevice getmSecondaryDevice() {
        return mSecondaryDevice;
    }

    public void setmSecondaryDevice(SecondaryDevice mSecondaryDevice) {
        this.mSecondaryDevice = mSecondaryDevice;
    }
}
