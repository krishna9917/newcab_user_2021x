package com.softechurecab.app.ui.activity.wallet;

import com.softechurecab.app.base.BasePresenter;
import com.softechurecab.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WalletPresenter<V extends WalletIView> extends BasePresenter<V> implements WalletIPresenter<V> {

    @Override
    public void addMoney(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .addMoney(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    public void addMoneyRaz(String amount) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .addRazMoney(amount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::successs, getMvpView()::onError));
    }

}
