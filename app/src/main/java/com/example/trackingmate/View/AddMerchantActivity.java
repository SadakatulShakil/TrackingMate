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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackingmate.API.ApiInterface;
import com.example.trackingmate.API.RetrofitClient;
import com.example.trackingmate.Adapter.DistrictAdapter;
import com.example.trackingmate.Adapter.UpazilaAdapter;
import com.example.trackingmate.Model.AddMerchant.AddMerchant;
import com.example.trackingmate.Model.AuthData;
import com.example.trackingmate.Model.District.District;
import com.example.trackingmate.Model.District.DistrictList;
import com.example.trackingmate.Model.Upazila.Upazila;
import com.example.trackingmate.Model.Upazila.UpazilatList;
import com.example.trackingmate.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Locale;

public class AddMerchantActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText nameEt, userNameEt, userEmailEt, userPhoneEt, userStoreNameEt, userAddressEt, userZipEt, passwordEt, confirmPasswordEt;
    private Spinner districtSpinner, upazilaSpinner;
    private String districtName, upazilaName;
    private int district_id, upazila_id;
    private ArrayList<District> mDistrictList = new ArrayList<>();
    private ArrayList<Upazila> mUpazilaList = new ArrayList<>();
    private DistrictAdapter mDistrictAdapter;
    private UpazilaAdapter mUpazilaAdapter;
    private SharedPreferences preferences;
    private String retrievedToken, employeeId,latitude, longitude;
    private TextView addMerchantBt;
    private FusedLocationProviderClient client;
    private Geocoder geocoder;
    private double lat, lng;
    private ProgressBar progressBar;
    public static final String TAG = "AddMerchant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_merchant);
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
        geocoder= new Geocoder(AddMerchantActivity.this, Locale.getDefault());
        client = LocationServices.getFusedLocationProviderClient(AddMerchantActivity.this);
        requestPermission();

        if(ActivityCompat.checkSelfPermission(AddMerchantActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }

        client.getLastLocation().addOnSuccessListener((Activity) AddMerchantActivity.this, new OnSuccessListener<Location>() {
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
                    Toast.makeText(AddMerchantActivity.this, "Couldn't find Any location !", Toast.LENGTH_SHORT).show();
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

                    getDistrictList(employeeId);
                }
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });

        addMerchantBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMerchantMerchant(employeeId, district_id, upazila_id, latitude, longitude);
                progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onClick: " +employeeId);
            }
        });

    }

    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    private void getDistrictList(String employeeId) {

        Retrofit retrofit = RetrofitClient.getRetrofitClient2();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<DistrictList> districtCall = api.getByDistrict();

        districtCall.enqueue(new Callback<DistrictList>() {
            @Override
            public void onResponse(Call<DistrictList> call, Response<DistrictList> response) {
                Log.d(TAG, "onResponse: " + response.code());
                DistrictList districtList = response.body();
                mDistrictList = (ArrayList<District>) districtList.getDistricts();
                Log.d(TAG, "onResponse: " + districtList.toString());
                Log.d(TAG, "onResponse: "+mDistrictList.size());
                mDistrictAdapter = new DistrictAdapter(AddMerchantActivity.this, mDistrictList);
                districtSpinner.setAdapter(mDistrictAdapter);

                districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        District clickedDistrict = (District) parent.getItemAtPosition(position);

                        districtName = clickedDistrict.getDistrict();
                        district_id = clickedDistrict.getId();

                        getUpazilaList(employeeId, district_id);

                        Toast.makeText(AddMerchantActivity.this, districtName +" is selected !"+" id: "+ district_id, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<DistrictList> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }

    private void getUpazilaList(String employeeId, int district_id) {
        Retrofit retrofit = RetrofitClient.getRetrofitClient2();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<UpazilatList> upazilaCall = api.postByUpazila(district_id);

        upazilaCall.enqueue(new Callback<UpazilatList>() {
            @Override
            public void onResponse(Call<UpazilatList> call, Response<UpazilatList> response) {
                Log.d(TAG, "onResponse: " + response.code());
                UpazilatList upazilatList = response.body();
                mUpazilaList = (ArrayList<Upazila>) upazilatList.getUpazila();
                Log.d(TAG, "onResponse: " + upazilatList.toString());
                mUpazilaAdapter = new UpazilaAdapter(AddMerchantActivity.this, mUpazilaList);
                upazilaSpinner.setAdapter(mUpazilaAdapter);

                upazilaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Upazila clickedUpazila = (Upazila) parent.getItemAtPosition(position);

                        upazilaName = clickedUpazila.getUpazila();
                        upazila_id = clickedUpazila.getId();
                        Toast.makeText(AddMerchantActivity.this, upazilaName +" is selected !"+" id: "+ upazila_id, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<UpazilatList> call, Throwable t) {
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });
    }

    private void addMerchantMerchant(String employeeId, int district_id, int upazila_id, String latitude, String longitude) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String name = nameEt.getText().toString().trim();
        String userName = userNameEt.getText().toString().trim();
        String email = userEmailEt.getText().toString().trim();
        String phone = userPhoneEt.getText().toString().trim();
        String storeName = userStoreNameEt.getText().toString().trim();
        String address = userAddressEt.getText().toString().trim();
        String zip = userZipEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String confirmPassword = confirmPasswordEt.getText().toString().trim();

        if (name.isEmpty()) {
            userNameEt.setError("Name is required!");
            userNameEt.requestFocus();
            return;
        }
        if (userName.isEmpty()) {
            userEmailEt.setError("userName is required!");
            userEmailEt.requestFocus();
            return;
        }
        if (email.isEmpty() || !email.matches(emailPattern)) {
            userPhoneEt.setError("Email is required!");
            userPhoneEt.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            userStoreNameEt.setError("phone number is required!");
            userStoreNameEt.requestFocus();
            return;
        }
        if (storeName.isEmpty()) {
            userAddressEt.setError("storeName is required!");
            userAddressEt.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            userZipEt.setError("address is required!");
            userZipEt.requestFocus();
            return;
        }
        if (zip.isEmpty()) {
            passwordEt.setError("zip is required!");
            passwordEt.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            nameEt.setError("password is required!");
            nameEt.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordEt.setError("confirmPassword is required!");
            confirmPasswordEt.requestFocus();
            return;
        }

        doSignUp(name, userName, email, phone, storeName, district_id, upazila_id, employeeId, address, zip, password, confirmPassword, latitude, longitude);


    }

    private void doSignUp(final String name, final String userName, final String email, final String phone,
                          final String storeName, final int district_id, final int upazila_id,
                          final String employeeId, final String address, final String zip, final String password,
                          final String confirmPassword, final String latitude, final String longitude) {


        Log.d(TAG, "doSignUp: " +name+" "+userName+" "+email+" "+phone+storeName+" "+district_id+upazila_id+" "+employeeId+" "+address+" "+zip+" "+latitude+" "+longitude);
        Retrofit retrofit = RetrofitClient.getRetrofitClient2();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<AddMerchant> addMerchantCall = api.postByAddMerchant(name, userName, email, phone, password, employeeId,
                                                                    storeName, district_id, upazila_id, address, zip, latitude, longitude);

        addMerchantCall.enqueue(new Callback<AddMerchant>() {
            @Override
            public void onResponse(Call<AddMerchant> call, Response<AddMerchant> response) {
                Log.d(TAG, "onResponse: " +response.code());
                if(response.code() == 200){

                    AddMerchant addMerchant = response.body();

                    Log.d(TAG, "onResponse:  "+ addMerchant.toString());
                    progressBar.setVisibility(View.GONE);
                    if(addMerchant.getStatus().equals("true")){
                        nameEt.setText("");
                        userEmailEt.setText("");
                        userNameEt.setText("");
                        userPhoneEt.setText("");
                        userStoreNameEt.setText("");
                        userAddressEt.setText("");
                        userZipEt.setText("");
                        passwordEt.setText("");
                        confirmPasswordEt.setText("");

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMerchantActivity.this);
                        alertDialog.setTitle("Successful");
                        alertDialog.setMessage("Merchant add Successfully !");
                        alertDialog.setIcon(R.drawable.ic_add_merchant);

                        alertDialog.setPositiveButton("View List", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent intent = new Intent(AddMerchantActivity.this, AllMerchantListActivity.class);
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
                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddMerchantActivity.this, addMerchant.getMessage()+"..."+addMerchant.getErrors().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddMerchant> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });

    }

    private void inItView() {
        toolbar = findViewById(R.id.toolbar);
        nameEt = findViewById(R.id.fullName);
        userNameEt = findViewById(R.id.userName);
        userEmailEt = findViewById(R.id.userEmail);
        userPhoneEt = findViewById(R.id.userPhone);
        userStoreNameEt = findViewById(R.id.shopName);
        userAddressEt = findViewById(R.id.userAddress);
        userZipEt = findViewById(R.id.userPostCode);
        passwordEt = findViewById(R.id.password);
        confirmPasswordEt = findViewById(R.id.conPassword);
        districtSpinner = findViewById(R.id.userDistrictSpinner);
        upazilaSpinner = findViewById(R.id.userUpazilaSpinner);
        addMerchantBt = findViewById(R.id.addMerchantBt);
        progressBar = findViewById(R.id.progressBar);
    }
}