package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCrypto {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    List<Crypto> listDataCrypto;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Crypto> getListDataCrypto() {
        return listDataCrypto;
    }

    public void setListDataCrypto(List<Crypto> listDataCrypto) {
        this.listDataCrypto = listDataCrypto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
