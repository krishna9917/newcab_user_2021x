package com.softechurecab.app.ui.activity.upcoming_trip_detail;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.Datum;

import java.util.List;

public interface UpcomingTripDetailsIView extends MvpView {

    void onSuccess(List<Datum> upcomingTripDetails);

    void onError(Throwable e);
}
