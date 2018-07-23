package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class AdminDevice {
    @SerializedName("dvc_id")
    private String dvc_id;

    public AdminDevice(String dvc_id) {
        this.dvc_id = dvc_id;
    }

    public String getDvc_id() {
        return dvc_id;
    }

    public void setDvc_id(String dvc_id) {
        this.dvc_id = dvc_id;
    }
}
