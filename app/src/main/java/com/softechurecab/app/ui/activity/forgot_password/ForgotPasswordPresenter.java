package com.softechurecab.app.ui.activity.forgot_password;


import com.softechurecab.app.base.BasePresenter;
import com.softechurecab.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ForgotPasswordPresenter<V extends ForgotPasswordIView> extends BasePresenter<V> implements ForgotPasswordIPresenter<V> {


    @Override
    public void resetPassword(HashMap<String, Object> parms) {

        getCompositeDisposable().add(APIClient.getAPIClient().
                resetPassword(parms)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object -> getMvpView().onSuccess(object),
                        throwable -> getMvpView().onError(throwable)));
    }
}
