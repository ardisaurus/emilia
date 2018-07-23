package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class PostAdminDevice {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private AdminDevice mAdminDevice;

    public PostAdminDevice(String status, String message, AdminDevice mAdminDevice) {
        this.status = status;
        this.message = message;
        this.mAdminDevice = mAdminDevice;
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

    public AdminDevice getmAdminDevice() {
        return mAdminDevice;
    }

    public void setmAdminDevice(AdminDevice mAdminDevice) {
        this.mAdminDevice = mAdminDevice;
    }
}
