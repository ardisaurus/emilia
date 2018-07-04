package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class Nugen {
    @SerializedName("dvc_id")
    String dvc_id;

    public Nugen(String dvc_id) {
        this.dvc_id = dvc_id;
    }

    public String getDvc_id() {
        return dvc_id;
    }

    public void setDvc_id(String dvc_id) {
        this.dvc_id = dvc_id;
    }
}
