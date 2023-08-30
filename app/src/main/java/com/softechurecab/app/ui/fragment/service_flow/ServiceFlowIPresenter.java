package com.softechurecab.app.ui.fragment.service_flow;

import com.softechurecab.app.base.MvpPresenter;

public interface ServiceFlowIPresenter<V extends ServiceFlowIView> extends MvpPresenter<V> {
    void checkStatus();
}
