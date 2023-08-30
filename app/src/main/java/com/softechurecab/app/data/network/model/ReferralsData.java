package com.softechurecab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferralsData {

    @SerializedName("full_name")
    @Expose
    private String full_name;

    @SerializedName("date")
    @Expose
    private String date;


    @SerializedName("amount")
    @Expose
    private String amount;


    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
