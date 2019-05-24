package com.example.maddi.logme.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SensorData {

    public SensorData(int act, double tmp, int bpm) {
        this.activity = act;
        this.temp = tmp;
        this.pulse = bpm;
    }

    @SerializedName("activity")
    @Expose
    public int activity;

    @SerializedName("temp")
    @Expose
    public double temp;

    @SerializedName("pulse")
    @Expose
    public int pulse;
}
