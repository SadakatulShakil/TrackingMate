package com.example.trackingmate;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.trackingmate.API.ApiInterface;
import com.example.trackingmate.API.RetrofitClient;
import com.example.trackingmate.Model.StoreLocation.StoreLocation;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationService extends Service {
    public static final String TAG = "Location";
    private Timer t = new Timer();
    private String currentTime, currentDate, retrievedToken;
    private SharedPreferences preferences;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                /////current location/////
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                retrievedToken  = preferences.getString("TOKEN",null);
                String uLatitude = String.valueOf(latitude);
                String uLongitude = String.valueOf(longitude);

                Log.d(TAG, "onLocationResult: "+retrievedToken);
                SimpleDateFormat format = new SimpleDateFormat("HHmm",
                        Locale.getDefault());
                String now = format.format(new Date());
                String start = "09:00".replace(":", "");
                String end = "19:00".replace(":", "");
                if(Integer.valueOf(now) > Integer.valueOf(start)
                        && Integer.valueOf(now) < Integer.valueOf(end)){


                    Retrofit retrofit = RetrofitClient.getRetrofitClient1();
                    ApiInterface api = retrofit.create(ApiInterface.class);

                    Call<StoreLocation> storeLocationCall = api.postByStoreLocation("Bearer "+retrievedToken, uLatitude, uLongitude);

                    storeLocationCall.enqueue(new Callback<StoreLocation>() {
                        @Override
                        public void onResponse(Call<StoreLocation> call, Response<StoreLocation> response) {
                            Log.d(TAG, "onSignUpRes: " + response.code());
                            if(response.code() == 200){
                                StoreLocation storeLocation = response.body();
                                Log.d(TAG, "onLocationResult: " + storeLocation.getData().getLatitude() + "..." + storeLocation.getData().getLongitude());
                            }
                        }

                        @Override
                        public void onFailure(Call<StoreLocation> call, Throwable t) {
                            Log.d(TAG, "onFailure: "+t.getMessage());
                        }
                    });
                }else{
                    Log.d(TAG, "onLocationResult: "+ "Not Exact time");
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Yet Implemented !");
    }

    private void startLocationService(String currentTime, String currentDate) {

        String channelId = "location_notification_channel";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null
                    && notificationManager.getNotificationChannel(channelId) == null) {

                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used for location service !");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);///Interval in which we want tp get location ex: 30 minutes = 1,800,000 milliseconds////
        locationRequest.setFastestInterval(2000);///If a location is available sooner you can get early///
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(Calendar.getInstance().getTime().getHours() == 10 &&
                        Calendar.getInstance().getTime().getMinutes() == 22){

                    Log.d(TAG, "run: " + "do something");
                }else{

                }
            }
        },0, 2000);

    }

    private void stopLocationService(String currentTime, String currentDate) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


            LocationServices.getFusedLocationProviderClient(this)
                    .removeLocationUpdates(locationCallback);
            stopForeground(true);
            stopSelf();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        currentTime = myTimeFormat.format(calendar.getTime());
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        currentDate = myDateFormat.format(calendar.getTime());

        if(intent != null){
            String action = intent.getAction();
            if(action != null){
            }
            if(action.equals(Constants.STATIC_START_LOCATION_SERVICE)){
                startLocationService(currentTime, currentDate);
                    /*Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    try {
                        Date estimatedOfficeTime = myTimeFormat.parse("05:33 PM");
                        Date checkInTime = myTimeFormat.parse(currentTime);

                        int diff = checkInTime.compareTo(estimatedOfficeTime);
                        Log.d(TAG, "onCreate: " + "Compare Value: "+ diff);

                        if(diff>0){///late office
                            startLocationService();
                        }else if(diff == 0){//in time office
                            startLocationService();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
*/
            }else if(action.equals(Constants.STATIC_STOP_LOCATION_SERVICE)){
                stopLocationService(currentTime, currentDate);
               /* Calendar calendar = Calendar.getInstance();
                SimpleDateFormat myTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                try {
                    Date estimatedOfficeTime = myTimeFormat.parse("05:350 PM");
                    Date checkInTime = myTimeFormat.parse(currentTime);

                    int diff = checkInTime.compareTo(estimatedOfficeTime);
                    Log.d(TAG, "onCreate: " + "Compare Value: "+ diff);

                    if(diff>0){///late office
                        stopLocationService();
                    }else if(diff == 0){//in time office
                        stopLocationService();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
