package com.softechurecab.app.data.network.model;

public class OtpVerify {
    private Boolean status;
    private String message;
    private int signup;

    public VerifyMobileUser getUser() {
        return user;
    }

    public void setUser(VerifyMobileUser user) {
        this.user = user;
    }

    private VerifyMobileUser user;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSignup() {
        return signup;
    }

    public void setSignup(int signup) {
        this.signup = signup;
    }


}
