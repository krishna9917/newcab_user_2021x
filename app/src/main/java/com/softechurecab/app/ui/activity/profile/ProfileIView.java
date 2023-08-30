package com.softechurecab.app.ui.activity.profile;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.User;

public interface ProfileIView extends MvpView {

    void onSuccess(User user);

    void onUpdateSuccess(User user);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);
}
