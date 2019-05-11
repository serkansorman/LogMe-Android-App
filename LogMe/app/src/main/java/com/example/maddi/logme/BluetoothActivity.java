package com.example.maddi.logme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maddi.logme.API.Response.SensorResponse;
import com.harrysoft.androidbluetoothserial.BluetoothManager;
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice;
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface;

import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BluetoothActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView bleText;
    private Button bt_button;
    private Button wifi_button;
    private TextView wifi_text;
    private EditText local_ip;
    MainApplication mainApplication;
    private BluetoothManager btManager;
    private SimpleBluetoothDeviceInterface deviceInterface;
    private Disposable connectionDisposable;
    private String deviceMacAddress = "B4:E6:2D:E9:53:B7";
    private UUID deviceServiceUUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");
    private String customBase = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        View mHeaderView = navigationView.getHeaderView(0);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        bleText = findViewById(R.id.ble_text);
        bt_button = findViewById(R.id.bt_button);
        wifi_button = findViewById(R.id.wifi_button);
        wifi_text = findViewById(R.id.wifi_text);
        local_ip = findViewById(R.id.local_ip);
        bt_button.setOnClickListener(view -> {
            if (connectionDisposable != null) {
                connectionDisposable.dispose();
                clearSubscription();
            }
            else {
                startBt();
            }
        });
        wifi_button.setOnClickListener(view -> {
            if (local_ip.getText().length() > 0) {
                customBase = local_ip.getText().toString();
                deviceInterface.sendMessage(local_ip.getText().toString());
            }
            else {
                customBase = null;
            }
            //makeCall();

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        startBt();
    }

    @Override
    protected void onPause() {
        super.onPause();


        if (btManager != null) {
            btManager.close();
            clearSubscription();
        }

    }

    private void onConnectionFailure(Throwable throwable) {
        //noinspection ConstantConditions
        bleText.setText("BT ERROR");
    }

    private void clearSubscription() {
        connectionDisposable = null;
        bleText.setText("BT CLOSED");
    }

    private void startBt() {
        btManager = BluetoothManager.getInstance();
        if (btManager == null) {
            Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_LONG).show();
            finish();
        }

        connectionDisposable = btManager.openSerialDevice(deviceMacAddress)
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
        deviceInterface.sendMessage("Hello world!");
    }

    private void onMessageSent(String message) {
        // We sent a message! Handle it here.
        Toast.makeText(this, "Sent a message! Message was: " + message, Toast.LENGTH_LONG).show(); // Replace context with your context instance.
    }

    private void onMessageReceived(String message) {
        // We received a message! Handle it here.
        bleText.setText(message);
    }

    private void makeCall() {
        Call<SensorResponse> request = MainApplication.getApiInterface(this, customBase).getLastData();

        request.enqueue(new Callback<SensorResponse>() {
            @Override
            public void onResponse(Call<SensorResponse> call, Response<SensorResponse> response) {
                int code = response.code();
                SensorResponse res = response.body();
                try {
                    wifi_text.setText(res.data.get(0).toString());
                } catch (NullPointerException ex) {
                    Log.d("WIFI", ex.toString());
                }
            }

            @Override
            public void onFailure(Call<SensorResponse> call, Throwable t) {
                wifi_text.setText("SERVER ERROR");
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item1:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.item2:
                intent = new Intent(this, SummaryActivity.class);
                startActivity(intent);
                break;
            case R.id.item3:
                intent = new Intent(this, SensorsActivity.class);
                startActivity(intent);
                break;
            case R.id.item4:
                intent = new Intent(this, BluetoothActivity.class);
                startActivity(intent);

            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}

