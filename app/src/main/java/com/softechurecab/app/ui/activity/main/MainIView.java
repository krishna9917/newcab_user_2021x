package com.softechurecab.app.ui.activity.main;

import com.akexorcist.googledirection.model.Direction;
import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.AddressResponse;
import com.softechurecab.app.data.network.model.DataResponse;
import com.softechurecab.app.data.network.model.Message;
import com.softechurecab.app.data.network.model.Provider;
import com.softechurecab.app.data.network.model.SettingsResponse;
import com.softechurecab.app.data.network.model.User;

import java.util.List;

public interface MainIView extends MvpView {

    void onSuccess(User user);

    void onSuccess(DataResponse dataResponse);

    abstract void onDestinationSuccess(Object object);

    void onDestination(Direction object);

    void onSuccess(Message message);

    void onSuccessLogout(Object object);

    void onSuccess(AddressResponse response);

    void onSuccess(List<Provider> objects);

    void onError(Throwable e);

    void onSuccess(SettingsResponse response);

    void onSettingError(Throwable e);

}
