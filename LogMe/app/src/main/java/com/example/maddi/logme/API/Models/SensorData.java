package com.example.maddi.logme.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SensorData {

    public SensorData(int act, double tmp, int bpm, int stp, int stairCount) {
        this.activity = act;
        this.temp = tmp;
        this.pulse = bpm;
        this.step = stp;
        this.stairs = stairCount;
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

    @SerializedName("step")
    @Expose
    public int step;

    @SerializedName("stairs")
    @Expose
    public int stairs;
}
