package com.softechurecab.app.base;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softechurecab.app.R;
import com.softechurecab.app.common.Constants;
import com.softechurecab.app.data.SharedHelper;
import com.softechurecab.app.data.network.model.Name;

import java.util.Calendar;

import static com.softechurecab.app.MvpApplication.RIDE_REQUEST;
import static com.softechurecab.app.MvpApplication.isCard;
import static com.softechurecab.app.MvpApplication.isCash;
import static com.softechurecab.app.MvpApplication.isOnline;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.CARD_LAST_FOUR;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;

public abstract class BaseFragment extends Fragment implements MvpView {

    private View view;
    private BaseActivity mBaseActivity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            initView(view);
        }

        return view;
    }

    protected abstract int getLayoutId();

    protected abstract View initView(View view);

    @Override
    public FragmentActivity baseActivity() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        if (mBaseActivity != null) {
            mBaseActivity.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mBaseActivity != null) {
            mBaseActivity.hideLoading();
        }
    }

    protected void datePicker(DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(baseActivity(),R.style.AlertDialogCustom, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    protected void timePicker(TimePickerDialog.OnTimeSetListener timeSetListener) {
        Calendar myCalendar = Calendar.getInstance();
        TimePickerDialog mTimePicker = new TimePickerDialog(getContext(),R.style.AlertDialogCustom, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
        mTimePicker.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mBaseActivity = (BaseActivity) context;
        }
    }
    public String getNameResult(Name value){

        String language=  SharedHelper.getKey(getContext(),"lang_code","English");
        if (language.equals("English")){
            return value.getEn();

        }else if (language.equals("Russian")){
            return value.getRu();
        }else if (language.equals("Azerbaijan")){
            return value.getAz();
        }

        return value.getEn();
    }
    protected void initPayment(TextView paymentMode) {
        if (RIDE_REQUEST.containsKey(PAYMENT_MODE))
            switch (RIDE_REQUEST.get(PAYMENT_MODE).toString()) {
                case Constants.PaymentMode.CASH:
                    paymentMode.setText(getString(R.string.cash));
                    //    paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
                    break;

                //TODO ALLAN - Alterações débito na máquina e voucher
                case Constants.PaymentMode.DEBIT_MACHINE:
                    paymentMode.setText(getString(R.string.debit_machine));
                    //    paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
                    break;
                case Constants.PaymentMode.ONLINE:
                    paymentMode.setText(getString(R.string.online));
                    //    paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
                    break;
                case Constants.PaymentMode.CARD:
                    if (RIDE_REQUEST.containsKey(CARD_LAST_FOUR))
                        paymentMode.setText(getString(R.string.card_, RIDE_REQUEST.get("card_last_four")));
                    else paymentMode.setText(getString(R.string.add_card_));
                    //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_card, 0, 0, 0);
                    break;
                case Constants.PaymentMode.PAYPAL:
                    paymentMode.setText(getString(R.string.paypal));
                    //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_paypal, 0, 0, 0);
                    break;
                case Constants.PaymentMode.BRAINTREE:
                    paymentMode.setText(getString(R.string.braintree));
                    break;

                case Constants.PaymentMode.PAYTM:
                    paymentMode.setText(getString(R.string.paytm));
                    break;

                case Constants.PaymentMode.PAYUMONEY:
                    paymentMode.setText(getString(R.string.payumoney));
                    break;

                case Constants.PaymentMode.WALLET:
                    paymentMode.setText(getString(R.string.wallet));
                    break;
            }
        else {
            if (isCash) {
                RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.CASH);
                paymentMode.setText(getString(R.string.cash));
                //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
            } else if (isCard) {
                paymentMode.setText(R.string.add_card_);
                //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_card, 0, 0, 0);
                RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.CARD);
            }
            else if (isOnline) {
                paymentMode.setText(R.string.online);
                //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_card, 0, 0, 0);
                RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.ONLINE);
            }

        }
    }

    protected void onErrorBase(Throwable t) {
        if (mBaseActivity != null) {
            mBaseActivity.onErrorBase(t);
        }
    }

    protected void handleError(Throwable e) {
        try {
            try {
                hideLoading();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (mBaseActivity != null) {
            mBaseActivity.handleError(e);
        }
    }

    @Override
    public void onSuccessLogout(Object object) {
        if (mBaseActivity != null) {
            mBaseActivity.onSuccessLogout(object);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (mBaseActivity != null) {
            mBaseActivity.onError(throwable);
        }
    }

//    protected String getNewNumberFormat(double d) {
//        return BaseActivity.SharedHelper.getKey(this, "currency") + " " +(d);
//    }

}
