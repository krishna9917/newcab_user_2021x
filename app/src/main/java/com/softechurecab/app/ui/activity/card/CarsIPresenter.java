package com.softechurecab.app.ui.activity.card;

import com.softechurecab.app.base.MvpPresenter;


public interface CarsIPresenter<V extends CardsIView> extends MvpPresenter<V> {
    void card();
}
