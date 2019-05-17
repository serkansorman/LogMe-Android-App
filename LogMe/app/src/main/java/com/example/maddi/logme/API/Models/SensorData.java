package com.example.maddi.logme.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SensorData {

    public SensorData(double ax, double ay, double az, double gx, double gy, double gz, double tmp, double bpm) {
        this.acc_x = ax;
        this.acc_y = ay;
        this.acc_z = az;
        this.gyro_x = gx;
        this.gyro_y = gy;
        this.gyro_z = gz;
        this.temp = tmp;
        this.pulse = bpm;
    }

    @SerializedName("acc_x")
    @Expose
    public double acc_x;

    @SerializedName("temp")
    @Expose
    public double temp;

    @SerializedName("acc_y")
    @Expose
    public double acc_y;

    @SerializedName("acc_z")
    @Expose
    public double acc_z;

    @SerializedName("gyro_z")
    @Expose
    public double gyro_z;

    @SerializedName("gyro_y")
    @Expose
    public double gyro_y;

    @SerializedName("gyro_x")
    @Expose
    public double gyro_x;

    @SerializedName("pulse")
    @Expose
    public double pulse;
}
