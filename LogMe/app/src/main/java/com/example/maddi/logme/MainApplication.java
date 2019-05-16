package com.example.maddi.logme;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.maddi.logme.API.ApiInterface;
import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import umich.cse.yctung.androidlibsvm.LibSVM;

public class MainApplication extends Application {
    private ApiInterface apiInterface;

    //public static String baseUrl = "http://10.1.40.214:6502/api/";
    public static String baseUrl = "http://192.168.43.234:6502/api/";

    private BluetoothManager btManager;
    public SimpleBluetoothDeviceInterface deviceInterface;
    private String deviceMacAddress = "B4:E6:2D:E9:53:B7";
    private long time = 0;
    public LibSVM svm;

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

        startBt();

        svm = new LibSVM();
    }

    private ApiInterface createApiInterface(String customBase) {
        apiInterface = new Retrofit.Builder().baseUrl(customBase != null ? customBase : baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class);
        return apiInterface;
    }

    @SuppressLint("CheckResult")
    private void startBt() {
        btManager = BluetoothManager.getInstance();
        if (btManager == null) {
            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
        }

        //noinspection ResultOfMethodCallIgnored
        btManager.openSerialDevice(deviceMacAddress)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onConnected, this::onConnectionFailure);
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
        float x = 0, y = 0, z = 0, temp = 0;
        int bpm = 0;
        try {
            if (tokens.length >= 3) {
                x = Float.parseFloat(tokens[0]);
                y = Float.parseFloat(tokens[1]);
                z = Float.parseFloat(tokens[2]);
            }

            if (tokens.length >= 8) {
                temp = Float.parseFloat(tokens[6]);
                bpm = Integer.parseInt(tokens[7]);
            }
        }
        catch (NumberFormatException ex) {
            Log.d("BT", "Cant parse");
        }
        double acc = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        SensorsActivity.updateProgress(bpm, (float) acc, temp);
        acc = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        StepCounterActivity.updateAcceleration(acc);
        // Also send acceleration and gyroscope data to server and get prediction
    }

    private void onConnectionFailure(Throwable throwable) {
        Toast.makeText(this, "BT ERROR " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        long current = System.currentTimeMillis() - time;
        time = System.currentTimeMillis();
        if (current > 1000) {
            startBt();
        }
    }
}
