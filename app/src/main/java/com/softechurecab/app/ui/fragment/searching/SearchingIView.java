package com.softechurecab.app.ui.fragment.searching;

import com.softechurecab.app.base.MvpView;

public interface SearchingIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
