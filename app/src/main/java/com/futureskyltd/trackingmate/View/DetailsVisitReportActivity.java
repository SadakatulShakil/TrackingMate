package com.futureskyltd.trackingmate.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.futureskyltd.trackingmate.Model.ViewVisitReport.Datum;
import com.futureskyltd.trackingmate.R;

public class DetailsVisitReportActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView shopName, userId, marketName, contactPerson, shopAddress, ownerContact, businessType, productsType, remarks;
    private Datum visitReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_visit_report);
        inItView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.ic_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        Intent intent = getIntent();
        visitReport = (Datum) intent.getSerializableExtra("visitReport");

        shopName.setText(visitReport.getShopName());
        userId.setText(visitReport.getId());
        marketName.setText(visitReport.getMarketName());
        contactPerson.setText(visitReport.getContactPerson());
        shopAddress.setText(visitReport.getShopAddress());
        ownerContact.setText(visitReport.getOwnerPhone());
        businessType.setText(visitReport.getBusinessType());
        productsType.setText(visitReport.getProductsType());
        remarks.setText(visitReport.getRemarks());
    }

    private void inItView() {
        toolbar = findViewById(R.id.toolbar);
        shopName = findViewById(R.id.shopName);
        userId = findViewById(R.id.userId);
        marketName = findViewById(R.id.marketName);
        contactPerson = findViewById(R.id.contactPerson);
        shopAddress = findViewById(R.id.shopAddress);
        ownerContact = findViewById(R.id.ownerContact);
        businessType = findViewById(R.id.businessType);
        productsType = findViewById(R.id.productsType);
        remarks = findViewById(R.id.remarks);
    }
}