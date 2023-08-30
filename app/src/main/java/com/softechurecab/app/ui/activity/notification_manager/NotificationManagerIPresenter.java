package com.softechurecab.app.ui.activity.notification_manager;

import com.softechurecab.app.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
