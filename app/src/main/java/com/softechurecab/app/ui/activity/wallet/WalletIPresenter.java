package com.softechurecab.app.ui.activity.wallet;

import com.softechurecab.app.base.MvpPresenter;

import java.util.HashMap;

public interface WalletIPresenter<V extends WalletIView> extends MvpPresenter<V> {
    void addMoney(HashMap<String, Object> obj);

}
