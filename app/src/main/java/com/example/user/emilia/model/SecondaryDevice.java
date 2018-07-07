package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class SecondaryDevice {
    @SerializedName("dvc_id")
    private String dvc_id;
    @SerializedName("dvc_name")
    private String dvc_name;
    @SerializedName("dvc_status")
    private String dvc_status;
    @SerializedName("status")
    private String status;

    public SecondaryDevice(String dvc_id, String dvc_name, String dvc_status) {
        this.dvc_id = dvc_id;
        this.dvc_name = dvc_name;
        this.dvc_status = dvc_status;
        this.status = status;
    }

    public String getDvc_id() {
        return dvc_id;
    }

    public void setDvc_id(String dvc_id) {
        this.dvc_id = dvc_id;
    }

    public String getDvc_name() {
        return dvc_name;
    }

    public void setDvc_name(String dvc_name) {
        this.dvc_name = dvc_name;
    }

    public String getDvc_status() {
        return dvc_status;
    }

    public void setDvc_status(String dvc_status) {
        this.dvc_status = dvc_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
