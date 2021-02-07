package com.example.trackingmate.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers("accept: application/json, content-type: multipart/form-data")
    @FormUrlEncoded
    @POST("fantacy_dev/api/signupcomplete")
    Call<String> postByRegister(
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("phone_no") String phone_no,
            @Field("password") String password
    );

    @Headers("accept: application/json, content-type: multipart/form-data")
    @FormUrlEncoded
    @POST("fantacy_dev/api/signupcomplete")
    Call<String> postByLogIn(
            @Field("email") String email,
            @Field("password") String password
    );
}
