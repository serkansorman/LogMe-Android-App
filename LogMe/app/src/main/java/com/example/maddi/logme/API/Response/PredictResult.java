package com.example.maddi.logme.API.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PredictResult {
    @SerializedName("message")
    @Expose
    public String activity;
}
