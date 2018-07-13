package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class History {
    @SerializedName("hst_user_name")
    String name;
    @SerializedName("hst_email")
    String email;
    @SerializedName("hst_time")
    String time;
    @SerializedName("hst_date")
    String date;
    @SerializedName("latest_access")
    String latest_access;

    public History(String name, String email, String time, String date, String latest_access) {
        this.name = name;
        this.email = email;
        this.time = time;
        this.date = date;
        this.latest_access = latest_access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatest_access() {
        return latest_access;
    }

    public void setLatest_access(String latest_access) {
        this.latest_access = latest_access;
    }
}
