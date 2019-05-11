package com.example.maddi.logme.API.Response;

import com.example.maddi.logme.API.Models.SensorData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SensorResponse {
    @SerializedName("response")
    @Expose
    public List<SensorData> data;
}
