package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class PostAdmin {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private Admin mAdmin;

    public PostAdmin(String status, String message, Admin mAdmin) {
        this.status = status;
        this.message = message;
        this.mAdmin = mAdmin;
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

    public Admin getmAdmin() {
        return mAdmin;
    }

    public void setmAdmin(Admin mAdmin) {
        this.mAdmin = mAdmin;
    }
}
