package com.softechurecab.app.ui.activity.PasswordFrogot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.data.network.APIClient;
import com.softechurecab.app.data.network.model.OtpSend;
import com.softechurecab.app.data.network.model.OtpVerify;
import com.softechurecab.app.ui.activity.UserMobile.GetUserMobileActivity;
import com.softechurecab.app.ui.activity.otp.OtpActivity;
import com.softechurecab.app.ui.activity.resetPassword.ResetPasswordActivity;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordForgotActivity extends BaseActivity {

    private EditText edtPhoneNum;
    private ImageView imgNext;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_password_forgot;
    }

    @Override
    protected void initView() {
        edtPhoneNum=findViewById(R.id.edtPhoneNum);
        imgNext=findViewById(R.id.imgNext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPhoneNum.getText().toString().equalsIgnoreCase("")) {
                    Toasty.error(PasswordForgotActivity.this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show();
                } else if (edtPhoneNum.getText().toString().length() < 10) {
                    Toasty.error(PasswordForgotActivity.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                }else {
                    showLoading();
                    APIClient.getAPIClient().sendOtpForgotPass(edtPhoneNum.getText().toString()).enqueue(new Callback<OtpSend>() {
                        @Override
                        public void onResponse(Call<OtpSend> call, Response<OtpSend> response) {
                            hideLoading();
                            if(response.code()==200)
                            {
                               if(response.body().getStatus())
                               {
                                   Intent ii=new Intent(PasswordForgotActivity.this, ResetPasswordActivity.class);
                                   ii.putExtra("mobile",edtPhoneNum.getText().toString());
                                   startActivity(ii);
                                   finish();
                               }else {
                                   Toasty.error(PasswordForgotActivity.this,response.body().getMessage()).show();
                               }
                            }
                        }
                        @Override
                        public void onFailure(Call<OtpSend> call, Throwable t) {
                           hideLoading();
                           Toast.makeText(PasswordForgotActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}