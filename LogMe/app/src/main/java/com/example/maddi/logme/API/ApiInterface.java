package com.example.maddi.logme.API;

import com.example.maddi.logme.API.Request.RawDataRequest;
import com.example.maddi.logme.API.Response.PredictResult;
import com.example.maddi.logme.API.Response.SensorResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("service/getLastData")
    Call<SensorResponse> getLastData();

    @POST("service/validation")
    Call<PredictResult> sendData(@Body RawDataRequest request);
}
