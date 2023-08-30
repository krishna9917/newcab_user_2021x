package com.softechurecab.app.ui.activity.splash;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.CheckVersion;
import com.softechurecab.app.data.network.model.Service;
import com.softechurecab.app.data.network.model.User;

import java.util.List;

public interface SplashIView extends MvpView {

    void onSuccess(List<Service> serviceList);

    void onSuccess(User user);

    void onError(Throwable e);

    void onSuccess(CheckVersion checkVersion);
    void onLanguageChanged(Object object);
}
