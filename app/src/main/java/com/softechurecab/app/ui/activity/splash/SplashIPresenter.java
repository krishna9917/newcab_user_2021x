package com.softechurecab.app.ui.activity.splash;

import com.softechurecab.app.base.MvpPresenter;

import java.util.HashMap;

public interface SplashIPresenter<V extends SplashIView> extends MvpPresenter<V> {

    void services();

    void profile();

    void checkVersion(HashMap<String, Object> map);
    void changeLanguage(String languageID);
}
