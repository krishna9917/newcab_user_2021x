package com.softechurecab.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.softechurecab.app.R;
import com.softechurecab.app.data.network.model.ReferralsData;
import java.util.ArrayList;

public class ReferralsAdapter extends RecyclerView.Adapter<ReferralsAdapter.ReferralsViewHolder>{
  private Context context;
  private ArrayList<ReferralsData> referralList;

    public ReferralsAdapter(Context context, ArrayList<ReferralsData> referralList) {
        this.context = context;
        this.referralList = referralList;
    }

    @NonNull
    @Override
    public ReferralsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReferralsViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_referral_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReferralsViewHolder holder, int position) {
        holder.txtAmount.setText("₹ "+referralList.get(position).getAmount());
        holder.txtReferralDate.setText(referralList.get(position).getDate());
        holder.txtReferralName.setText(referralList.get(position).getFull_name());
    }
    @Override
    public int getItemCount() {
        return referralList==null?0:referralList.size();
    }
    class ReferralsViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvMultipleInvite;
        ConstraintLayout clMultiReferrals;
        TextView txtMultipleReferrals,txtReferralName,txtReferralDate,txtAmount;
        ImageView imgMore;
        public ReferralsViewHolder(@NonNull View itemView) {
            super(itemView);
            rvMultipleInvite=itemView.findViewById(R.id.rvMultipleInvite);
            clMultiReferrals=itemView.findViewById(R.id.clMultiReferrals);
            txtMultipleReferrals=itemView.findViewById(R.id.txtMultipleReferrals);
            txtReferralName=itemView.findViewById(R.id.txtReferralName);
            txtReferralDate=itemView.findViewById(R.id.txtReferralDate);
            txtAmount=itemView.findViewById(R.id.txtAmount);
            imgMore=itemView.findViewById(R.id.imgMore);
        }
    }
}
