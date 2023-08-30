package com.softechurecab.app.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.softechurecab.app.R;


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.custom_dialog);
//        setCancelable(false);

        WindowManager.LayoutParams wlmp = getWindow().getAttributes();

        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        View view = LayoutInflater.from(context).inflate(R.layout.lottie, null);
        setContentView(view);
    }
}