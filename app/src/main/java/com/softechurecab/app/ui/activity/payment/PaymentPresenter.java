package com.softechurecab.app.ui.activity.payment;

import com.softechurecab.app.base.BasePresenter;
import com.softechurecab.app.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PaymentPresenter<V extends PaymentIView> extends BasePresenter<V> implements PaymentIPresenter<V> {

    @Override
    public void deleteCard(String cardId) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .deleteCard(cardId, "DELETE")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void card() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .card()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void addCard(String cardId) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .card(cardId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onAddCardSuccess, getMvpView()::onError));
    }
}
