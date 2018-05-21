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

import com.example.user.emilia.adapter.AdapterSecondaryDevice;
import com.example.user.emilia.model.SecondaryDevice;

import java.util.ArrayList;
import java.util.List;

public class FragmentDeviceSecondary extends Fragment {
    private static final String TAG = "FragmentDeviceSecondary";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<SecondaryDevice> mSecondaryDevice = new ArrayList<>();
    SecondaryDevice device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_secondary,container,false);

        mRecyclerView = view.findViewById(R.id.listsecondarydevice);
        mLayoutManager = new LinearLayoutManager(MainActivity.ma);
        mRecyclerView.setLayoutManager(mLayoutManager);

        device = new SecondaryDevice("bk803","Front Door","Closed");
        mSecondaryDevice.add(device);
        device = new SecondaryDevice("ae342","Back Door","Closed");
        mSecondaryDevice.add(device);
        device = new SecondaryDevice("sl986","Bedroom Door","Closed");
        mSecondaryDevice.add(device);

        mAdapter = new AdapterSecondaryDevice(mSecondaryDevice);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
