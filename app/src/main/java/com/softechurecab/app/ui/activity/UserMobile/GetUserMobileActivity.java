package com.softechurecab.app.ui.activity.UserMobile;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.data.network.APIClient;
import com.softechurecab.app.data.network.model.OtpSend;
import com.softechurecab.app.ui.activity.login.EmailActivity;
import com.softechurecab.app.ui.activity.otp.OtpActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserMobileActivity extends BaseActivity {
    private ImageView imgNextBtn;
    private EditText edtUserMobile;
    private TextView txtPasswordLogin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_get_user_mobile;
    }

    @Override
    public void initView() {
        imgNextBtn = findViewById(R.id.imgNext);
        edtUserMobile = findViewById(R.id.edtPhoneNum);
        txtPasswordLogin=findViewById(R.id.txtPasswordLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      txtPasswordLogin.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(GetUserMobileActivity.this, EmailActivity.class));
          }
      });

        imgNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUserMobile.getText().toString().equalsIgnoreCase("")) {
                    Toasty.error(GetUserMobileActivity.this, getString(R.string.invalid_phone_number), Toast.LENGTH_SHORT).show();
                } else if (edtUserMobile.getText().toString().length() < 10) {
                    Toasty.error(GetUserMobileActivity.this, "Please enter valid phone number", Toast.LENGTH_SHORT).show();
                } else {
                    showLoading();
                   if (isNetworkConnected()) {
                        APIClient.getAPIClient().sendOtp(edtUserMobile.getText().toString()).enqueue(new Callback<OtpSend>() {
                            @Override
                            public void onResponse(retrofit2.Call<OtpSend> call, Response<OtpSend> response) {
                               hideLoading();
                                if (response.code() == 200) {
                                    if (response.body().getStatus()) {
                                        Toasty.info(GetUserMobileActivity.this, "OTP send to given Number").show();
                                        Intent intent = new Intent(GetUserMobileActivity.this, OtpActivity.class);
                                        intent.putExtra("mobile", edtUserMobile.getText().toString());
                                        intent.putExtra("from", "userMobileActivity");
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<OtpSend> call, Throwable t) {
                                hideLoading();
                                Toast.makeText(GetUserMobileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                       hideLoading();
                       Toast.makeText(GetUserMobileActivity.this,getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}