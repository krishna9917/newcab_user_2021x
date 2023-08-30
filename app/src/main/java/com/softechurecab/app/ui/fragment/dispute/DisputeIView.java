package com.softechurecab.app.ui.fragment.dispute;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.DisputeResponse;
import com.softechurecab.app.data.network.model.Help;

import java.util.List;

public interface DisputeIView extends MvpView {

    void onSuccess(Object object);

    void onSuccessDispute(List<DisputeResponse> responseList);

    void onError(Throwable e);

    void onSuccess(Help help);
}
