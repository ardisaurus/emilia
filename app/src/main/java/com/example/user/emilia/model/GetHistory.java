package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetHistory {
    @SerializedName("message")
    private String message;
    @SerializedName("status")
    private String status;
    @SerializedName("result")
    private List<History> listHitory;

    public GetHistory(String message, String status, List<History> listHitory) {
        this.message = message;
        this.status = status;
        this.listHitory = listHitory;
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

    public List<History> getListHitory() {
        return listHitory;
    }

    public void setListHitory(List<History> listHitory) {
        this.listHitory = listHitory;
    }
}
