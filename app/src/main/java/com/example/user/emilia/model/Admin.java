package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class Admin {
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("dob")
    private String dob;
    @SerializedName("level")
    private String Level ;
    @SerializedName("active")
    private String active ;

    public Admin(){}

    public Admin(String email, String name, String dob, String level, String active) {
        this.email = email;
        this.name = name;
        this.dob = dob;
        Level = level;
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
