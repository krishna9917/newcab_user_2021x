package com.softechurecab.app.ui.activity.payment;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.Card;

import java.util.List;

public interface PaymentIView extends MvpView {

    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onAddCardSuccess(Object cards);

    void onError(Throwable e);


}
