package com.softechurecab.app.ui.activity.coupon;

import com.softechurecab.app.base.MvpPresenter;

public interface CouponIPresenter<V extends CouponIView> extends MvpPresenter<V> {
    void coupon();
}
