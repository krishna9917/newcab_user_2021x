package com.softechurecab.app.ui.activity.wallet;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.AddWallet;

public interface WalletIView extends MvpView {
    void onSuccess(AddWallet object);

    void successs(AddWallet object);
    void onError(Throwable e);
}
