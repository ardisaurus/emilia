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

import com.example.user.emilia.adapter.AdapterPrimaryDevice;
import com.example.user.emilia.model.PrimaryDevice;

import java.util.ArrayList;
import java.util.List;

public class FragmentDevicePrimary extends Fragment {
    private static final String TAG = "FragmentDevicePrimary";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<PrimaryDevice> mPrimaryDevice = new ArrayList<>();
    private PrimaryDevice device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_primary,container,false);
        mRecyclerView = view.findViewById(R.id.listprimarydevice);
        mLayoutManager = new LinearLayoutManager(MainActivity.ma);
        mRecyclerView.setLayoutManager(mLayoutManager);

        device = new PrimaryDevice("bk803","Front Door","Closed");
        mPrimaryDevice.add(device);
        device = new PrimaryDevice("ae342","Back Door","Closed");
        mPrimaryDevice.add(device);
        device = new PrimaryDevice("sl986","Bedroom Door","Closed");
        mPrimaryDevice.add(device);

        mAdapter = new AdapterPrimaryDevice(mPrimaryDevice);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
