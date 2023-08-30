package com.softechurecab.app.ui.activity.setting;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.AddressResponse;

public interface SettingsIView extends MvpView {

    void onSuccessAddress(Object object);

    void onLanguageChanged(Object object);

    void onSuccess(AddressResponse address);

    void onError(Throwable e);
}
