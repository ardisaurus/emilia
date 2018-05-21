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

import com.example.user.emilia.adapter.AdapterAdminDevice;
import com.example.user.emilia.model.AdminDevice;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdminDevice extends Fragment {
    private static final String TAG = "FragmentAdminDevice";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<AdminDevice> mAdminDevice = new ArrayList<>();
    AdminDevice device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_device,container,false);
        mRecyclerView = view.findViewById(R.id.listadmindevice);
        mLayoutManager = new LinearLayoutManager(MainActivity.ma);
        mRecyclerView.setLayoutManager(mLayoutManager);

        device = new AdminDevice("bk803");
        mAdminDevice.add(device);
        device = new AdminDevice("ae342");
        mAdminDevice.add(device);
        device = new AdminDevice("sl986");
        mAdminDevice.add(device);

        mAdapter = new AdapterAdminDevice(mAdminDevice);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
