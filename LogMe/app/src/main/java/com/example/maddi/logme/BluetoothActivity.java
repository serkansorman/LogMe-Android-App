package com.example.maddi.logme;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maddi.logme.API.Response.SensorResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BluetoothActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Button wifi_button;
    private EditText ssid;
    private EditText password;
    private EditText host;
    private MainApplication app;
    private String customBase = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        View mHeaderView = navigationView.getHeaderView(0);

        drawerLayout = findViewById(R.id.drawer);
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
        app =  ((MainApplication) getApplication());
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        wifi_button = findViewById(R.id.wifi_button);
        ssid = findViewById(R.id.ssid_edit);
        password = findViewById(R.id.password_edit);
        host = findViewById(R.id.host_edit);

        initBtButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!app.btOn) {
            app.startBt();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initBtButton() {
        wifi_button.setOnClickListener(view -> {
            boolean error = false;
            String hostAddr = "";
            if (TextUtils.isEmpty(ssid.getText())) {
                ssid.setError("SSID Required!!!");
                error = true;
            }
            if (TextUtils.isEmpty(host.getText())){
                host.setError("Host Address Required!!!");
                error = true;
            }
            else {
                hostAddr = host.getText().toString();
                if (hostAddr.charAt(host.length() - 1) != '/') {
                    hostAddr += '/';
                }
                try {
                    app.setBaseUrl(hostAddr);
                } catch (Exception ex) {
                    Log.e("WIFI", "Url error");
                }
            }
            if (TextUtils.isEmpty(password.getText())) {
                password.setError("Password Required!!!");
                error = true;
            }

            if (error) return;


            String btMessage = ssid.getText().toString() + "," + password.getText().toString() + "," + hostAddr;


            //app.setBaseUrl(hostAddr);
            app.deviceInterface.sendMessage(btMessage);
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

