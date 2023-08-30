package com.softechurecab.app.ui.activity.rental;

import com.softechurecab.app.base.MvpPresenter;

import java.util.HashMap;

public interface RentBookingIPresenter<V extends RentBookingIView> extends MvpPresenter<V> {
    void services();
    void sendRequest(HashMap<String, Object> parms);
    void estimateFare(HashMap<String, Object> parms);
}