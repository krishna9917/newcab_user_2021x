package com.softechurecab.app.ui.activity.otp;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mukesh.OtpView;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.data.SharedHelper;
import com.softechurecab.app.data.network.APIClient;
import com.softechurecab.app.data.network.model.OtpSend;
import com.softechurecab.app.data.network.model.OtpVerify;
import com.softechurecab.app.data.network.model.VerifyMobileUser;
import com.softechurecab.app.ui.activity.main.MainActivity;
import com.softechurecab.app.ui.activity.register.RegisterActivity;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import com.softechurecab.app.BuildConfig;
import retrofit2.Response;
public class OtpActivity extends BaseActivity {
    private TextView txtMobile,txtCounter,txtSubmitBtn;
    private OtpView otpView;
    private ImageView imgBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_otp;
    }

    @Override
    public void initView() {
        txtMobile=findViewById(R.id.txtMobile);
        txtCounter=findViewById(R.id.txtCounter);
        txtSubmitBtn=findViewById(R.id.txtSubmitBtn);
        otpView=findViewById(R.id.editOtp);
        imgBack=findViewById(R.id.imgBack);
        txtMobile.setText(getIntent().getStringExtra("mobile"));
        startCounter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                if(otpView.getOTP().equals("") || otpView.getOTP().length()<4)
                {
                    Toasty.error(OtpActivity.this,"Please enter valid OTP").show();
                    hideLoading();
                }
                else
                {
                    if(isNetworkConnected())
                    {
                        Call<OtpVerify> call;
                        if(getIntent().getStringExtra("from").equals("userMobileActivity"))
                        {
                            if (SharedHelper.getKey(OtpActivity.this, "device_id").isEmpty()) {
                                @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                Log.d("DATA", "validate: device_id-->" + deviceId);
                                SharedHelper.putKey(OtpActivity.this, "device_id", deviceId);
                            }
                            if (SharedHelper.getKey(OtpActivity.this, "device_token").isEmpty()) {
                                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        if (s != null) {
                                            SharedHelper.putKey(OtpActivity.this, "device_token", s);
                                            Log.d("DATA", "validate: device_token-->" + s);
                                        }
                                    }
                                });
                            }
                            call= APIClient.getAPIClient().verifyOtp(getIntent().getStringExtra("mobile"),otpView.getOTP(), SharedHelper.getKey(OtpActivity.this, "device_token"),SharedHelper.getKey(OtpActivity.this, "device_id"), BuildConfig.DEVICE_TYPE);
                            callOtpVerifyApi(call);
                        }
                   }else {
                        Toast.makeText(OtpActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        txtCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCounter();
                resendOTP();
            }
        });
    }

    private void resendOTP() {
        showLoading();
        APIClient.getAPIClient().sendOtp(getIntent().getStringExtra("mobile")).enqueue(new Callback<OtpSend>() {
            @Override
            public void onResponse(retrofit2.Call<OtpSend> call, Response<OtpSend> response) {
                hideLoading();
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        Toasty.info(OtpActivity.this, "Sent OTP").show();
                    }
                }
            }
            @Override
            public void onFailure(retrofit2.Call<OtpSend> call, Throwable t) {
                hideLoading();
                Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callOtpVerifyApi(Call<OtpVerify> call) {
        try {
        call.enqueue(new Callback<OtpVerify>() {
            @Override
            public void onResponse(Call<OtpVerify> call, Response<OtpVerify> response) {
                hideLoading();
                if (response.code() == 200) {
                    if (response.body().getStatus() && response.body().getSignup() == 0) {
                        VerifyMobileUser user=response.body().getUser();
                        login(user);
                    } else if (response.body().getStatus() && response.body().getSignup() == 1) {
                        Intent ii = new Intent(OtpActivity.this, RegisterActivity.class);
                        ii.putExtra("from","OtpActivity");
                        ii.putExtra("mobile",getIntent().getStringExtra("mobile"));
                        startActivity(ii);
                        finish();
                    } else {
                        hideLoading();
                        Toasty.error(OtpActivity.this, response.body().getMessage()).show();
                    }
                }
            }



            @Override
            public void onFailure(Call<OtpVerify> call, Throwable t) {
                hideLoading();
                Toast.makeText(OtpActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCounter() {
        txtCounter.setEnabled(false);
        txtCounter.setTextColor(getColor(R.color.quantum_black_text));
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long sec = (millisUntilFinished / 1000) % 60;
                txtCounter.setText(f.format(sec));
            }
            public void onFinish() {
                txtCounter.setText(R.string.resend);
                txtCounter.setTextColor(getColor(R.color.colorPrimaryDark));
                txtCounter.setEnabled(true);
            }
        }.start();
    }
    public void login(VerifyMobileUser user)
    {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Log.d("dsfj2","sdfsdf " +  "ghkfghl");
        String accessToken = "Bearer " +user.getAccess_token();
        SharedHelper.putKey(this, "access_token", accessToken);
        //SharedHelper.putKey(this, "refresh_token", token.getRefreshToken());
        SharedHelper.putKey(this, "logged_in", true);
        Intent ii = new Intent(OtpActivity.this, MainActivity.class);
        startActivity(ii);
        finishAffinity();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}