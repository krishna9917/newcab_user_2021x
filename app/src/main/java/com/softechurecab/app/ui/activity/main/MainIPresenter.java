package com.softechurecab.app.ui.activity.main;

import com.softechurecab.app.base.MvpPresenter;

import java.util.HashMap;

public interface MainIPresenter<V extends MainIView> extends MvpPresenter<V> {

    void getUserInfo();

    void logout(String id);

    void checkStatus();

    void payment(HashMap<String, Object> obj);

    void getSavedAddress();

    void getProviders(HashMap<String, Object> params);


    void getDirectionResult(String slat, String slang, String dlat, String dlang);

    void getNavigationSettings();

    void updateDestination(HashMap<String, Object> obj);

}
