package com.example.maddi.logme;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
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
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;
import java.util.Date;
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

    //public static String baseUrl = "http://192.168.43.172:6503/api/";
    //public static String baseUrl = "http://192.168.43.234:6502/api/";
    public static String baseUrl = "http://10.1.40.70:6502/api/";

    public SimpleBluetoothDeviceInterface deviceInterface;
    private long connectionFailureTimer = 0;
    public ActivityType currentActivity = null;
    private MainApplication tempThis = this;
    public boolean btOn = false;

    private static final int VALUE_COUNT = 10;
    private double[][] lastValues = new double[5][VALUE_COUNT];
    private int valueCounter = 0;
    private int barCounter = 0;
    private boolean standing = false, climbUp = false, climbDown = false, walking = false, running = false, sitting = false;
    private boolean isFirst = true;
    private double barAvg = 0, prevBarAvg = 0;

    public int stepCount = 0;
    public int climbCount = 0;
    private boolean isLow = true, isHigh = true, stepped = false;

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
        if (!btOn) {
            btManager.openSerialDevice(deviceMacAddress)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onConnected, this::onConnectionFailure);
            btOn = true;
        }

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
        double ax = 0.0, ay = 0.0, az = 0.0, bar = 0.0, temp = 0.0;
        int bpm = 0;
        try {
            if (tokens.length >= 3) {
                ax = Double.parseDouble(tokens[0]);
                ay = Double.parseDouble(tokens[1]);
                az = Double.parseDouble(tokens[2]);
            }

            if (tokens.length >= 4) {
                bar = Double.parseDouble(tokens[3]);
            }

            if (tokens.length >= 6) {
                temp = Double.parseDouble(tokens[4]);
                bpm = Integer.parseInt(tokens[5]);
            }
        }
        catch (NumberFormatException ex) {
            Log.d("BT", "Cant parse");
        }


        predict(ax, ay, az, bar, bpm, temp);

        double acc = Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2) + Math.pow(az, 2));
        SensorsActivity.updateProgress(bpm, (float) acc, (float) temp);
    }

    private void predict(double x, double y, double z, double avgBar, int pulse, double temp) {
        boolean prevStanding = standing, prevClimbDown = climbDown, prevClimbUp = climbUp, prevWalking = walking, prevRunning = running, prevSitting = sitting;

        if (y < -12 && isLow) {
            isHigh = true;
            isLow = false;
            stepCount++;
            stepped = true;
            if (currentActivity == ActivityType.ClimbUp || currentActivity == ActivityType.ClimbDown) {
                climbCount++;
            }
            Toast.makeText(tempThis, "Step Count: " + stepCount, Toast.LENGTH_SHORT).show();
        }
        else if(y > -9 && isHigh) {
            isLow = true;
            isHigh = false;
        }


        if (valueCounter < VALUE_COUNT) {
            lastValues[0][valueCounter] = x;
            lastValues[1][valueCounter] = y;
            lastValues[2][valueCounter] = z;
            lastValues[3][valueCounter] = x * x + z * z + y;
            lastValues[4][valueCounter++] = avgBar;
        }
        else {
            valueCounter = 0;
            StandardDeviation sd = new StandardDeviation();
            double avgFormula = 0, maxY = lastValues[3][0], minY = lastValues[3][0];
            double avgZ = 0, maxZ = lastValues[2][0], minZ = lastValues[2][0];
            double avgY = 0;

            for (int i = 0; i < VALUE_COUNT; i++) {
                double itemZ = lastValues[2][i];
                avgZ += itemZ;
                if (itemZ > maxZ) maxZ = itemZ;
                if (itemZ < minZ) minZ = itemZ;

                avgY += lastValues[1][i];
                double item = lastValues[1][i];
                if (item > maxY) maxY = item;
                if (item < minY) minY = item;

                avgFormula += lastValues[3][i];

                barAvg += lastValues[4][i];
            }



            avgY = avgY / VALUE_COUNT;
            avgZ = avgZ / VALUE_COUNT;
            avgFormula = avgFormula / VALUE_COUNT;
            double sdResult = sd.evaluate(lastValues[3]);
            double sdX = sd.evaluate(lastValues[0]);
            double sdY = sd.evaluate(lastValues[1]);
            double sdZ = sd.evaluate(lastValues[2]);


            if (barCounter == 2) {
                barAvg = barAvg / (VALUE_COUNT * (barCounter + 1));
//                climbDown = (barAvg - prevBarAvg < -0.4 && prevBarAvg != 0) || maxY - minY > 11 ;
//                climbUp = barAvg - prevBarAvg > 0.4 && prevBarAvg != 0;

                climbUp = (sdY < 3.5 && sdY > 2.5) || (barAvg - prevBarAvg > 0.3 && prevBarAvg != 0);
                prevBarAvg = barAvg;
                barCounter = -1;
                barAvg = 0;
            }
            barCounter++;

            boolean isStill = sdX < 1.3 && sdY < 1.5 && sdZ < 1.0;

            standing = isStill && y < -7;
            climbDown = sdY > 3.5 && maxY - minY > 15;
            //climbDown = maxY - minY > 10 && barAvg - prevBar < -0.2 && !isFirst;

            //climbUp = barAvg - prevBar > 0.3 && !isFirst;
//            climbDown = prevBar - barAvg >= 0.25 && !isFirst;
//            climbUp = barAvg - prevBar >= 0.2 && !isFirst;
            walking = stepped;
            stepped = false;
            running = maxZ - minZ > 19;
            sitting = isStill && !standing;

//            String message = "standing: ";
//            message += standing ? "true\n": "false\n";
//            message += "climbDown: ";
//            message += climbDown ? "true\n": "false\n";
//            message += "climbUp: false\n";
//            message += "walk: ";
//            message += walking ? "true\n" : "false\n";
//            message += "run: ";
//            message += running ? "true\n" : "false\n";
//            message += "sit: ";
//            message += sitting ? "true\n" : "false\n";
//            Toast.makeText(tempThis, message, Toast.LENGTH_SHORT).show();

            // Evaluate here with previous flags
            if (running) {
                //Toast.makeText(tempThis, "RUNNING", Toast.LENGTH_SHORT).show();
                currentActivity = ActivityType.Running;
            }
            else if (standing){
                //Toast.makeText(tempThis, "STANDING", Toast.LENGTH_SHORT).show();
                currentActivity = ActivityType.Standing;
            }
            else if (sitting) {
                //Toast.makeText(tempThis, "SITTING", Toast.LENGTH_SHORT).show();
                currentActivity = ActivityType.Sitting;
            }
            else if (walking){
                if (climbDown && (prevClimbDown || isFirst)) {
                    //Toast.makeText(tempThis, "STAIRS DOWN", Toast.LENGTH_SHORT).show();
                    currentActivity = ActivityType.ClimbDown;
                }
                else if (climbUp && (prevClimbUp || isFirst)) {
                    //Toast.makeText(tempThis, "STAIRS UP", Toast.LENGTH_SHORT).show();
                    currentActivity = ActivityType.ClimbUp;
                }
                else {
                    //Toast.makeText(tempThis, "WALKING", Toast.LENGTH_SHORT).show();
                    currentActivity = ActivityType.Walking;
                }
            }

            if (currentActivity != null) makeCall(temp, pulse);
            isFirst = false;
        }
    }

    private void onConnectionFailure(Throwable throwable) {
        Toast.makeText(this, "BT Failed to connect. Please try again!", Toast.LENGTH_SHORT).show();
        Log.e("BT", throwable.getMessage());
        long current = System.currentTimeMillis() - connectionFailureTimer;
        connectionFailureTimer = System.currentTimeMillis();
        btOn = false;
        makeCall(15.0, 81);
    }

    private void makeCall(double temp, int pulse) {
        Call<PredictResult> request = apiInterface.sendData(new RawDataRequest(new SensorData(currentActivity == null ? -1 : currentActivity.ordinal(), temp, pulse)));

        request.enqueue(new Callback<PredictResult>() {
            @Override
            public void onResponse(Call<PredictResult> call, Response<PredictResult> response) {
                Log.d("WIFI", "" + response.code());
            }

            @Override
            public void onFailure(Call<PredictResult> call, Throwable t) {
                Log.e("WIFI", t.getMessage());
            }
        });
    }


}
