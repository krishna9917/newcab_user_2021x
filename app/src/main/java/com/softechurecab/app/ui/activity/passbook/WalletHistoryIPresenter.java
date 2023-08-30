package com.softechurecab.app.ui.activity.passbook;

import com.softechurecab.app.base.MvpPresenter;

public interface WalletHistoryIPresenter<V extends WalletHistoryIView> extends MvpPresenter<V> {
    void wallet();
}
