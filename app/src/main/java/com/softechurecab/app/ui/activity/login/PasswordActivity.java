package com.softechurecab.app.ui.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.softechurecab.app.BuildConfig;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.data.SharedHelper;
import com.softechurecab.app.data.network.model.ForgotResponse;
import com.softechurecab.app.data.network.model.Token;
import com.softechurecab.app.ui.activity.forgot_password.ForgotPasswordActivity;
import com.softechurecab.app.ui.activity.main.MainActivity;
import com.softechurecab.app.ui.activity.otp.OtpActivity;
import com.softechurecab.app.ui.activity.register.RegisterActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordActivity extends BaseActivity implements LoginIView {

    public static String TAG = "";
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String email;
    private loginPresenter<PasswordActivity> presenter = new loginPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbar.setNavigationOnClickListener(v -> finish());

        presenter.attachView(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) email = extras.getString("email");
        //if (BuildConfig.DEBUG) password.setText("123456");
    }

    private void login() {
        try {
            if (password.getText().toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty()) {
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                return;
            }
            if (SharedHelper.getKey(PasswordActivity.this, "device_id").isEmpty()) {
                @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("DATA", "validate: device_id-->" + deviceId);
                SharedHelper.putKey(PasswordActivity.this, "device_id", deviceId);
            }
            if (SharedHelper.getKey(PasswordActivity.this, "device_token").isEmpty()) {
                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (s != null) {
                            SharedHelper.putKey(PasswordActivity.this, "device_token", s);
                            Log.d("DATA", "validate: device_token-->" + s);
                        }
                    }
                });
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("grant_type", "password");
            map.put("username", email);
            map.put("password", password.getText().toString());
            map.put("device_token", SharedHelper.getKey(this, "device_token", "No device"));
            map.put("device_id", SharedHelper.getKey(this, "device_id", "123"));
            map.put("device_type", BuildConfig.DEVICE_TYPE);

            showLoading();
            presenter.login(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.sign_up, R.id.forgot_password, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.forgot_password:
                showLoading();
                presenter.forgotPassword(email);
                break;
            case R.id.next:
                login();
                break;
        }
    }

    @Override
    public void onSuccess(Token token) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String accessToken = "Bearer " +
                token.getAccessToken();
        SharedHelper.putKey(this, "access_token", accessToken);
        //SharedHelper.putKey(this, "refresh_token", token.getRefreshToken());
        SharedHelper.putKey(this, "logged_in", true);
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onSuccess(ForgotResponse forgotResponse) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Toast.makeText(this, forgotResponse.getMessage(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.putExtra("email", forgotResponse.getEmail());
        intent.putExtra("otp", forgotResponse.getOtp());
        intent.putExtra("id", forgotResponse.getId());
        startActivity(intent);
    }

    @Override
    public void onError(Throwable e) {
        TAG = "PasswordActivity";
        handleError(e);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
