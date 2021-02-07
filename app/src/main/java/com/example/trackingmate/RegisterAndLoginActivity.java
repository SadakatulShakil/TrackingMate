package com.example.trackingmate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterAndLoginActivity extends AppCompatActivity {

    private TextView goToRegister, goToLogIn, logInBtn, registrationBtn;
    private ConstraintLayout logInLay, registerLay;
    private EditText lEmailOrPhone, lPassword, rEmail, rPhone, rName, rPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);
        inItView();

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

                Intent logIn = new Intent(RegisterAndLoginActivity.this, MainActivity.class);
                startActivity(logIn);
            }
        });

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = rEmail.getText().toString().trim();
                String phone = rPhone.getText().toString().trim();
                String name = rName.getText().toString().trim();
                String password = rPassword.getText().toString().trim();

                if (email.isEmpty()) {
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
    }
}