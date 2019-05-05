package com.example.maddi.logme.API.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SensorResponse {
    @SerializedName("version")
    @Expose
    public double version;

    @SerializedName("status")
    @Expose
    public double status;

    @Override
    public String toString() {
        return "version: " + version + " status: " + status;
    }

    //    @SerializedName("acc_y")
//    @Expose
//    public double acc_y;
//
//    @SerializedName("acc_z")
//    @Expose
//    public double acc_z;
//
//    @SerializedName("pulse")
//    @Expose
//    public double pulse;
}
