package com.softechurecab.app.ui.activity.change_password;


import com.softechurecab.app.base.MvpPresenter;

import java.util.HashMap;


public interface ChangePasswordIPresenter<V extends ChangePasswordIView> extends MvpPresenter<V> {
    void changePassword(HashMap<String, Object> parms);
}
