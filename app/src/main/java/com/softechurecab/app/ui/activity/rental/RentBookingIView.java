package com.softechurecab.app.ui.activity.rental;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.EstimateFare;
import com.softechurecab.app.data.network.model.Service;

import java.util.List;

public interface RentBookingIView extends MvpView {

    void onSuccess(List<Service> services);

    void onSuccessRequest(Object object);

    void onSuccess(EstimateFare estimateFare);

}