package com.softechurecab.app.ui.fragment.invoice;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.Message;

public interface InvoiceIView extends MvpView {
    void onSuccess(Message message);

    void onSuccess(Object o);

    void onSuccessPayment(Object o);

    void onError(Throwable e);

}
