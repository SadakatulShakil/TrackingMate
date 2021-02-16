package com.futureskyltd.trackingmate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futureskyltd.trackingmate.Model.ViewVisitReport.Datum;
import com.futureskyltd.trackingmate.R;
import com.futureskyltd.trackingmate.View.DetailsVisitReportActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VisitReportAdapter extends RecyclerView.Adapter<VisitReportAdapter.viewHolder> {
    private Context context;
    private ArrayList<Datum> visitReportArrayList;

    public VisitReportAdapter(Context context, ArrayList<Datum> visitReportArrayList) {
        this.context = context;
        this.visitReportArrayList = visitReportArrayList;
    }

    @NonNull
    @Override
    public VisitReportAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_report_list_view, parent, false);
        return new VisitReportAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitReportAdapter.viewHolder holder, int position) {
        Datum datum = visitReportArrayList.get(position);

        holder.serialNo.setText("Serial No: "+datum.getId());
        holder.marketName.setText("Market: "+datum.getMarketName());
        holder.shopName.setText("Shop: "+datum.getShopName());
        holder.businessType.setText("Business: "+datum.getBusinessType());
        holder.productsType.setText("Products: "+datum.getProductsType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsVisitReportActivity.class);
                intent.putExtra("visitReport", datum);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return visitReportArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView serialNo, marketName, shopName, businessType, productsType;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            serialNo = itemView.findViewById(R.id.serialNo);
            marketName = itemView.findViewById(R.id.marketName);
            shopName = itemView.findViewById(R.id.shopName);
            businessType = itemView.findViewById(R.id.businessType);
            productsType = itemView.findViewById(R.id.productsType);
        }
    }
}
