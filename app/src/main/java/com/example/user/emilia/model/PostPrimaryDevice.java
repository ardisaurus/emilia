package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class PostPrimaryDevice {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private PrimaryDevice mPrimaryDevice;

    public PostPrimaryDevice(String status, String message, PrimaryDevice mPrimaryDevice) {
        this.status = status;
        this.message = message;
        this.mPrimaryDevice = mPrimaryDevice;
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

    public PrimaryDevice getmPrimaryDevice() {
        return mPrimaryDevice;
    }

    public void setmPrimaryDevice(PrimaryDevice mPrimaryDevice) {
        this.mPrimaryDevice = mPrimaryDevice;
    }
}
