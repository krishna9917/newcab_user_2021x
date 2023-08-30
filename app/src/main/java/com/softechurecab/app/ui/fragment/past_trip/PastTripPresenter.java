package com.softechurecab.app.ui.fragment.past_trip;

import com.softechurecab.app.base.BasePresenter;
import com.softechurecab.app.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PastTripPresenter<V extends PastTripIView> extends BasePresenter<V> implements PastTripIPresenter<V> {

    @Override
    public void pastTrip() {

        getCompositeDisposable().add(APIClient.getAPIClient().pastTrip()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(pastTripResponse -> getMvpView().onSuccess(pastTripResponse),
                        throwable -> getMvpView().onError(throwable)));
    }
}
