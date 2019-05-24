package com.example.maddi.logme.API.Request;

import com.example.maddi.logme.API.Models.SensorData;

public class RawDataRequest {
    private SensorData request;

    public RawDataRequest(SensorData data){
        request = data;
    }
}
