package com.futureskyltd.trackingmate.API;
import com.futureskyltd.trackingmate.Model.AddMerchant.AddMerchant;
import com.futureskyltd.trackingmate.Model.AllMerchant.AllMerchant;
import com.futureskyltd.trackingmate.Model.AuthData;
import com.futureskyltd.trackingmate.Model.District.DistrictList;
import com.futureskyltd.trackingmate.Model.Login.Login;
import com.futureskyltd.trackingmate.Model.Registration;
import com.futureskyltd.trackingmate.Model.StoreLocation.StoreLocation;
import com.futureskyltd.trackingmate.Model.Upazila.UpazilatList;
import com.futureskyltd.trackingmate.Model.ViewVisitReport.ViewVisitReport;
import com.futureskyltd.trackingmate.Model.VisitReport.VisitReport;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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

    @Headers("accept: application/json, content-type: application/json")
    @FormUrlEncoded
    @POST("visitreport")
    Call<VisitReport> postByAddVisitReport(
            @Field("future_employee_id") String future_employee_id,
            @Field("market_name") String market_name,
            @Field("shop_name") String shop_name,
            @Field("contact_person") String contact_person,
            @Field("shop_address") String shop_address,
            @Field("business_type") String business_type,
            @Field("products_type") String products_type,
            @Field("remarks") String remarks,
            @Field("owner_phone") String owner_phone,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @Headers("accept: application/json, content-type: application/json")
    @GET("visitreportlist")
    Call<ViewVisitReport> getByVisitReport(
            @Query("future_employee_id") String future_employee_id
    );
}
