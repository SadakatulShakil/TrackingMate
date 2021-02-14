package com.example.trackingmate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trackingmate.View.MerchantDetailsActivity;
import com.example.trackingmate.Model.AllMerchant.Datum;
import com.example.trackingmate.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MerchantAdapter extends RecyclerView.Adapter<MerchantAdapter.viewHolder> {
    private Context context;
    private ArrayList<Datum> merchantArrayList;

    public MerchantAdapter(Context context, ArrayList<Datum> merchantArrayList) {
        this.context = context;
        this.merchantArrayList = merchantArrayList;
    }

    @NonNull
    @Override
    public MerchantAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_list_view, parent, false);
        return new MerchantAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantAdapter.viewHolder holder, int position) {
        Datum allMerchantData = merchantArrayList.get(position);
        holder.merchantId.setText("Id no: "+allMerchantData.getId());
        holder.merchantName.setText("Name: "+allMerchantData.getName());
        holder.merchantEmail.setText("Email: "+allMerchantData.getEmail());
        holder.merchantPhone.setText("Phone: "+allMerchantData.getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent merchantDetail = new Intent(context, MerchantDetailsActivity.class);
                merchantDetail.putExtra("merchantDetail", allMerchantData);
                context.startActivity(merchantDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return merchantArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView merchantId, merchantName, merchantEmail, merchantPhone;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            merchantId = itemView.findViewById(R.id.serialNo);
            merchantName = itemView.findViewById(R.id.merchantName);
            merchantEmail = itemView.findViewById(R.id.merchantEmail);
            merchantPhone = itemView.findViewById(R.id.merchantPhone);
        }
    }
}
