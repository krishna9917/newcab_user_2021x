package com.softechurecab.app.ui.activity.main;

import com.softechurecab.app.base.BasePresenter;
import com.softechurecab.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter<V extends MainIView> extends BasePresenter<V> implements MainIPresenter<V> {

    @Override
    public void getUserInfo() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .profile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }


    @Override
    public void payment(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .payment1(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(paymentResponse -> getMvpView().onSuccess(paymentResponse),
                        throwable -> getMvpView().onError(throwable)));
    }
    @Override
    public void checkStatus() {
        getCompositeDisposable().add(APIClient
                .getAPIClient2()
                .checkStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void logout(String id) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .logout(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccessLogout, getMvpView()::onError));
    }

    @Override
    public void getProviders(HashMap<String, Object> params) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .providers(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void getSavedAddress() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .address()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void getDirectionResult(String slat,String slang,String dlat, String  dlang) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getDirection(slat, slang, dlat, dlang)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onDestination, getMvpView()::onError));
    }

    @Override
    public void getNavigationSettings() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getSettings()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onSettingError));
    }

    @Override
    public void updateDestination(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .extendTrip(obj)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMvpView()::onDestinationSuccess, getMvpView()::onError));
    }

}
