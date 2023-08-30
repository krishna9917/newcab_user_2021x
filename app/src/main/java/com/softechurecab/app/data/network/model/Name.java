package com.softechurecab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name {
    @SerializedName("en")
    @Expose
    private String en;
    @SerializedName("az")
    @Expose
    private String az;
    @SerializedName("ru")
    @Expose
    private String ru;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getAz() {
        return az;
    }

    public void setAz(String az) {
        this.az = az;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }
}
