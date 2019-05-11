package com.example.maddi.logme;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.maddi.logme.API.ApiInterface;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {
    private ApiInterface apiInterface;
    //private static String baseUrl = "http://192.168.43.234:6502/api/";
    //public static String baseUrl = "http://10.1.40.214:6502/api/";
    public static String baseUrl = "http://192.168.43.234:6502/api/";
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


    }

    private ApiInterface createApiInterface(String customBase) {
        apiInterface = new Retrofit.Builder().baseUrl(customBase != null ? customBase : baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class);
        return apiInterface;
    }
}
