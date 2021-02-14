package com.example.trackingmate.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.trackingmate.API.ApiInterface;
import com.example.trackingmate.API.RetrofitClient;
import com.example.trackingmate.Adapter.MerchantAdapter;
import com.example.trackingmate.Model.AllMerchant.AllMerchant;
import com.example.trackingmate.Model.AllMerchant.Datum;
import com.example.trackingmate.Model.AuthData;
import com.example.trackingmate.R;

import java.util.ArrayList;

public class AllMerchantListActivity extends AppCompatActivity {

    private RecyclerView allMerchantRecyclerView;
    private MerchantAdapter merchantAdapter;
    private ArrayList<Datum> mMerchantArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private String employeeId, retrievedToken;
    private SharedPreferences preferences;
    private Toolbar toolbar;
    public static final String TAG = "merchant";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_merchant_list);
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
                    getAllMerchantList(employeeId);
                    Log.d(TAG, "onResponse: " +employeeId);
                }
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });


        progressBar.setVisibility(View.VISIBLE);



    }

    private void inItView() {
        allMerchantRecyclerView = findViewById(R.id.allMerchantRevView);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
    }

    private void getAllMerchantList(String employeeId) {

        Log.d(TAG, "getAllMerchantList: " +employeeId);
        Retrofit retrofit = RetrofitClient.getRetrofitClient2();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<AllMerchant> allMerchantCall = api.getByAllMerchant(employeeId);

        allMerchantCall.enqueue(new Callback<AllMerchant>() {
            @Override
            public void onResponse(Call<AllMerchant> call, Response<AllMerchant> response) {
                Log.d(TAG, "onResponse: " +response.code());
                if(response.code() == 200){
                    AllMerchant allMerchant = response.body();

                    Log.d(TAG, "onResponse: " +allMerchant.toString());
                    //mMerchantArrayList.clear();

                    mMerchantArrayList.addAll(allMerchant.getData());
                    Log.d(TAG, "onResponse: " +mMerchantArrayList.size());
                    merchantAdapter = new MerchantAdapter(AllMerchantListActivity.this, mMerchantArrayList);
                    allMerchantRecyclerView.setLayoutManager(new LinearLayoutManager(AllMerchantListActivity.this));
                    allMerchantRecyclerView.setAdapter(merchantAdapter);
                    merchantAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AllMerchant> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });
    }
}