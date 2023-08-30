package com.softechurecab.app.ui.activity.notification_manager;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.NotificationManager;

import java.util.List;

public interface NotificationManagerIView extends MvpView {

    void onSuccess(List<NotificationManager> notificationManager);

    void onError(Throwable e);

}