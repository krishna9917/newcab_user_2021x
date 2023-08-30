package com.softechurecab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckVersion {

    @SerializedName("force_update")
    @Expose
    private Boolean forceUpdate;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("razorpay_key_id")
    @Expose
    private String razorpay_key_id;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getRazorpay_key_id() {
        return razorpay_key_id;
    }

    public void setRazorpay_key_id(String razorpay_key_id) {
        this.razorpay_key_id = razorpay_key_id;
    }
}
