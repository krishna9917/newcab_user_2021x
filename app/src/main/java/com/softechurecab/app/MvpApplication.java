package com.softechurecab.app;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.softechurecab.app.common.ConnectivityReceiver;
import com.softechurecab.app.common.LocaleHelper;
import com.softechurecab.app.data.network.model.Datum;
import com.yariksoffice.lingver.Lingver;

import java.util.HashMap;


public class MvpApplication extends Application {

    public static String marker;
    private static MvpApplication mInstance;

    public static boolean canGoToChatScreen;
    public static boolean isChatScreenOpen;

    public static boolean isCash = true;
    public static boolean isOnline ;
    public static boolean isCard;
    public static boolean isPayumoney;
    public static boolean isPaypal;
    public static boolean isPaytm;
    public static boolean isPaypalAdaptive;
    public static boolean isBraintree;
    public static boolean openChatFromNotification = true;

    //TODO ALLAN - Alterações débito na máquina e voucher
    public static boolean isDebitMachine;
    public static boolean isVoucher;

    public static HashMap<String, Object> RIDE_REQUEST = new HashMap<>();
    public static Datum DATUM = null;
    public static final int PICK_LOCATION_REQUEST_CODE = 3;
    public static boolean showOTP = true;
    public static int cancelCharge = 0;

    public static boolean isCCAvenueEnabled = false;

    public static synchronized MvpApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this);
        Lingver.init(this, "en");
        MultiDex.install(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
        MultiDex.install(newBase);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
