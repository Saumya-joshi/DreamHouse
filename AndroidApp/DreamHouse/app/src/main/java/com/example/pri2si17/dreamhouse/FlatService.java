package com.example.pri2si17.dreamhouse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pri2si17 on 12/1/16.
 */

public interface FlatService {

    @GET("/minor/flats.json")
    Call<FlatList> getJSON();
}
