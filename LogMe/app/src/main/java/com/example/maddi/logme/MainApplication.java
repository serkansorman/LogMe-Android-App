package com.example.maddi.logme;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.maddi.logme.API.ApiInterface;
import com.polidea.rxandroidble2.LogConstants;
import com.polidea.rxandroidble2.LogOptions;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.exceptions.BleException;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {
    private RxBleClient rxBleClient;
    private ApiInterface apiInterface;
    private static String baseUrl = "http://192.168.43.234:6502/api/";
    //private static String baseUrl = "http://192.168.137.1:57906";
    /**
     * In practise you will use some kind of dependency injection pattern.
     */
    public static RxBleClient getRxBleClient(Context context) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.rxBleClient;
    }

    public static ApiInterface getApiInterface(Context context) {
        MainApplication application = (MainApplication) context.getApplicationContext();
        return application.apiInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rxBleClient = RxBleClient.create(this);
        RxBleClient.updateLogOptions(new LogOptions.Builder()
                .setLogLevel(LogConstants.INFO)
                .setMacAddressLogSetting(LogConstants.MAC_ADDRESS_FULL)
                .setUuidsLogSetting(LogConstants.UUIDS_FULL)
                .setShouldLogAttributeValues(true)
                .build()
        );

        RxJavaPlugins.setErrorHandler(throwable -> {
            if (throwable instanceof UndeliverableException && throwable.getCause() instanceof BleException) {
                Log.v("SampleApplication", "Suppressed UndeliverableException: " + throwable.toString());
                return; // ignore BleExceptions as they were surely delivered at least once
            }
            // add other custom handlers if needed
            throw new RuntimeException("Unexpected Throwable in RxJavaPlugins error handler", throwable);
        });

        apiInterface = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class);
    }
}
