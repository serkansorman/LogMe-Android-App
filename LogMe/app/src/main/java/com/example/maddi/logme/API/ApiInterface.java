package com.example.maddi.logme.API;

import com.example.maddi.logme.API.Response.SensorResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("service/test")
    Call<SensorResponse> registerWithEmail();
}
