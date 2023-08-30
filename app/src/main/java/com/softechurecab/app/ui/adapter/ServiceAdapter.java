package com.softechurecab.app.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.softechurecab.app.BuildConfig;
import com.softechurecab.app.R;
import com.softechurecab.app.common.Constants;
import com.softechurecab.app.data.SharedHelper;
import com.softechurecab.app.data.network.model.EstimateFare;
import com.softechurecab.app.data.network.model.Name;
import com.softechurecab.app.data.network.model.Service;
import com.softechurecab.app.ui.fragment.RateCardFragment;
import com.softechurecab.app.ui.fragment.service.ServiceTypesFragment;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private List<Service> list;
    private TextView capacity;
    private Context context;
    private int lastCheckedPos = 0;
    private Animation zoomIn;
    private ServiceTypesFragment.ServiceListener mListener;
    private EstimateFare estimateFare;
    private boolean canNotifyDataSetChanged = true;

    public ServiceAdapter(Context context, List<Service> list,
                          ServiceTypesFragment.ServiceListener listener,
                          TextView capacity, EstimateFare fare) {
        this.context = context;
        this.list = list;
        this.capacity = capacity;
        this.mListener = listener;
        this.estimateFare = fare;
        zoomIn = AnimationUtils.loadAnimation(this.context, R.anim.zoom_in_animation);
        zoomIn.setFillAfter(true);
    }

    public void setEstimateFare(EstimateFare estimateFare) {
        this.estimateFare = estimateFare;
        canNotifyDataSetChanged = true;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_service, parent, false));
    }

    @SuppressLint({"StringFormatMatches", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        Service obj = list.get(position);
        if (obj != null)
            holder.serviceName.setText(getNameResult(obj.getName()));
        if (estimateFare != null) {
            holder.estimated_fixed.setVisibility(View.VISIBLE);
            holder.price.setVisibility(View.VISIBLE);
            holder.estimated_fixed.setText(SharedHelper.getKey(context, "currency")+""+Double.parseDouble(String.valueOf(estimateFare.getEstimatedFare())));
            if (SharedHelper.getKey(context, "measurementType").equalsIgnoreCase(Constants.MeasurementType.KM)) {
                if (estimateFare.getDistance() > 1 || estimateFare.getDistance() > 1.0) {
                    holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.kms));
                } else {
                    holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.km));
                }
            } else {
                if (estimateFare.getDistance() > 1 || estimateFare.getDistance() > 1.0) {
                    holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.miles));
                } else {
                    holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.mile));
                }
            }
        }
        Glide.with(context)
                .load(BuildConfig.BASE_IMAGE_URL+obj.getImage())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_car).dontAnimate().error(R.drawable.ic_car))
                .into(holder.image);

        if (position == lastCheckedPos && canNotifyDataSetChanged) {
            canNotifyDataSetChanged = true;
            if(capacity!=null){
            capacity.setText(String.valueOf(obj.getCapacity()));
            }
            holder.mFrame_service.setBackground(context.getResources().getDrawable(R.drawable.yellowstrokegerysolid));
            holder.serviceName.setTextColor(context.getResources().getColor(R.color.colorAccent));

            holder.itemView.setAlpha(1);

            if(capacity!=null){
            holder.estimated_fixed.setVisibility(View.VISIBLE);
                holder.price.setVisibility(View.VISIBLE);
            }
            if (estimateFare != null) {
                if (SharedHelper.getKey(context, "measurementType").equalsIgnoreCase(Constants.MeasurementType.KM)) {
                    if (estimateFare.getDistance() > 1 || estimateFare.getDistance() > 1.0)
                        holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.kms));
                    else
                        holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.km));
                } else {
                    if (estimateFare.getDistance() > 1 || estimateFare.getDistance() > 1.0)
                        holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.miles));
                    else
                        holder.price.setText(estimateFare.getDistance() + " " + context.getString(R.string.mile));
                }
                holder.estimated_fixed.setText(SharedHelper.getKey(context, "currency")+""+Double.parseDouble(String.valueOf(estimateFare.getEstimatedFare())));

            }
            //holder.itemView.startAnimation(zoomIn);
        } else {
            holder.mFrame_service.setBackground(context.getResources().getDrawable(R.drawable.service_bkg));
            holder.serviceName.setTextColor(context.getResources().getColor(R.color.colorPrimaryText));
            holder.itemView.setAlpha((float) 1.0);
            holder.estimated_fixed.setVisibility(View.INVISIBLE);
            holder.price.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            Service object = list.get(position);
            if (object != null) {
                if (view.getId() == R.id.item_view) {
                    if (lastCheckedPos == position) {
                        RateCardFragment.SERVICE = object;
                        //((MainActivity) context).changeFragment(new RateCardFragment());
                    }
                    lastCheckedPos = position;
                    notifyDataSetChanged();
                }
                YoYo.with(Techniques.BounceInRight)
                        .duration(1000)
                        .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                        .interpolate(new AccelerateDecelerateInterpolator())
                        .playOn(holder.image);
                mListener.whenClicked(position);
            }
        });
    }

    public String getNameResult(Name value){

        String language=  SharedHelper.getKey(context,"lang_code","English");
        if (language.equals("English")){
            return value.getEn();

        }else if (language.equals("Russian")){
            return value.getRu();
        }else if (language.equals("Azerbaijan")){
            return value.getAz();
        }

        return value.getEn();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Service getSelectedService() {
        if (list.size() > 0) return list.get(lastCheckedPos);
        else return null;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemView;
        private TextView serviceName, price, estimated_fixed;
        private ImageView image;
        private FrameLayout mFrame_service;

        MyViewHolder(View view) {
            super(view);
            mFrame_service = view.findViewById(R.id.frame_service);
            estimated_fixed = view.findViewById(R.id.estimated_fixed);
            serviceName = view.findViewById(R.id.service_name);
            price = view.findViewById(R.id.price);
            image = view.findViewById(R.id.image);
            itemView = view.findViewById(R.id.item_view);
        }
    }
}
