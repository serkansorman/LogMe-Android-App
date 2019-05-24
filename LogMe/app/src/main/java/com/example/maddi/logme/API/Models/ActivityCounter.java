package com.example.maddi.logme.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityCounter {

    @SerializedName("downstairs")
    @Expose
    public int down_count;

    @SerializedName("upstairs")
    @Expose
    public int up_count;

    @SerializedName("running")
    @Expose
    public int run_count;

    @SerializedName("walking")
    @Expose
    public int walk_count;

    @SerializedName("sitting")
    @Expose
    public int sit_count;

    @SerializedName("standing")
    @Expose
    public int stand_count;
}
