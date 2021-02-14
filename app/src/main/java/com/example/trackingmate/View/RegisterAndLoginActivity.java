package com.example.trackingmate.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackingmate.API.ApiInterface;
import com.example.trackingmate.API.RetrofitClient;
import com.example.trackingmate.MainActivity;
import com.example.trackingmate.Model.Login.Login;
import com.example.trackingmate.Model.Registration;
import com.example.trackingmate.R;

public class RegisterAndLoginActivity extends AppCompatActivity {

    private TextView goToRegister, goToLogIn, logInBtn, registrationBtn;
    private ConstraintLayout logInLay, registerLay;
    private EditText lEmailOrPhone, lPassword, rEmail, rPhone, rName, rPassword, rConfirmPassword;
    private ProgressBar progressBar;
    public static final String TAG = "userLog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);
        inItView();

        SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrievedToken  = preferences.getString("TOKEN",null);
        if(retrievedToken != null){
            Intent main = new Intent(RegisterAndLoginActivity.this, MainActivity.class);
            startActivity(main);
            finish();
        }
        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInLay.setVisibility(View.GONE);
                registerLay.setVisibility(View.VISIBLE);
            }
        });

        goToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInLay.setVisibility(View.VISIBLE);
                registerLay.setVisibility(View.GONE);
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uEmailOrName = lEmailOrPhone.getText().toString().trim();
                String uPassword = lPassword.getText().toString().trim();

                if (uEmailOrName.isEmpty()) {
                    lEmailOrPhone.setError("This field is required!");
                    lEmailOrPhone.requestFocus();
                    return;
                }

                if (uPassword.isEmpty()) {
                    lPassword.setError("This field is required!");
                    lPassword.requestFocus();
                    return;
                }

                loginProcess(uEmailOrName, uPassword);
            }
        });

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = rEmail.getText().toString().trim();
                String phone = rPhone.getText().toString().trim();
                String name = rName.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String confirmPassword = rConfirmPassword.getText().toString().trim();

                if (email.isEmpty() || !email.matches(emailPattern)) {
                    rEmail.setError("This field is required!");
                    rEmail.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    rPhone.setError("This field is required!");
                    rPhone.requestFocus();
                    return;
                }

                if (name.isEmpty()) {
                    rName.setError("This field is required!");
                    rName.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    rPassword.setError("This field is required!");
                    rPassword.requestFocus();
                    return;
                }
                if (confirmPassword.isEmpty()) {
                    rConfirmPassword.setError("This field is required!");
                    rConfirmPassword.requestFocus();
                    return;
                }
                if (password.equals(confirmPassword)) {
                    RegisterUserData(name, email, phone, password, confirmPassword);
                }else {
                    Toast.makeText(RegisterAndLoginActivity.this, "Password don't match !", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loginProcess(final String uEmailOrName, final String uPassword) {
        Retrofit retrofit = RetrofitClient.getRetrofitClient1();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<Login>  logInCall = api.postByLogIn(uEmailOrName, uPassword);

        logInCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.d(TAG, "onSignUpRes: " + response.code());
                progressBar.setVisibility(View.GONE);
                if(response.code() == 200){
                    Login login = response.body();
                    SharedPreferences preferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    Toast.makeText(RegisterAndLoginActivity.this, "Successfully Login !", Toast.LENGTH_SHORT).show();
                    preferences.edit().putString("TOKEN", login.getToken()).apply();
                    Intent logIn = new Intent(RegisterAndLoginActivity.this, MainActivity.class);
                    startActivity(logIn);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });

    }

    private void RegisterUserData(final String name, final String email, final String phone, final String password, final String confirmPassword) {
        Retrofit retrofit = RetrofitClient.getRetrofitClient1();
        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<Registration> registrationCall = api.postByRegister(name, email, phone, password, confirmPassword);

        registrationCall.enqueue(new Callback<Registration>() {
            @Override
            public void onResponse(Call<Registration> call, Response<Registration> response) {
                Log.d(TAG, "onSignUpRes: " + response.code());
                progressBar.setVisibility(View.GONE);
                if(response.code() == 200){
                    progressBar.setVisibility(View.GONE);
                    Registration registrationInfo = response.body();
                    Log.d(TAG, "onResponse: "+registrationInfo.toString());
                    logInLay.setVisibility(View.VISIBLE);
                    registerLay.setVisibility(View.GONE);

                    Toast.makeText(RegisterAndLoginActivity.this, "Successfully registered !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Registration> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    private void inItView() {
        goToRegister = findViewById(R.id.goToRegister);
        goToLogIn = findViewById(R.id.goToLogIn);
        logInLay = findViewById(R.id.loginLay);
        registerLay = findViewById(R.id.registrationLay);
        lEmailOrPhone = findViewById(R.id.emailEt);
        lPassword = findViewById(R.id.passwordEt);
        logInBtn = findViewById(R.id.logInBt);
        rEmail = findViewById(R.id.rEmailEt);
        rPhone = findViewById(R.id.phoneEt);
        rName = findViewById(R.id.rNameEt);
        rPassword = findViewById(R.id.rPasswordEt);
        registrationBtn = findViewById(R.id.registerBt);
        rConfirmPassword = findViewById(R.id.rConfirmPasswordEt);
        progressBar = findViewById(R.id.progressBar);
    }
}