package com.softechurecab.app.ui.fragment.service_flow;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.DataResponse;

public interface ServiceFlowIView extends MvpView {
    void onSuccess(DataResponse dataResponse);
}
