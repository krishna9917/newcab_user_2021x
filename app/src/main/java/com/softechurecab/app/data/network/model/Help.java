package com.softechurecab.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Help {
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("contact_email")
    @Expose
    private String contactEmail;

    @SerializedName("contact_link")
    @Expose
    private String contactLink;



    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactLink() {
        return contactLink;
    }

    public void setContactLink(String contactLink) {
        this.contactLink = contactLink;
    }
}

