package com.example.trackingmate.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trackingmate.Model.AllMerchant.AllMerchant;
import com.example.trackingmate.Model.AllMerchant.Datum;
import com.example.trackingmate.R;

public class MerchantDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Datum merchantInfo;
    private TextView fullNameTv, userNameTv, userIdTv, userPhoneTv, userAddressTv, userEmailTv, userStoreName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_details);
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
        merchantInfo = (Datum) intent.getSerializableExtra("merchantDetail");

        fullNameTv.setText(merchantInfo.getName());
        userNameTv.setText(merchantInfo.getUsername());
        userIdTv.setText(merchantInfo.getId());
        userPhoneTv.setText(merchantInfo.getPhone());
        userAddressTv.setText(merchantInfo.getUserAddress());
        userEmailTv.setText(merchantInfo.getEmail());
        userStoreName.setText(merchantInfo.getStoreName());
    }

    private void inItView() {
        toolbar = findViewById(R.id.toolbar);
        fullNameTv = findViewById(R.id.userFullName);
        userNameTv = findViewById(R.id.userName);
        userIdTv = findViewById(R.id.userId);
        userPhoneTv = findViewById(R.id.userContact);
        userAddressTv = findViewById(R.id.userAddress);
        userEmailTv = findViewById(R.id.userEmail);
        userStoreName = findViewById(R.id.userShopName);
    }
}