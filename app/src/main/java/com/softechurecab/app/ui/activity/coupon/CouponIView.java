package com.softechurecab.app.ui.activity.coupon;

import com.softechurecab.app.base.MvpView;
import com.softechurecab.app.data.network.model.PromoResponse;

public interface CouponIView extends MvpView {
    void onSuccess(PromoResponse object);

    void onError(Throwable e);
}
