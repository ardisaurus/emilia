package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class RegisteredDevice {
    @SerializedName("dvc_id")
    String dvc_id;
    @SerializedName("email")
    String email;

    public RegisteredDevice(String dvc_id, String email) {
        this.dvc_id = dvc_id;
        this.email = email;
    }

    public String getDvc_id() {
        return dvc_id;
    }

    public void setDvc_id(String dvc_id) {
        this.dvc_id = dvc_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
