package com.softechurecab.app.ui.activity.passbook;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.WalletResponse;

public interface WalletHistoryIView extends MvpView {
    void onSuccess(WalletResponse response);

    void onError(Throwable e);
}
