package com.softechurecab.app.ui.activity.social;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.Token;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface SocialIView extends MvpView {
    void onSuccess(Token token);

    void onError(Throwable e);
}
