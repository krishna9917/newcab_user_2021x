package com.softechurecab.app.ui.activity.login;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.ForgotResponse;
import com.softechurecab.app.data.network.model.Token;

public interface LoginIView extends MvpView {
    void onSuccess(Token token);

    void onSuccess(ForgotResponse object);

    void onError(Throwable e);
}
