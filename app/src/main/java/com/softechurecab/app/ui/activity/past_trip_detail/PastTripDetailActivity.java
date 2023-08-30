package com.softechurecab.app.ui.activity.past_trip_detail;

import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softechurecab.app.R;
import com.softechurecab.app.base.BaseActivity;
import com.softechurecab.app.data.SharedHelper;
import com.softechurecab.app.data.network.model.Datum;
import com.softechurecab.app.data.network.model.Payment;
import com.softechurecab.app.data.network.model.Provider;
import com.softechurecab.app.data.network.model.Rating;
import com.softechurecab.app.ui.fragment.InvoiceDialogFragment;
import com.softechurecab.app.ui.fragment.dispute.DisputeCallBack;
import com.softechurecab.app.ui.fragment.dispute.DisputeFragment;
import com.softechurecab.app.ui.fragment.dispute_status.DisputeStatusFragment;
import com.softechurecab.app.ui.fragment.lost_item.LostFragment;
import com.softechurecab.app.ui.fragment.lost_item_status.LostItemStatusFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.softechurecab.app.MvpApplication.DATUM;

public class PastTripDetailActivity extends BaseActivity implements PastTripDetailsIView, DisputeCallBack {

    @BindView(R.id.static_map)
    ImageView staticMap;
    @BindView(R.id.avatar)
    CircleImageView avatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.s_address)
    TextView sAddress;
    @BindView(R.id.d_address)
    TextView dAddress;
    @BindView(R.id.text_req_type)
    TextView text_type;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.finished_at)
    TextView finishedAt;
    @BindView(R.id.finished_at_time)
    TextView finishedAtTime;
    @BindView(R.id.booking_id)
    TextView bookingId;
    @BindView(R.id.payment_mode)
    TextView paymentMode;
    @BindView(R.id.payable)
    TextView payable;
    @BindView(R.id.user_comment)
    TextView userComment;
    @BindView(R.id.view_receipt)
    Button viewReceipt;
    @BindView(R.id.payment_image)
    ImageView paymentImage;

    private boolean isDisputeCreated;
    private boolean isLostItemCreated;
    private Datum datum;

    private PastTripDetailsPresenter<PastTripDetailActivity> presenter = new PastTripDetailsPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_past_trip_detail;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        setTitle(getString(R.string.past_trip_details));

        if (DATUM != null) {
            showLoading();
            presenter.getPastTripDetails(DATUM.getId());
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof DisputeFragment) ((DisputeFragment) fragment).setCallBack(this);
    }

    @OnClick({R.id.view_receipt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_receipt:
                InvoiceDialogFragment fragment = new InvoiceDialogFragment();
                fragment.show(getSupportFragmentManager(), fragment.getTag());
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_help_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem disputeMenuItem = menu.findItem(R.id.action_dispute);
        MenuItem lostMenuItem = menu.findItem(R.id.action_lost_my_item);


        if (isDisputeCreated) disputeMenuItem.setTitle(getString(R.string.dispute_status));
        else disputeMenuItem.setTitle(getString(R.string.dispute));

        if (isLostItemCreated) lostMenuItem.setTitle(getString(R.string.lost_item_status));
        else lostMenuItem.setTitle(getString(R.string.lost_item));
        SpannableString spanString = new SpannableString(disputeMenuItem.getTitle().toString());
        SpannableString spanString2 = new SpannableString(lostMenuItem.getTitle().toString());

        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        spanString2.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString2.length(), 0); //fix the color to white

        disputeMenuItem.setTitle(spanString);
        lostMenuItem.setTitle(spanString2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_dispute:
                if (isDisputeCreated) {
                    DisputeStatusFragment disputeDetailFragment = DisputeStatusFragment.newInstance(datum);
                    disputeDetailFragment.show(getSupportFragmentManager(), disputeDetailFragment.getTag());
                } else {
                    DisputeFragment disputeFragment = new DisputeFragment();
                    disputeFragment.show(getSupportFragmentManager(), disputeFragment.getTag());
                }

                return true;

            case R.id.action_lost_my_item:
                if (isLostItemCreated) {
                    LostItemStatusFragment lostItemStatusFragment = LostItemStatusFragment.newInstance(datum);
                    lostItemStatusFragment.show(getSupportFragmentManager(), lostItemStatusFragment.getTag());
                } else {
                    LostFragment lostFragment = new LostFragment();
                    lostFragment.show(getSupportFragmentManager(), lostFragment.getTag());
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSuccess(List<Datum> pastTripDetails) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        datum = pastTripDetails.get(0);

        if (datum != null) {

            Rating providerRating = datum.getRating();
            bookingId.setText(datum.getBookingId());

            String strCurrentDate = datum.getFinishedAt();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            SimpleDateFormat timeFormat;
            Date newDate;
            try {
                newDate = format.parse(strCurrentDate);
                format = new SimpleDateFormat("dd MMM yyyy");
                timeFormat = new SimpleDateFormat("hh:mm a");
                String date = format.format(newDate);
                String time = timeFormat.format(newDate);
                finishedAt.setText(date);
                finishedAtTime.setText(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Glide.with(baseActivity())
                    .load(datum.getStaticMap())
                    .apply(RequestOptions.placeholderOf(R.drawable.backride)
                            .dontAnimate().error(R.drawable.backride))
                    .into(staticMap);
            sAddress.setText(datum.getSAddress());
            dAddress.setText(datum.getDAddress());
            text_type.setText(datum.getServicerequired());

            isDisputeCreated = datum.getDispute() != null;
            isLostItemCreated = datum.getLostitem() != null;

            Provider provider = datum.getProvider();
            if (provider != null) {
                Glide.with(baseActivity())
                        .load(provider.getAvatar())
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_user_placeholder)
                                .dontAnimate().error(R.drawable.ic_user_placeholder))
                        .into(avatar);
                name.setText(String.format("%s %s", provider.getFirstName(), provider.getLastName()));
            }

            if (providerRating != null) {
                userComment.setText(providerRating.getProviderComment());
                rating.setRating(providerRating.getProviderRating());
            }

            initPayment(datum.getPaymentMode(), paymentMode, paymentImage);

            Payment payment = datum.getPayment();
            if (payment != null)
                payable.setText(SharedHelper.getKey(this, "currency") + " " +(payment.getTotal()));
        }
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    public void onDisputeCreated() {
        presenter.getPastTripDetails(DATUM.getId());
    }
}
