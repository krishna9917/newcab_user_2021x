package com.softechurecab.app.ui.activity.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softechurecab.app.ui.activity.add_card.AddCardActivity;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.data.SharedHelper;
import com.softechurecab.app.data.network.model.Card;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.softechurecab.app.MvpApplication.RIDE_REQUEST;
import static com.softechurecab.app.MvpApplication.isCard;
import static com.softechurecab.app.MvpApplication.isCash;

//TODO ALLAN - Alterações débito na máquina e voucher
import static com.softechurecab.app.MvpApplication.isDebitMachine;

import static com.softechurecab.app.common.Constants.PaymentMode.CASH;
import static com.softechurecab.app.common.Constants.PaymentMode.DEBIT_MACHINE;
import static com.softechurecab.app.common.Constants.PaymentMode.ONLINE;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.CARD_ID;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.CARD_LAST_FOUR;
import static com.softechurecab.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;
import static com.softechurecab.app.ui.fragment.invoice.InvoiceFragment.isInvoiceCashToCard;

public class PaymentActivity extends BaseActivity implements PaymentIView {

    public static final int PICK_PAYMENT_METHOD = 12;
    private static final int STRIPE_PAYMENT_REQUEST_CODE = 100;
    private static final int BRAINTREE_PAYMENT_REQUEST_CODE = 101;
    private static final int PAYTM_PAYMENT_REQUEST_CODE = 102;
    private static final int PAYUMONEY_PAYMENT_REQUEST_CODE = 103;

//    @BindView(R.id.add_card)
//    TextView addCard;
    @BindView(R.id.cash)
    TextView tvCash;
    @BindView(R.id.txtOnline)
    TextView txtOnline;
//    @BindView(R.id.cards_rv)
//    RecyclerView cardsRv;
//    @BindView(R.id.llCardContainer)
//    LinearLayout llCardContainer;
//    @BindView(R.id.llCashContainer)
//    LinearLayout llCashContainer;

    //TODO ALLAN - Alterações débito na máquina e voucher
//    @BindView(R.id.credit_card)
//    TextView credit_card;
//    @BindView(R.id.debit_machine)
//    TextView debit_machine;

//    @BindView(R.id.braintree)
//    TextView braintree;
//    @BindView(R.id.payumoney)
//    TextView payumoney;
//    @BindView(R.id.paytm)
//    TextView paytm;

    private List<Card> cardsList = new ArrayList<>();
    private PaymentPresenter<PaymentActivity> presenter = new PaymentPresenter<>();

    private static final String TAG = "PaymentActivity";

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setTitle(getString(R.string.payment));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            boolean hideCash = extras.getBoolean("hideCash", false);
            tvCash.setVisibility(hideCash ? View.GONE : View.VISIBLE);
        }

        //TODO ALLAN - Alterações débito na máquina e voucher
//        credit_card.setVisibility(isCard ? View.VISIBLE : View.GONE);
//        debit_machine.setVisibility(isDebitMachine ? View.VISIBLE : View.GONE);

