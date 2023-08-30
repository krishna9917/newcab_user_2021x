package com.softechurecab.app.ui.fragment.service_flow;

import com.softechurecab.app.base.BasePresenter;
import com.softechurecab.app.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ServiceFlowPresenter<V extends ServiceFlowIView> extends BasePresenter<V> implements ServiceFlowIPresenter<V> {

    @Override
    public void checkStatus() {
        getCompositeDisposable().add(APIClient
                .getAPIClient2()
                .checkStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

}
