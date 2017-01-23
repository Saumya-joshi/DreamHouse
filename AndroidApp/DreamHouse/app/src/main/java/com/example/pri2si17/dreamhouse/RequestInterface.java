package com.example.pri2si17.dreamhouse;

/**
 * Created by pri2si17 on 11/17/16.
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RequestInterface
{
    @POST("minor/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
