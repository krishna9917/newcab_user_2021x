package com.softechurecab.app.ui.activity.location_pick;

import com.softechurecab.app.base.MvpPresenter;

public interface LocationPickIPresenter<V extends LocationPickIView> extends MvpPresenter<V> {
    void address();
}
