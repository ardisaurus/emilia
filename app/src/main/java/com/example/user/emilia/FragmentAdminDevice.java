package com.example.user.emilia;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.emilia.adapter.AdapterAdminDevice;
import com.example.user.emilia.model.AdminDevice;
import com.example.user.emilia.model.GetAdminDevice;
import com.example.user.emilia.model.RegisteredDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminDevice extends Fragment {

    public FragmentAdminDevice() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fad=this;
    }
    private static final String TAG = "FragmentAdminDevice";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<AdminDevice> mAdminDevice = new ArrayList<>();
    ApiInterface mApiInterface;
    public static FragmentAdminDevice fad;

    private AdminDevice device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_device,container,false);
        mRecyclerView = view.findViewById(R.id.listadmindevice);
        mLayoutManager = new LinearLayoutManager(MainActivity.ma);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        refresh();
        return view;
    }

    public void refresh(){
        final List<AdminDevice> mAdminDevice = new ArrayList<>();
        Call<GetAdminDevice> adminDevice = mApiInterface.getAdminDevice();
        adminDevice.enqueue(new Callback<GetAdminDevice>() {
            @Override
            public void onResponse(Call<GetAdminDevice> call, Response<GetAdminDevice> response) {
                List<AdminDevice> DeviceList = response.body().getListAdminDevice();
                for (Integer i = 0; i<=DeviceList.size()-1; i++){
                    mAdminDevice.add(DeviceList.get(i));
                }
                mAdapter = new AdapterAdminDevice(mAdminDevice);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetAdminDevice> call, Throwable t) {
                Toast.makeText(MainActivity.ma, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
