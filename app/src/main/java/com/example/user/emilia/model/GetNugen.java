package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNugen {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    private List<Nugen> listNugen;

    public GetNugen(String message, String status, List<Nugen> listNugen) {
        this.message = message;
        this.status = status;
        this.listNugen = listNugen;
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

    public List<Nugen> getListNugen() {
        return listNugen;
    }

    public void setListNugen(List<Nugen> listNugen) {
        this.listNugen = listNugen;
    }
}
