package com.example.maddi.logme.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SensorData {
    @SerializedName("acc_x")
    @Expose
    public double acc_x;

    @SerializedName("temp")
    @Expose
    public double temp;

    @Override
    public String toString() {
        return "acc_x: " + acc_x + " acc_y: " + acc_y + " acc_z: " + acc_z + " temp: " + temp + " pulse: " + pulse;
    }

    @SerializedName("acc_y")
    @Expose
    public double acc_y;

    @SerializedName("acc_z")
    @Expose
    public double acc_z;

    @SerializedName("pulse")
    @Expose
    public double pulse;
}
