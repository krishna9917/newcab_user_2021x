package com.softechurecab.app.ui.activity.past_trip_detail;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.Datum;

import java.util.List;

public interface PastTripDetailsIView extends MvpView {

    void onSuccess(List<Datum> pastTripDetails);

    void onError(Throwable e);
}
