package com.softechurecab.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.softechurecab.app.R;
import com.softechurecab.app.common.Constants;
import com.softechurecab.app.common.LocaleHelper;
import com.softechurecab.app.data.network.model.CheckVersion;
import com.softechurecab.app.data.network.model.Service;
import com.softechurecab.app.data.network.model.User;
import com.softechurecab.app.ui.activity.login.EmailActivity;
import com.softechurecab.app.ui.activity.register.RegisterActivity;
import com.softechurecab.app.ui.activity.splash.SplashIView;
import com.softechurecab.app.ui.activity.splash.SplashPresenter;
import com.yariksoffice.lingver.Lingver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivityNew extends AppCompatActivity implements SplashIView
{
    @BindView(R.id.tv_loginn)

    TextView login;
    @BindView(R.id.tv_registerry)
    TextView regsuter;

    @BindView(R.id.choose_language)
    RadioGroup chooseLanguage;
    @BindView(R.id.english)
    RadioButton english;
    @BindView(R.id.arabic)
    RadioButton arabic;
    private String language;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_welcomeactivity);
        ButterKnife.bind(this);
        presenter.attachView(this);

        chooseLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            showLoading();
            switch (checkedId) {
                case R.id.english:
                    language = Constants.Language.ENGLISH;
                    break;
                case R.id.french:
                    language = Constants.Language.FRENCH;
                    break;
                case R.id.arabic:
                    language = Constants.Language.ARABIC;
                    break;
            }
            Lingver.getInstance().setLocale(this, language);
            Intent intent = new Intent(this, WelcomeActivityNew.class);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK));
            this.overridePendingTransition(R.anim.rotate_in, R.anim.rotate_out);
            //LocaleHelper.setLocale(getApplicationContext(), language);
            findViewById(R.id.grplanguages).setVisibility(View.GONE);
            //presenter.changeLanguage(language);

        });

        findViewById(R.id.tv_languages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.grplanguages).setVisibility(View.VISIBLE);
            }
        });
    }
    private SplashPresenter<WelcomeActivityNew> presenter = new SplashPresenter<>();


    @OnClick({R.id.tv_loginn, R.id.tv_registerry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_loginn:
                startActivity(new Intent(this, EmailActivity.class));
                break;
            case R.id.tv_registerry:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

        }
    }

    @Override
    public void onSuccess(List<Service> serviceList) {

    }

    @Override
    public void onSuccess(User user) {

    }

    @Override
    public Activity baseActivity() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() throws Exception {

    }

    @Override
    public void onSuccessLogout(Object object) {

    }

    @Override
    public void onError(Throwable e) {

        Log.i("dsds",e.getLocalizedMessage());
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        findViewById(R.id.grplanguages).setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(CheckVersion checkVersion) {

    }

    @Override
    public void onLanguageChanged(Object object) {

        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        LocaleHelper.setLocale(getApplicationContext(), language);
        findViewById(R.id.grplanguages).setVisibility(View.GONE);
    }
}
