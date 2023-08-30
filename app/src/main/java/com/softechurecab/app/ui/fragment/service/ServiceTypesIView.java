package com.softechurecab.app.ui.fragment.service;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.Service;

import java.util.List;

public interface ServiceTypesIView extends MvpView {

    void onSuccess(List<Service> serviceList);

    void onError(Throwable e);

    void onSuccess(Object object);
}
