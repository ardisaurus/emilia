package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class PostCrypto {
    @SerializedName("status")
    String status;
    @SerializedName("result")
    Crypto mCrypto;
    @SerializedName("message")
    String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Crypto getmCrypto() {
        return mCrypto;
    }

    public void setmCoba(Crypto mCoba) {
        this.mCrypto = mCrypto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
