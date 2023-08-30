package com.softechurecab.app.ui.activity.wallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.common.Constants;
import com.softechurecab.app.data.SharedHelper;
import com.softechurecab.app.data.network.model.AddWallet;
import com.softechurecab.app.data.network.model.User;
import com.softechurecab.app.ui.activity.payment.PaymentActivity;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.softechurecab.app.MvpApplication.isBraintree;
import static com.softechurecab.app.MvpApplication.isCard;
import static com.softechurecab.app.MvpApplication.isPaytm;
import static com.softechurecab.app.MvpApplication.isPayumoney;
import static com.softechurecab.app.data.SharedHelper.getKey;
import static com.softechurecab.app.ui.activity.payment.PaymentActivity.PICK_PAYMENT_METHOD;

public class WalletActivity extends BaseActivity implements WalletIView {

    private static final String TAG = "WalletActivity";
    @BindView(R.id.wallet_balance)
    TextView walletBalance;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id._199)
    Button _199;
    @BindView(R.id._599)
    Button _599;
    @BindView(R.id._1099)
    Button _1099;
    @BindView(R.id.add_amount)
    Button addAmount;
    @BindView(R.id.cvAddMoneyContainer)
    CardView cvAddMoneyContainer;
    String regexNumber = "^(\\d{0,9}\\.\\d{1,4}|\\d{1,9})$";
    private WalletPresenter<WalletActivity> presenter = new WalletPresenter<>();

    private static final int BRAINTREE_PAYMENT_REQUEST_CODE = 101;


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {

        ButterKnife.bind(this);
        presenter.attachView(this);
        // Activity title will be updated after the locale has changed in Runtime
        setTitle(getString(R.string.wallet));

        _199.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._199));
        _599.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._599));
        _1099.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._1099));
        amount.setTag(SharedHelper.getKey(this, "currency"));
        Double d = Double.parseDouble(SharedHelper.getKey(this, "walletBalance", "0"));
        int i = d.intValue();
        walletBalance.setText(SharedHelper.getKey(this, "currency") + " " +(i));

        if (!isCard && !isBraintree && !isPaytm && !isPayumoney && !BaseActivity.online ) {
            cvAddMoneyContainer.setVisibility(View.GONE);
            addAmount.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id._199, R.id._599, R.id._1099, R.id.add_amount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id._199:
                amount.setText(getString(R.string._199));
                break;
            case R.id._599:
                amount.setText(getString(R.string._599));
                break;
            case R.id._1099:
                amount.setText(getString(R.string._1099));
                break;
            case R.id.add_amount:
                if (!amount.getText().toString().trim().matches(regexNumber)) {
                    Toast.makeText(baseActivity(), getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(BaseActivity.online){
                    startPaymentRaz();
                }else{
                    Intent intent = new Intent(baseActivity(), PaymentActivity.class);
                    intent.putExtra("hideCash", true);
                    startActivityForResult(intent, PICK_PAYMENT_METHOD);
                }
                break;
        }
    }


    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            addMoney2();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    private void addMoney2() {

        showLoading();
        presenter.addMoneyRaz(amount.getText().toString());
    }

    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK && data != null)
            switch (data.getStringExtra("payment_mode")) {
                case Constants.PaymentMode.CARD:
                    HashMap<String, Object> map = new HashMap<>();
                    String cardId = data.getStringExtra("card_id");
                    map.put("amount", amount.getText().toString());
                    map.put("payment_mode", "CARD");
                    map.put("card_id", cardId);
                    map.put("user_type", "app");
                    showLoading();
                    presenter.addMoney(map);
                    break;
            }
    }


    String name;
    String email;
    String phone;
    public void startPaymentRaz() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setKeyID(SharedHelper.getKey(WalletActivity.this, Constants.SharedPref.razorpay_key_id));
        try {
            JSONObject options = new JSONObject();

            User user = new Gson().fromJson(SharedHelper.getKey(this, "userInfo"), User.class);
            if (user != null) {
                name=user.getFirstName();
                email=user.getEmail();
                phone=user.getMobile();
            }

            options.put("name", name);
            options.put("description", "Wallet ReCharges");
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", Double.parseDouble(amount.getText().toString())*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact",phone);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void successs(AddWallet object) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if(object.getMessage().equals("Transaction Failed")){
            Toast.makeText(this, "\n" +
                    "Card failed or insufficient balance! Please try again.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, object.getMessage(), Toast.LENGTH_LONG).show();
        }

        amount.setText("");

        SharedHelper.putKey(this, "walletBalance", String.valueOf(object.getBalance()));
        walletBalance.setText(getKey(this, "currency") + " " +(Double.parseDouble(String.valueOf(object.getBalance()))));


    }

    @Override
    public void onSuccess(AddWallet wallet) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if(wallet.getMessage().equals("Transaction Failed")){
            Toast.makeText(this, "\n" +
                    "Card failed or insufficient balance! Please try again.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, wallet.getMessage(), Toast.LENGTH_LONG).show();
        }

        amount.setText("");

//        SharedHelper.putKey(this, "walletBalance", String.valueOf(wallet.getBalance()));
        Double d = Double.parseDouble(SharedHelper.getKey(this, "walletBalance", "0"));
        int i = d.intValue();
        walletBalance.setText(SharedHelper.getKey(this, "currency") + " " + (i));
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

}
