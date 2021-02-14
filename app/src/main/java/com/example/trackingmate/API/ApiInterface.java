package com.example.trackingmate.API;
import com.example.trackingmate.Model.AddMerchant.AddMerchant;
import com.example.trackingmate.Model.AllMerchant.AllMerchant;
import com.example.trackingmate.Model.AuthData;
import com.example.trackingmate.Model.District.DistrictList;
import com.example.trackingmate.Model.Login.Login;
import com.example.trackingmate.Model.Registration;
import com.example.trackingmate.Model.StoreLocation.StoreLocation;
import com.example.trackingmate.Model.Upazila.UpazilatList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("accept: application/json, content-type: application/json")
    @FormUrlEncoded
    @POST("register")
    Call<Registration> postByRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone_no,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation
    );

    @Headers("accept: application/json, content-type: application/json")
    @FormUrlEncoded
    @POST("login")
    Call<Login> postByLogIn(
            @Field("email") String email,
            @Field("password") String password
    );

    @Headers("accept: application/json, content-type: application/json")
    @FormUrlEncoded
    @POST("location")
    Call<StoreLocation> postByStoreLocation(
            @Header("Authorization") String token,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @Headers("accept: application/json, content-type: application/json")
    @GET("profile")
    Call<AuthData> getByAuthQuery(
            @Header("Authorization") String token
    );

    @Headers("accept: application/json, content-type: application/json")
    @GET("district-list")
    Call<DistrictList> getByDistrict();

    @Headers("accept: application/json, content-type: application/json")
    @FormUrlEncoded
    @POST("upazilas-selected-by-district")
    Call<UpazilatList> postByUpazila(
            @Field("district_id") int district_id);

    @Headers("accept: application/json, content-type: application/json")
    @FormUrlEncoded
    @POST("addmerchantbyemployee")
    Call<AddMerchant> postByAddMerchant(
            @Field("first_name") String first_name,
            @Field("username") String username,
            @Field("email") String email,
            @Field("phone_no") String phone_no,
            @Field("password") String password,
            @Field("future_employee_id") String future_employee_id,
            @Field("storename") String storename,
            @Field("district") int district,
            @Field("upazila") int upazila,
            @Field("address") String address,
            @Field("zip") String zip,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @Headers("accept: application/json, content-type: application/json")
    @GET("merchantslist")
    Call<AllMerchant> getByAllMerchant(
            @Query("future_employee_id") String future_employee_id
    );
}
