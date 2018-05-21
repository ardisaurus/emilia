package com.example.user.emilia.model;

public class PrimaryDevice {
    private String dvc_id, dvc_name, dvc_status;

    public PrimaryDevice(String dvc_id, String dvc_name, String dvc_status) {
        this.dvc_id = dvc_id;
        this.dvc_name = dvc_name;
        this.dvc_status = dvc_status;
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
}
