package com.example.trackingmate.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackingmate.API.ApiInterface;
import com.example.trackingmate.API.RetrofitClient;
import com.example.trackingmate.Model.AddMerchant.AddMerchant;
import com.example.trackingmate.Model.AuthData;
import com.example.trackingmate.Model.VisitReport.VisitReport;
import com.example.trackingmate.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class VisitReportActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView marketName, shopName, contactPerson, ownerContact, shopAddress, businessType, productsType, remarks;
    private TextView addReportBtn;
    private String employeeId, retrievedToken, latitude, longitude;
    private SharedPreferences preferences;
    private ProgressBar progressBar;
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private double lat, lng;
    public static final String TAG = "Report";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_report);

        inItView();
        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken  = preferences.getString("TOKEN",null);
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

        ///current Location////
        geocoder= new Geocoder(VisitReportActivity.this, Locale.getDefault());
        client = LocationServices.getFusedLocationProviderClient(VisitReportActivity.this);
        requestPermission();

        if(ActivityCompat.checkSelfPermission(VisitReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }

        client.getLastLocation().addOnSuccessListener((Activity) VisitReportActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null)
                {


                    //txtLocation.setText(location.toString());
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(lng);
                    Log.d(TAG, "onSuccess: "+ lat+"....."+lng);

                }
                else
                {
                    Toast.makeText(VisitReportActivity.this, "Couldn't find Any location !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ///current Location////

        Retrofit retrofit = RetrofitClient.getRetrofitClient1();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<AuthData> authDataCall = api.getByAuthQuery("Bearer "+retrievedToken);

        authDataCall.enqueue(new Callback<AuthData>() {
            @Override
            public void onResponse(Call<AuthData> call, Response<AuthData> response) {
                Log.d(TAG, "onResponse: " +response.code());

                if(response.code() == 200){
                    AuthData authData = response.body();
                    employeeId = String.valueOf(authData.getId());
                    Log.d(TAG, "onResponse: " +employeeId);
                }
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });

        addReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVisitReport(employeeId, latitude, longitude);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    private void addVisitReport(String employeeId, String latitude, String longitude) {
        String uMarketName = marketName.getText().toString().trim();
        String uShopName = shopName.getText().toString().trim();
        String uContactPerson = contactPerson.getText().toString().trim();
        String uShopAddress = shopAddress.getText().toString().trim();
        String uOwnerContact = ownerContact.getText().toString().trim();
        String uBusinessType = businessType.getText().toString().trim();
        String uProductsType = productsType.getText().toString().trim();
        String uRemarks = remarks.getText().toString().trim();


        if (uMarketName.isEmpty()) {
            marketName.setError("marketName is required!");
            marketName.requestFocus();
            return;
        }
        if (uShopName.isEmpty()) {
            shopName.setError("shopName is required!");
            shopName.requestFocus();
            return;
        }

        if (uContactPerson.isEmpty()) {
            contactPerson.setError("contactPerson is required!");
            contactPerson.requestFocus();
            return;
        }
        if (uShopAddress.isEmpty()) {
            shopAddress.setError("shopAddress is required!");
            shopAddress.requestFocus();
            return;
        }
        if (uOwnerContact.isEmpty()) {
            ownerContact.setError("ownerContact is required!");
            ownerContact.requestFocus();
            return;
        }
        if (uBusinessType.isEmpty()) {
            businessType.setError("businessType is required!");
            businessType.requestFocus();
            return;
        }
        if (uProductsType.isEmpty()) {
            productsType.setError("productsType is required!");
            productsType.requestFocus();
            return;
        }
        if (uRemarks.isEmpty()) {
            remarks.setError("remarks is required!");
            remarks.requestFocus();
            return;
        }

        storeReport(employeeId, uMarketName, uShopName, uContactPerson, uShopAddress, uOwnerContact, uBusinessType, uProductsType, uRemarks, latitude, longitude);

    }

    private void storeReport(final String employeeId, final String uMarketName, final String uShopName, final String uContactPerson,
                             final String uShopAddress, final String uOwnerContact, final String uBusinessType, final String uProductsType,
                             final String uRemarks, final String latitude, final String longitude) {

        Retrofit retrofit = RetrofitClient.getRetrofitClient2();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<VisitReport> addVisitReportCall = api.postByAddVisitReport(employeeId, uMarketName, uShopName, uContactPerson, uShopAddress,
                                                uBusinessType, uProductsType, uRemarks, uOwnerContact, latitude, longitude);

        addVisitReportCall.enqueue(new Callback<VisitReport>() {
            @Override
            public void onResponse(Call<VisitReport> call, Response<VisitReport> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if(response.code() == 200){
                    VisitReport visitReport = response.body();

                    Log.d(TAG, "onResponse: " + visitReport.toString());
                    progressBar.setVisibility(View.GONE);
                    if(visitReport.getStatus().equals("true")){
                        marketName.setText("");
                        shopName.setText("");
                        contactPerson.setText("");
                        shopAddress.setText("");
                        ownerContact.setText("");
                        businessType.setText("");
                        productsType.setText("");
                        remarks.setText("");

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VisitReportActivity.this);
                        alertDialog.setTitle("Successful");
                        alertDialog.setMessage("Visit Report add Successfully !");
                        alertDialog.setIcon(R.drawable.ic_add_merchant);

                        alertDialog.setPositiveButton("Home", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(VisitReportActivity.this, AllMerchantListActivity.class);
                                startActivity(intent);
                            }
                        });

                        alertDialog.setNegativeButton("Add Another", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { {
                                dialog.dismiss();

                            }
                            }
                        });

                        alertDialog.create();
                        alertDialog.show();

                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(VisitReportActivity.this, visitReport.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VisitReport> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });
    }

    private void inItView() {
        toolbar = findViewById(R.id.toolbar);
        marketName = findViewById(R.id.marketName);
        shopName = findViewById(R.id.shopName);
        contactPerson = findViewById(R.id.contactPerson);
        ownerContact = findViewById(R.id.ownerContact);
        shopAddress = findViewById(R.id.shopAddress);
        businessType = findViewById(R.id.businessType);
        productsType = findViewById(R.id.productsType);
        remarks = findViewById(R.id.remarks);

        addReportBtn = findViewById(R.id.addReportBt);
        progressBar = findViewById(R.id.progressBar);
    }
}