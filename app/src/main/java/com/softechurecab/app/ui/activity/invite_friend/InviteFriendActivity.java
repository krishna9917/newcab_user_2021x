package com.softechurecab.app.ui.activity.invite_friend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softechurecab.app.MvpApplication;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.common.GlobalVariables;
import com.softechurecab.app.data.SharedHelper;
import com.softechurecab.app.data.network.model.ReferralsData;
import com.softechurecab.app.data.network.model.User;
import com.softechurecab.app.ui.adapter.ReferralsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendActivity extends BaseActivity implements InviteFriendIView {

    private static final String TAG = "InviteFriendActivity";
    @BindView(R.id.invite_friend)
    TextView invite_friend;
    @BindView(R.id.referral_code)
    TextView referral_code;
    @BindView(R.id.rvReferrals)
    RecyclerView rvReferrals;
    @BindView(R.id.referralTxt)
    TextView txtReferral;


    String inviteText="";


    private InviteFriendPresenter<InviteFriendActivity> inviteFriendPresenter = new InviteFriendPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        inviteFriendPresenter.attachView(this);

        if (!SharedHelper.getKey(this, "referral_code").equalsIgnoreCase("")) {
            updateUI(GlobalVariables.referrals);
        } else {
            //To get updated referral details
            inviteFriendPresenter.profile();
        }
    }

    private void updateUI(ArrayList<ReferralsData> referralsData) {
        referral_code.setText(SharedHelper.getKey(this, "referral_code"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, "referral_text"), Html.FROM_HTML_MODE_COMPACT));
            inviteText= String.valueOf(Html.fromHtml(SharedHelper.getKey(this, "invite_text"), Html.FROM_HTML_MODE_COMPACT));
        } else {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, "referral_text")));
            inviteText= String.valueOf(Html.fromHtml(SharedHelper.getKey(this, "invite_text")));
        }
       showReferrals(referralsData);
    }

    private void showReferrals(ArrayList<ReferralsData> referrals) {
        if(GlobalVariables.referrals.size()==0 && GlobalVariables.referrals==null)
        {
            txtReferral.setText(getString(R.string.referrals)+" : 0");
        }else
        {
            txtReferral.setText(getString(R.string.referrals));
            rvReferrals.setLayoutManager(new LinearLayoutManager(this));
            rvReferrals.setAdapter(new ReferralsAdapter(InviteFriendActivity.this,referrals));
        }
    }
    @Override
    public void onSuccess(User user) {
        SharedHelper.putKey(this, "referral_code", user.getReferral_unique_id());
        SharedHelper.putKey(this, "referral_count", user.getReferral_count());
        SharedHelper.putKey(this, "referral_text", user.getReferral_text());
        SharedHelper.putKey(this, "invite_text", user.getReferral_details());

        GlobalVariables.referrals= user.getReferrals();
        MvpApplication.showOTP = user.getRide_otp().equals("1");
        updateUI(user.getReferrals());
    }

    @Override
    public void onError(Throwable throwable) {
        handleError(throwable);
    }

    @SuppressLint("StringFormatInvalid")
    @OnClick({R.id.share})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.share:
                try {
                    String appName = getString(R.string.app_name);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, appName);
                    i.putExtra(Intent.EXTRA_TEXT, inviteText);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
