package com.futureskyltd.trackingmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.futureskyltd.trackingmate.API.ApiInterface;
import com.futureskyltd.trackingmate.API.RetrofitClient;
import com.futureskyltd.trackingmate.Model.AuthData;
import com.futureskyltd.trackingmate.View.AddMerchantActivity;
import com.futureskyltd.trackingmate.View.AllMerchantListActivity;
import com.futureskyltd.trackingmate.View.ProfileActivity;
import com.futureskyltd.trackingmate.View.RegisterAndLoginActivity;
import com.futureskyltd.trackingmate.View.ViewVisitReportActivity;
import com.futureskyltd.trackingmate.View.VisitReportActivity;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button startBtn, stopBtn;
    private TextView logOutBtn;
    private String currentTime, currentDate, retrievedToken;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    public static final String TAG = "main";
    private SharedPreferences preferences;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar dToolbar;
    private View navHeaderView;
    private TextView userName, userContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startBtn = findViewById(R.id.startBtn);
        //stopBtn = findViewById(R.id.stopBtn);
        //logOutBtn = findViewById(R.id.logOut);
        inItView();
        navHeaderView = navigationView.getHeaderView(0);

        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        retrievedToken  = preferences.getString("TOKEN",null);
        getUserData();

        initNavigationViewDrawer();



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        currentTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        currentDate = myDateFormat.format(calendar.getTime());

        startLocationService();

        /*startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

       /* stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
            }
        });*/
       /* logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                preferences.edit().putString("TOKEN", null).apply();
                Intent logInIntent = new Intent(MainActivity.this, RegisterAndLoginActivity.class);
                startActivity(logInIntent);
                finish();
            }
        });*/

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, dToolbar,
                R.string.drawer_open, R.string.drawer_closed);
        drawerToggle.getDrawerArrowDrawable().setColor(Color.BLACK);
        drawerToggle.setDrawerArrowDrawable(drawerToggle.getDrawerArrowDrawable());
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    private void getUserData() {
        Retrofit retrofit = RetrofitClient.getRetrofitClient1();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<AuthData> authDataCall = api.getByAuthQuery("Bearer "+retrievedToken);

        authDataCall.enqueue(new Callback<AuthData>() {
            @Override
            public void onResponse(Call<AuthData> call, Response<AuthData> response) {
                Log.d(TAG, "onResponse: " +response.code());

                if(response.code() == 200){
                    AuthData authData = response.body();
                    userName.setText(authData.getName());
                    userContact.setText(authData.getPhone());
                }
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {

            }
        });

    }

    private void initNavigationViewDrawer() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch ((item.getItemId())) {

                    case R.id.profile:
                        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        //Toast.makeText(UserUiContainerActivity.this, "Settings Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.add_merchant:
                        Intent add_merchant = new Intent(MainActivity.this, AddMerchantActivity.class);
                        startActivity(add_merchant);
                        // Toast.makeText(UserUiContainerActivity.this, "FeedBack Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.all_merchant:
                        Intent merchantList = new Intent(MainActivity.this, AllMerchantListActivity.class);
                        startActivity(merchantList);
                        //Toast.makeText(MainActivity.this, "This is Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.visit_report:
                        Intent visit_report = new Intent(MainActivity.this, VisitReportActivity.class);
                        startActivity(visit_report);
                        //Toast.makeText(UserUiContainerActivity.this, "About Under Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.view_visit_report:
                        Intent visitReport = new Intent(MainActivity.this, ViewVisitReportActivity.class);
                        startActivity(visitReport);
                        //Toast.makeText(MainActivity.this, "This is Construction be Patient!", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.order_report:
                       /* Intent intent3 = new Intent(context, AboutUsActivity.class);
                        startActivity(intent3);*/
                        Toast.makeText(MainActivity.this, "This is Construction be Patient!", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.log_out:
                        finish();
                        preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                        preferences.edit().putString("TOKEN", null).apply();
                        Intent logInIntent = new Intent(MainActivity.this, RegisterAndLoginActivity.class);
                        startActivity(logInIntent);
                        //Toast.makeText(UserUiContainerActivity.this, "Successfully Log Out", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void inItView() {
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationDrawer);
        navHeaderView = navigationView.getHeaderView(0);
        userName = navHeaderView.findViewById(R.id.userName);
        userContact = navHeaderView.findViewById(R.id.contact);
        dToolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission Denied !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent serviceIntent = new Intent(getApplicationContext(), LocationService.class);
            serviceIntent.setAction(Constants.STATIC_START_LOCATION_SERVICE);
            startService(serviceIntent);
            Toast.makeText(this, "Location Service Started !", Toast.LENGTH_SHORT).show();
        }
    }

   /* private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent serviceIntent = new Intent(getApplicationContext(), LocationService.class);
            serviceIntent.setAction(Constants.STATIC_STOP_LOCATION_SERVICE);
            startService(serviceIntent);
            Toast.makeText(this, "Location Service Stoped !", Toast.LENGTH_SHORT).show();
        }
    }*/

}