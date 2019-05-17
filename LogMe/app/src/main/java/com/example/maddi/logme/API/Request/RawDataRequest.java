package com.example.maddi.logme.API.Request;

import com.example.maddi.logme.API.Models.SensorData;

import java.util.List;

public class RawDataRequest {
    List<SensorData> mData;

    public RawDataRequest(List<SensorData> data){
        mData = data;
    }
}
