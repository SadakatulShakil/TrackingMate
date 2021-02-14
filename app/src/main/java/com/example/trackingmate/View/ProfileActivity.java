package com.example.trackingmate.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackingmate.API.ApiInterface;
import com.example.trackingmate.API.RetrofitClient;
import com.example.trackingmate.Model.AuthData;
import com.example.trackingmate.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName, userContact, userAddress, editProfile;
    private SharedPreferences preferences;
    private String retrievedToken;
    public static final String TAG = "Profile";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        getUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);*/
                Toast.makeText(ProfileActivity.this, "This is under construction", Toast.LENGTH_SHORT).show();
            }
        });
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
                    profileName.setText(authData.getName());
                    userContact.setText(authData.getPhone());
                    userAddress.setText(authData.getCreatedAt());
                }
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {

            }
        });

    }

    private void inItView() {
        profileName = findViewById(R.id.userName);
        userContact = findViewById(R.id.userContact);
        userAddress = findViewById(R.id.userAddress);
        editProfile = findViewById(R.id.editProfile);
        toolbar = findViewById(R.id.toolbar);
    }
}