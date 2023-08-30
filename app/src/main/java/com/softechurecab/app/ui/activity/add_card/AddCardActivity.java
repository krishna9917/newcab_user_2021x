package com.softechurecab.app.ui.activity.add_card;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.softechurecab.app.R;

public class AddCardActivity extends AppCompatActivity {

    private static final String TAG = "StripePaymentActivity";
    private CardInputWidget cardForm;

    private String stripe_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cardForm = findViewById(R.id.card_formm);
        stripe_token = getIntent().getStringExtra("stripe_token");

//        cardForm.cardRequired(true)
//                .expirationRequired(true)
//                .cvvRequired(true)
//                .postalCodeRequired(false)
//                .mobileNumberRequired(false)
//                .actionLabel(getString(R.string.add_card_details))
//                .setup(this);

        findViewById(R.id.submit).setOnClickListener(v -> onSubmit());

    }

    public void onSubmit() {

        if (cardForm.getCard().getNumber().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_card_number), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cardForm.getCard().validateExpiryDate()) {
            Toast.makeText(this, getString(R.string.please_enter_card_expiration_details), Toast.LENGTH_SHORT).show();
            return;
        }
        if (cardForm.getCard().getCVC().isEmpty()) {
            Toast.makeText(this, getString(R.string.please_enter_card_cvv), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isDigitsOnly(cardForm.getCard().getExpMonth().toString()) || !TextUtils.isDigitsOnly(cardForm.getCard().getExpYear().toString())) {
            Toast.makeText(this, getString(R.string.please_enter_card_expiration_details), Toast.LENGTH_SHORT).show();
            return;
        }

        String cardNumber = cardForm.getCard().getNumber();
        int cardMonth =  cardForm.getCard().getExpMonth();
        int cardYear = cardForm.getCard().getExpYear();
        String cardCvv = cardForm.getCard().getCVC();
        Log.d("CARD", "CardDetails Number: " + cardNumber + "Month: " + cardMonth + " Year: " + cardYear + " Cvv " + cardCvv);
        Card card = new Card(cardNumber, cardMonth, cardYear, cardCvv);
        card.setCurrency("usd");
        addCard(card);

    }

    private void addCard(Card card) {
        Stripe stripe = new Stripe(this, stripe_token);
        stripe.createToken(card, new TokenCallback() {
                    public void onSuccess(Token token) {
                        Log.d("CARD:", " " + token.getId());
                        Log.d("CARD:", " " + token.getCard().getLast4());
                        String stripeToken = token.getId();

                        Intent intent = new Intent();
                        intent.putExtra("stripetoken", stripeToken);
                        setResult(100, intent);
                        finish();
                    }

                    public void onError(Exception error) {
                        try {
                            Log.d(TAG, "onError: ");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
