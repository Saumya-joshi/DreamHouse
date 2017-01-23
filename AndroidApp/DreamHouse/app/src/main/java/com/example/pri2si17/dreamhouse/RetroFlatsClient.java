package com.example.pri2si17.dreamhouse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pri2si17 on 12/1/16.
 */

public class RetroFlatsClient {

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static FlatService getFlatService() {
        return getRetrofitInstance().create(FlatService.class);
    }
}
