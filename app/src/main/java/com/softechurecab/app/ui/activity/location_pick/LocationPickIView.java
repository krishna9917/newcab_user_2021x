package com.softechurecab.app.ui.activity.location_pick;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.AddressResponse;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface LocationPickIView extends MvpView {

    void onSuccess(AddressResponse address);

    void onError(Throwable e);
}
