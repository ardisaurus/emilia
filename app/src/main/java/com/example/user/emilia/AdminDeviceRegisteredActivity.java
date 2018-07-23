package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.user.emilia.adapter.AdapterRegisteredDevice;
import com.example.user.emilia.model.Admin;
import com.example.user.emilia.model.GetRegisteredDevice;
import com.example.user.emilia.model.RegisteredDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDeviceRegisteredActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<RegisteredDevice> mRegisteredDevice = new ArrayList<>();
    ApiInterface mApiInterface;
    private RegisteredDevice registeredDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_device_registered);
        setTitle("Registered Device");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        mRecyclerView = findViewById(R.id.listdeviceregistered);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        refresh();
    }

    public void refresh(){
        final List<RegisteredDevice> mRegisteredDevice = new ArrayList<>();
        Call<GetRegisteredDevice> registeredDevice = mApiInterface.getRegisteredDevice(1);
        registeredDevice.enqueue(new Callback<GetRegisteredDevice>() {
            @Override
            public void onResponse(Call<GetRegisteredDevice> call, Response<GetRegisteredDevice> response) {
                List<RegisteredDevice> DeviceList = response.body().getListRegisteredDevice();
                for (Integer i = 0; i<=DeviceList.size()-1; i++){
                        mRegisteredDevice.add(DeviceList.get(i));
                }
                mAdapter = new AdapterRegisteredDevice(mRegisteredDevice);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetRegisteredDevice> call, Throwable t) {
                Toast.makeText(AdminDeviceRegisteredActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
