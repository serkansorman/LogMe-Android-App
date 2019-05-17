package com.example.maddi.logme;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.util.Log;
import android.widget.Toast;

import com.example.maddi.logme.API.ApiInterface;
import com.example.maddi.logme.API.Models.ActivityType;
import com.example.maddi.logme.API.Models.SensorData;
import com.example.maddi.logme.API.Request.RawDataRequest;
import com.example.maddi.logme.API.Response.PredictResult;
import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {
    private ApiInterface apiInterface;

    public static String baseUrl = "http://192.168.43.172:6503/api/";
    //public static String baseUrl = "http://192.168.43.234:6502/api/";

    public SimpleBluetoothDeviceInterface deviceInterface;
    private long connectionFailureTimer = 0;
    private long timer = 0;
    private List<SensorData> mDataList = new ArrayList<>();
    public ActivityType currentActivity = null;
    private MainApplication tempThis = this;
    public boolean btOn = false;
    /**
     * In practise you will use some kind of dependency injection pattern.
     */

    public static ApiInterface getApiInterface(Context context, String customBase) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.createApiInterface(customBase);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        apiInterface = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class);
        startBt();
    }

    private ApiInterface createApiInterface(String customBase) {
        apiInterface = new Retrofit.Builder().baseUrl(customBase != null ? customBase : baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class);
        return apiInterface;
    }

    public void setBaseUrl(String url) {
        baseUrl = url;
        apiInterface = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class);
    }

    @SuppressLint("CheckResult")
    public void startBt() {
        BluetoothManager btManager = BluetoothManager.getInstance();
        if (btManager == null) {
            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
            startBt();
            return;
        }

        String deviceMacAddress = "B4:E6:2D:E9:53:B7";
        //noinspection ResultOfMethodCallIgnored
        btManager.openSerialDevice(deviceMacAddress)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnected, this::onConnectionFailure);
        btOn = true;
    }

    private void onConnected(BluetoothSerialDevice connectedDevice) {
        // You are now connected to this device!
        // Here you may want to retain an instance to your device:
        deviceInterface = connectedDevice.toSimpleDeviceInterface();

        // Listen to bluetooth events
        deviceInterface.setListeners(this::onMessageReceived, this::onMessageSent, this::onConnectionFailure);

        // Let's send a message:
        //deviceInterface.sendMessage("Hello world!");
        Toast.makeText(this, "BT Connected", Toast.LENGTH_LONG).show(); // Replace context with your context instance.
    }

    private void onMessageSent(String message) {
        // We sent a message! Handle it here.
        Toast.makeText(this, "Sent a message! Message was: " + message, Toast.LENGTH_LONG).show(); // Replace context with your context instance.
    }

    private void onMessageReceived(String message) {
        // We received a message! Handle it here.
        String[] tokens = message.split(",");
        float ax = 0, ay = 0, az = 0, gx = 0, gy = 0, gz = 0, temp = 0;
        int bpm = 0;
        try {
            if (tokens.length >= 3) {
                ax = Float.parseFloat(tokens[0]);
                ay = Float.parseFloat(tokens[1]);
                az = Float.parseFloat(tokens[2]);
            }

            if (tokens.length >= 6) {
                gx = Float.parseFloat(tokens[3]);
                gy = Float.parseFloat(tokens[4]);
                gz = Float.parseFloat(tokens[5]);
            }

            if (tokens.length >= 8) {
                temp = Float.parseFloat(tokens[6]);
                bpm = Integer.parseInt(tokens[7]);
            }
        }
        catch (NumberFormatException ex) {
            Log.d("BT", "Cant parse");
        }
        mDataList.add(new SensorData(ax, ay, az, gx, gy, gz, temp, bpm));
        long current = System.currentTimeMillis() - timer;
        timer = System.currentTimeMillis();
        if (current > 1000) {
            makeCall();
        }
        double acc = Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2) + Math.pow(az, 2));
        mDataList.add(new SensorData(ax, ay, az, gx, gy, gz, temp, bpm));
        SensorsActivity.updateProgress(bpm, (float) acc, temp);
        acc = Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2));
        StepCounterActivity.updateAcceleration(acc);
    }

    private void onConnectionFailure(Throwable throwable) {
        Toast.makeText(this, "BT ERROR " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e("BT", throwable.getMessage());
        long current = System.currentTimeMillis() - connectionFailureTimer;
        connectionFailureTimer = System.currentTimeMillis();
        btOn = false;

    }

    private void makeCall() {
        Call<PredictResult> request = apiInterface.sendData(new RawDataRequest(mDataList));

        request.enqueue(new Callback<PredictResult>() {
            @Override
            public void onResponse(Call<PredictResult> call, Response<PredictResult> response) {
                if (response.body() != null && response.body().activity != null) {
                    Toast.makeText(tempThis, response.body().activity, Toast.LENGTH_SHORT).show();
                    currentActivity = ActivityType.values()[response.body().activity];
                }
                mDataList = new ArrayList<>();
            }

            @Override
            public void onFailure(Call<PredictResult> call, Throwable t) {
                currentActivity = null;
                mDataList = new ArrayList<>();
            }
        });
    }


}