//        payumoney.setVisibility(isPayumoney ? View.VISIBLE : View.GONE);
//        paytm.setVisibility(isPaytm ? View.VISIBLE : View.GONE);
//        braintree.setVisibility(isBraintree ? View.VISIBLE : View.GONE);

        getCardsDetails();

    }

    private void getCardsDetails() {
//        showLoading();

//        new Handler().postDelayed(() -> {
//            if (isCard) {
//                cardsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//                cardsRv.setItemAnimator(new DefaultItemAnimator());
//                presenter.card();
//                llCardContainer.setVisibility(View.VISIBLE);
//            } else {
//                try {
//                    hideLoading();
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//                llCardContainer.setVisibility(View.GONE);
//            }
//
//            if (isCash && !isInvoiceCashToCard)
//                llCashContainer.setVisibility(View.VISIBLE);
//            else llCashContainer.setVisibility(View.GONE);
//        }, 3000);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    //TODO ALLAN - Alterações débito na máquina e voucher
//    @OnClick({R.id.add_card, R.id.cash, R.id.braintree, R.id.paytm, R.id.payumoney})
//    @OnClick({R.id.add_card, R.id.cash, R.id.debit_machine, R.id.credit_card})
    @OnClick({R.id.txtOnline, R.id.cash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.add_card:
//            case R.id.credit_card:
//                Intent intent = new Intent(this, AddCardActivity.class);
//                intent.putExtra("stripe_token", SharedHelper.getKey(this, "stripe_publishable_key"));
//                startActivityForResult(intent, STRIPE_PAYMENT_REQUEST_CODE);
//                break;
            case R.id.cash:
                finishResult(CASH);
                break;
            case R.id.txtOnline:
                finishResult(ONLINE);
                break;
//            case R.id.debit_machine:
//                finishResult(DEBIT_MACHINE);
//                break;

//            case R.id.braintree:
////                finishResult(BRAINTREE);
//                Toast.makeText(this, getString(R.string.under_dev), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.payumoney:
////                finishResult(PAYUMONEY);
//                Toast.makeText(this, getString(R.string.under_dev), Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.paytm:
////                finishResult(PAYTM);
//                Toast.makeText(this, getString(R.string.under_dev), Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    public void deleteCard(@NonNull Card card) {
        new AlertDialog.Builder(this,R.style.AlertDialogCustom)
                .setMessage(getString(R.string.are_sure_you_want_to_delete))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.delete), (dialog, whichButton) -> presenter.deleteCard(card.getCardId()))
                .setNegativeButton(getString(R.string.no), null).show();
    }

    public void finishResult(String mode) {
        Intent intent = new Intent();
        RIDE_REQUEST.put(PAYMENT_MODE, mode);
        intent.putExtra("payment_mode", mode);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSuccess(Object card) {
        Toast.makeText(baseActivity(), R.string.card_deleted_successfully, Toast.LENGTH_SHORT).show();
        presenter.card();
    }

    @Override
    public void onSuccess(List<Card> cards) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
//        cardsList.clear();
//        cardsList.addAll(cards);
//        cardsRv.setAdapter(new CardAdapter(cardsList));
    }

    @Override
    public void onAddCardSuccess(Object cards) {
        Toast.makeText(PaymentActivity.this, R.string.card_added, Toast.LENGTH_SHORT).show();
        presenter.card();
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) if (requestCode == STRIPE_PAYMENT_REQUEST_CODE) {
            Log.d("_D", "onActivityResult: " + data.getStringExtra("stripetoken"));
            getCardsDetails();
            presenter.addCard(data.getStringExtra("stripetoken"));
        } else if (requestCode == PAYTM_PAYMENT_REQUEST_CODE) {
            Log.d("_D", "onActivityResult: " + data.getStringExtra("order_id"));
        } else if (requestCode == PAYUMONEY_PAYMENT_REQUEST_CODE) {
            Log.d("_D", "onActivityResult: " + data.getStringExtra("status"));
            //                presenter.addCard(data.getStringExtra("stripetoken"));
        }
    }

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

        private List<Card> list;

        CardAdapter(List<Card> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Card item = list.get(position);
            holder.card.setText(getString(R.string.card_ ,item.getLastFour()));
            if (item.getIsDefault() == 1) holder.ivDefaultCard.setVisibility(View.VISIBLE);
            else holder.ivDefaultCard.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private RelativeLayout itemView;
            private TextView card;
            private ImageView ivDefaultCard;

            MyViewHolder(View view) {
                super(view);
                itemView = view.findViewById(R.id.item_view);
                ivDefaultCard = view.findViewById(R.id.ivDefaultCard);
                card = view.findViewById(R.id.card);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                Card card = list.get(position);
                if (view.getId() == R.id.item_view) {
                    Intent intent = new Intent();
                    RIDE_REQUEST.put(PAYMENT_MODE, "CARD");
                    RIDE_REQUEST.put(CARD_ID, card.getCardId());
                    RIDE_REQUEST.put(CARD_LAST_FOUR, card.getLastFour());
                    intent.putExtra("payment_mode", "CARD");
                    intent.putExtra("card_id", card.getCardId());
                    intent.putExtra("card_last_four", card.getLastFour());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public boolean onLongClick(View v) {
                int position = getAdapterPosition();
                Card card = list.get(position);
                if (v.getId() == R.id.item_view) deleteCard(card);
                return true;
            }
        }
    }
}
