package com.futureskyltd.trackingmate.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public static final String BASE_URL1 = "https://www.futureskyltd.com/api/";////field manager registration, login and location
    public static final String BASE_URL2 = "https://www.bebsha.com/api/";////Add customer/merchant/ get merchant list
    //public static final String BASE_URL3 = "https://www.bebsha.com/api/";/// district and upazilla list

    public static Retrofit retrofit1;
    public static Retrofit retrofit2;
    public static Retrofit retrofit3;

    public static Retrofit getRetrofitClient1() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();



        if (retrofit1 == null) {
            retrofit1 = new Retrofit.Builder().baseUrl(BASE_URL1)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit1;
    }

    public static Retrofit getRetrofitClient2() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();



        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder().baseUrl(BASE_URL2)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit2;
    }

   /* public static Retrofit getRetrofitClient3() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();



        if (retrofit3 == null) {
            retrofit3 = new Retrofit.Builder().baseUrl(BASE_URL3)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit3;
    }*/

}
