package com.softechurecab.app.ui.activity.outstation;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.EstimateFare;
import com.softechurecab.app.ui.adapter.ServiceAdapterSingle;

public interface OutstationBookingIView extends MvpView {

    void onSuccess(ServiceAdapterSingle adapter);

    void onSuccessRequest(Object object);
    void onSuccess(EstimateFare estimateFare);
}
