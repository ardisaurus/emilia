package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.emilia.adapter.AdapterRegisteredDevice;
import com.example.user.emilia.model.RegisteredDevice;

import java.util.ArrayList;
import java.util.List;

public class AdminDeviceRegisteredActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<RegisteredDevice> mRegisteredDevice = new ArrayList<>();
    private RegisteredDevice registeredDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_device_registered);
        setTitle("Registered Device");

        mRecyclerView = findViewById(R.id.listdeviceregistered);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        registeredDevice = new RegisteredDevice("ig790", "user@example.com");
        mRegisteredDevice.add(registeredDevice);
        registeredDevice = new RegisteredDevice("ig790", "user@example.com");
        mRegisteredDevice.add(registeredDevice);
        registeredDevice = new RegisteredDevice("ig790", "user@example.com");
        mRegisteredDevice.add(registeredDevice);

        mAdapter = new AdapterRegisteredDevice(mRegisteredDevice);
        mRecyclerView.setAdapter(mAdapter);
    }
}
