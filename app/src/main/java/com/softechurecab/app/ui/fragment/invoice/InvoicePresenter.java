package com.softechurecab.app.ui.fragment.invoice;

import com.softechurecab.app.base.BasePresenter;
import com.softechurecab.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InvoicePresenter<V extends InvoiceIView> extends BasePresenter<V> implements InvoiceIPresenter<V> {

    @Override
    public void payment(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .payment(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        paymentResponse -> getMvpView().onSuccess(paymentResponse),
                        throwable -> getMvpView().onError(throwable)));
    }

    @Override
    public void updateRide(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .updateRequest(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        object -> getMvpView().onSuccess(object),
                        throwable -> getMvpView().onError(throwable)));

    }
}
