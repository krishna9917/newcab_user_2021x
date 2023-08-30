package com.softechurecab.app.ui.activity.resetPassword;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.OtpView;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.data.network.APIClient;
import com.softechurecab.app.data.network.model.OtpSend;
import com.softechurecab.app.ui.activity.PasswordFrogot.PasswordForgotActivity;
import com.softechurecab.app.ui.activity.otp.OtpActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.prefs.BackingStoreException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends BaseActivity {
    private ImageView imgBack;
    private TextView txtCounter;
    private TextView txtSubmitBtn;
    private OtpView otpView;
    private EditText edtNewPassword,edtConfirmPassword;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void initView() {
     imgBack=findViewById(R.id.imgBack);
     txtCounter=findViewById(R.id.txtCounter);
     txtSubmitBtn=findViewById(R.id.txtSubmitBtn);
     otpView=findViewById(R.id.editOtp);
     edtNewPassword=findViewById(R.id.edtNewPassword);
     edtConfirmPassword=findViewById(R.id.edtConfirmNewPassword);
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

         txtCounter.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startCounter();
                 ResendOtp();
             }
         });

         txtSubmitBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(otpView.getOTP().equals("") || otpView.getOTP().length()<4)
                 {
                     Toasty.error(ResetPasswordActivity.this,"Please enter valid OTP").show();
                 }
                 else if(edtNewPassword.getText().toString().equals("")) {
                     Toasty.error(ResetPasswordActivity.this, "Please enter new password").show();
                 }
                 else if(edtNewPassword.getText().toString().length()<6) {
                     Toasty.error(ResetPasswordActivity.this, "Password should have min 6 characters").show();
                 }
                 else if(!edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString()))
                 {
                     Toasty.error(ResetPasswordActivity.this,"Mismatch password").show();
                 }else
                 {
                     ResetPassword();
                 }
             }
         });
    }

    private void ResetPassword() {
        showLoading();
        if(isNetworkConnected())
        {
           APIClient.getAPIClient().resetPassword(getIntent().getStringExtra("mobile"),otpView.getOTP(),edtNewPassword.getText().toString(),edtConfirmPassword.getText().toString()).enqueue(new Callback<OtpSend>() {
               @Override
               public void onResponse(Call<OtpSend> call, Response<OtpSend> response) {
                   hideLoading();
                   if (response.code() == 200) {
                       if (response.body().getStatus()) {
                           Toasty.info(ResetPasswordActivity.this, response.body().getMessage()).show();
                           finish();
                       }else
                       {
                           Toasty.info(ResetPasswordActivity.this, response.body().getMessage()).show();
                       }
                   }
               }
               @Override
               public void onFailure(Call<OtpSend> call, Throwable t) {
                   Toast.makeText(ResetPasswordActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                   hideLoading();
               }
           });

        }else
        {
            hideLoading();
            Toast.makeText(ResetPasswordActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
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
    public void ResendOtp()
    {
        showLoading();
        if(isNetworkConnected()) {
            APIClient.getAPIClient().sendOtpForgotPass(getIntent().getStringExtra("mobile")).enqueue(new Callback<OtpSend>() {
                @Override
                public void onResponse(Call<OtpSend> call, Response<OtpSend> response) {
                    hideLoading();
                    if (response.code() == 200) {
                        if (response.body().getStatus()) {
                            Toasty.info(ResetPasswordActivity.this, "OTP Send Successfully").show();
                        } else {
                            Toasty.error(ResetPasswordActivity.this, response.body().getMessage()).show();
                        }
                    }

                }
                @Override
                public void onFailure(Call<OtpSend> call, Throwable t) {
                    hideLoading();
                    Toast.makeText(ResetPasswordActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            hideLoading();
            Toast.makeText(ResetPasswordActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}