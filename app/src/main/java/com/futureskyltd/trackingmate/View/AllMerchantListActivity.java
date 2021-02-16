package com.futureskyltd.trackingmate.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.futureskyltd.trackingmate.API.ApiInterface;
import com.futureskyltd.trackingmate.API.RetrofitClient;
import com.futureskyltd.trackingmate.Adapter.MerchantAdapter;
import com.futureskyltd.trackingmate.Model.AllMerchant.AllMerchant;
import com.futureskyltd.trackingmate.Model.AllMerchant.Datum;
import com.futureskyltd.trackingmate.Model.AuthData;
import com.futureskyltd.trackingmate.R;

import java.util.ArrayList;
import java.util.Calendar;

public class AllMerchantListActivity extends AppCompatActivity {

    private RecyclerView allMerchantRecyclerView, desireMerchantRevView;
    private MerchantAdapter merchantAdapter;
    private ArrayList<Datum> mMerchantArrayList = new ArrayList<>();
    private ArrayList<Datum> mDesireMerchantArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private String employeeId, retrievedToken, desiredDate;
    private SharedPreferences preferences;
    private Toolbar toolbar;
    private EditText searchDate;
    private int year, month, day;
    private TextView seeAll;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
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

        searchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog dialog = new DatePickerDialog(
                            AllMerchantListActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d(TAG, "onDateSet: "+ dayOfMonth+"-"+month+"-"+year);
                searchDate.setText("Search result for : "+dayOfMonth+"-"+month+"-"+year);
                if(month<10){
                    desiredDate =  dayOfMonth+"-"+"0"+month+"-"+year;
                }else if(dayOfMonth <10){
                    desiredDate =  "0"+dayOfMonth+"-"+month+"-"+year;
                }
                else{
                    desiredDate =  dayOfMonth+"-"+month+"-"+year;
                }

                Log.d(TAG, "onDateSetOutput: "+desiredDate);
                getDesiredAllMerchantList(employeeId, desiredDate);
            }
        };

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDate.setText("");
                allMerchantRecyclerView.setVisibility(View.VISIBLE);
                desireMerchantRevView.setVisibility(View.GONE);
            }
        });

    }

    private void getDesiredAllMerchantList(String employeeId, String desiredDate) {
        Log.d(TAG, "getAllMerchantList: " +employeeId);
        Retrofit retrofit = RetrofitClient.getRetrofitClient2();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<AllMerchant> allMerchantCall = api.getByAllMerchant(employeeId);

        allMerchantCall.enqueue(new Callback<AllMerchant>() {
            @Override
            public void onResponse(Call<AllMerchant> call, Response<AllMerchant> response) {
                Log.d(TAG, "onResponse: " +response.code());
                if(response.code() == 200){
                    mDesireMerchantArrayList.clear();
                    mMerchantArrayList.clear();
                    allMerchantRecyclerView.setVisibility(View.GONE);
                    desireMerchantRevView.setVisibility(View.VISIBLE);
                    AllMerchant allMerchant = response.body();

                    Log.d(TAG, "onResponse: " +allMerchant.toString());
                    //mMerchantArrayList.clear();

                    mMerchantArrayList.addAll(allMerchant.getData());
                    Log.d(TAG, "onResponse: " +mMerchantArrayList.size()+"..."+desiredDate);
                    for(int i = 0; i < mMerchantArrayList.size(); i++){
                        Log.d(TAG, "onResponseCheck: " +i+"...."+allMerchant.getData().get(i).getCreated());
                        if(allMerchant.getData().get(i).getCreated().equals(desiredDate)){
                            Log.d(TAG, "onResponsePrint: " +"true");

                            mDesireMerchantArrayList.add(mMerchantArrayList.get(i));
                            merchantAdapter = new MerchantAdapter(AllMerchantListActivity.this, mDesireMerchantArrayList);
                            desireMerchantRevView.setLayoutManager(new LinearLayoutManager(AllMerchantListActivity.this));
                            desireMerchantRevView.setAdapter(merchantAdapter);
                            merchantAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onResponseOutput: "+mDesireMerchantArrayList.size());
                        }else
                            Log.d(TAG, "onResponsePrint: " +"false");
                    }
                }
            }

            @Override
            public void onFailure(Call<AllMerchant> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });
    }

    private void inItView() {
        allMerchantRecyclerView = findViewById(R.id.allMerchantRevView);
        desireMerchantRevView = findViewById(R.id.desireMerchantRevView);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        searchDate = findViewById(R.id.searchList);
        seeAll = findViewById(R.id.seeAll);
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