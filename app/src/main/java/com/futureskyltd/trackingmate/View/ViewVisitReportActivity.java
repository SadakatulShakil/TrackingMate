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
import com.futureskyltd.trackingmate.Adapter.VisitReportAdapter;
import com.futureskyltd.trackingmate.Model.AuthData;
import com.futureskyltd.trackingmate.Model.ViewVisitReport.Datum;
import com.futureskyltd.trackingmate.Model.ViewVisitReport.ViewVisitReport;
import com.futureskyltd.trackingmate.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewVisitReportActivity extends AppCompatActivity {
    private RecyclerView allVisitRecyclerView, desireVisitRevView;
    private VisitReportAdapter visitReportAdapter;
    private ArrayList<Datum> mVisitReportArrayList = new ArrayList<>();
    private ArrayList<Datum> mDesireVisitReportArrayList = new ArrayList<>();
    private ProgressBar progressBar;
    private String employeeId, retrievedToken, desiredDate;
    private SharedPreferences preferences;
    private Toolbar toolbar;
    private EditText searchDate;
    private int year, month, day;
    private TextView seeAll;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static final String TAG = "VisitReport";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_visit_report);
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
                    getAllVisitReportList(employeeId);
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
                            ViewVisitReportActivity.this,
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
                allVisitRecyclerView.setVisibility(View.VISIBLE);
                desireVisitRevView.setVisibility(View.GONE);
            }
        });

    }

    private void getDesiredAllMerchantList(String employeeId, String desiredDate) {
        Log.d(TAG, "getAllMerchantList: " +employeeId);
        Retrofit retrofit = RetrofitClient.getRetrofitClient2();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<ViewVisitReport> allVisitReport = api.getByVisitReport(employeeId);

        allVisitReport.enqueue(new Callback<ViewVisitReport>() {
            @Override
            public void onResponse(Call<ViewVisitReport> call, Response<ViewVisitReport> response) {
                Log.d(TAG, "onResponse: " +response.code());
                if(response.code() == 200){
                    mDesireVisitReportArrayList.clear();
                    mVisitReportArrayList.clear();
                    allVisitRecyclerView.setVisibility(View.GONE);
                    desireVisitRevView.setVisibility(View.VISIBLE);
                    ViewVisitReport viewVisitReport = response.body();

                    Log.d(TAG, "onResponse: " +viewVisitReport.toString());
                    //mMerchantArrayList.clear();

                    mVisitReportArrayList.addAll(viewVisitReport.getData());
                    Log.d(TAG, "onResponse: " +mVisitReportArrayList.size()+"..."+desiredDate);
                    for(int i = 0; i < mVisitReportArrayList.size(); i++){
                        Log.d(TAG, "onResponseCheck: " +i+"...."+viewVisitReport.getData().get(i).getCreated());
                        if(viewVisitReport.getData().get(i).getCreated().equals(desiredDate)){
                            Log.d(TAG, "onResponsePrint: " +"true");

                            mDesireVisitReportArrayList.add(mVisitReportArrayList.get(i));
                            visitReportAdapter = new VisitReportAdapter(ViewVisitReportActivity.this, mDesireVisitReportArrayList);
                            desireVisitRevView.setLayoutManager(new LinearLayoutManager(ViewVisitReportActivity.this));
                            desireVisitRevView.setAdapter(visitReportAdapter);
                            visitReportAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onResponseOutput: "+mDesireVisitReportArrayList.size());
                        }else
                            Log.d(TAG, "onResponsePrint: " +"false");
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewVisitReport> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });
    }

    private void getAllVisitReportList(String employeeId) {
        Log.d(TAG, "getAllMerchantList: " +employeeId);
        Retrofit retrofit = RetrofitClient.getRetrofitClient2();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<ViewVisitReport> allMerchantCall = api.getByVisitReport(employeeId);

        allMerchantCall.enqueue(new Callback<ViewVisitReport>() {
            @Override
            public void onResponse(Call<ViewVisitReport> call, Response<ViewVisitReport> response) {
                Log.d(TAG, "onResponse: " +response.code());
                if(response.code() == 200){
                    ViewVisitReport allVisitReport = response.body();

                    Log.d(TAG, "onResponse: " +allVisitReport.toString());
                    //mMerchantArrayList.clear();

                    mVisitReportArrayList.addAll(allVisitReport.getData());
                    Log.d(TAG, "onResponse: " +mVisitReportArrayList.size());
                    visitReportAdapter = new VisitReportAdapter(ViewVisitReportActivity.this, mVisitReportArrayList);
                    allVisitRecyclerView.setLayoutManager(new LinearLayoutManager(ViewVisitReportActivity.this));
                    allVisitRecyclerView.setAdapter(visitReportAdapter);
                    visitReportAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ViewVisitReport> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });
    }

    private void inItView() {
        allVisitRecyclerView = findViewById(R.id.allVisitReportRevView);
        desireVisitRevView = findViewById(R.id.desireVisitReportRevView);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        searchDate = findViewById(R.id.searchList);
        seeAll = findViewById(R.id.seeAll);
    }
}