package com.softechurecab.app.ui.activity.invite_friend;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.User;

public interface InviteFriendIView extends MvpView {

    void onSuccess(User user);

    void onError(Throwable e);

}
