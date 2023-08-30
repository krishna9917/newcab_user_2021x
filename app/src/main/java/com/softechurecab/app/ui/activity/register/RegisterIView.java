package com.softechurecab.app.ui.activity.register;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.PhoneOtpResponse;
import com.softechurecab.app.data.network.model.RegisterResponse;
import com.softechurecab.app.data.network.model.SettingsResponse;

public interface RegisterIView extends MvpView {

    void onSuccess(SettingsResponse response);

    void onSuccess(RegisterResponse object);

    void onOtpSuccess(PhoneOtpResponse object);

    void onSuccess(Object object);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);

    void onError(Throwable e);

    void onVerifyEmailError(Throwable e);
}
