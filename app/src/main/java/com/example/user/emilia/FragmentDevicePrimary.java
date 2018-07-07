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

import com.example.user.emilia.adapter.AdapterPrimaryDevice;
import com.example.user.emilia.model.Admin;
import com.example.user.emilia.model.GetPrimaryDevice;
import com.example.user.emilia.model.PrimaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDevicePrimary extends Fragment {
    public FragmentDevicePrimary(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fdp=this;
    }

    private static final String TAG = "FragmentDevicePrimary";
    SessionManager session;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<PrimaryDevice> mPrimaryDevice = new ArrayList<>();
    ApiInterface mApiInterface;
    public static FragmentDevicePrimary fdp;
    private PrimaryDevice device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_primary,container,false);
        mRecyclerView = view.findViewById(R.id.listprimarydevice);
        mLayoutManager = new LinearLayoutManager(MainActivity.ma);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        session = new SessionManager(MainActivity.ma);
        Boolean status = session.isLoggedIn();
        if (status){
            refresh();
        }
        return view;
    }

    public void refresh() {
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManager.KEY_EMAIL);
        final List<PrimaryDevice> mPrimaryDevice = new ArrayList<>();
        Call<GetPrimaryDevice> primaryDevice = mApiInterface.getPrimaryDevice(email);
        primaryDevice.enqueue(new Callback<GetPrimaryDevice>() {
            @Override
            public void onResponse(Call<GetPrimaryDevice> call, Response<GetPrimaryDevice> response) {
                List<PrimaryDevice> DeviceList = response.body().getListPrimaryDevice();
                for (Integer i = 0; i<=DeviceList.size()-1; i++){
                        mPrimaryDevice.add(DeviceList.get(i));
                }
                mAdapter = new AdapterPrimaryDevice(mPrimaryDevice);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetPrimaryDevice> call, Throwable t) {
                Toast.makeText(MainActivity.ma, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
