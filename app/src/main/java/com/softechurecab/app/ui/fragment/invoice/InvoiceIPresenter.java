package com.softechurecab.app.ui.fragment.invoice;

import com.softechurecab.app.base.MvpPresenter;

import java.util.HashMap;

public interface InvoiceIPresenter<V extends InvoiceIView> extends MvpPresenter<V> {
    void payment(HashMap<String, Object> obj);

    void updateRide(HashMap<String, Object> obj);


}
