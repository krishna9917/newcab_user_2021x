package com.softechurecab.app.ui.activity.past_trip_detail;

import com.softechurecab.app.base.MvpPresenter;

public interface PastTripDetailsIPresenter<V extends PastTripDetailsIView> extends MvpPresenter<V> {

    void getPastTripDetails(Integer requestId);
}
