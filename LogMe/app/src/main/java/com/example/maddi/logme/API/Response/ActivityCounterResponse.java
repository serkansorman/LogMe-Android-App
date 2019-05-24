package com.example.maddi.logme.API.Response;

import com.example.maddi.logme.API.Models.ActivityCounter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivityCounterResponse {
    @SerializedName("response")
    @Expose
    public List<ActivityCounter> data;
}
