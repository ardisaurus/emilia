package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAdmin {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    private List<Admin> listDataAdmin;

    public GetAdmin(String message, String status, List<Admin> listDataAdmin) {
        this.message = message;
        this.status = status;
        this.listDataAdmin = listDataAdmin;
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

    public List<Admin> getListDataAdmin() {
        return listDataAdmin;
    }

    public void setListDataAdmin(List<Admin> listDataAdmin) {
        this.listDataAdmin = listDataAdmin;
    }
}
