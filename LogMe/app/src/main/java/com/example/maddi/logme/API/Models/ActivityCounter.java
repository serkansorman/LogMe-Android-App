package com.example.maddi.logme.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityCounter {

    @SerializedName("down_count")
    @Expose
    public int down_count;

    @SerializedName("up_count")
    @Expose
    public int up_count;

    @SerializedName("run_count")
    @Expose
    public int run_count;

    @SerializedName("walk_count")
    @Expose
    public int walk_count;

    @SerializedName("sit_count")
    @Expose
    public int sit_count;

    @SerializedName("stand_count")
    @Expose
    public int stand_count;
}
