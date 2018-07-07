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

import com.example.user.emilia.adapter.AdapterSecondaryDevice;
import com.example.user.emilia.model.GetSecondaryDevice;
import com.example.user.emilia.model.SecondaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDeviceSecondary extends Fragment {
    public FragmentDeviceSecondary(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fds=this;
    }

    private static final String TAG = "FragmentDeviceSecondary";
    SessionManager session;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<SecondaryDevice> mSecondaryDevice = new ArrayList<>();
    ApiInterface mApiInterface;
    public static FragmentDeviceSecondary fds;
    private SecondaryDevice device;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_secondary,container,false);

        mRecyclerView = view.findViewById(R.id.listsecondarydevice);
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
        final List<SecondaryDevice> mSecondaryDevice = new ArrayList<>();
        Call<GetSecondaryDevice> secondaryDevice = mApiInterface.getSecondaryDevice(email, 1);
        secondaryDevice.enqueue(new Callback<GetSecondaryDevice>() {
            @Override
            public void onResponse(Call<GetSecondaryDevice> call, Response<GetSecondaryDevice> response) {
                List<SecondaryDevice> DeviceList = response.body().getListSecondaryDevice();
                for (Integer i = 0; i<=DeviceList.size()-1; i++){
                    mSecondaryDevice.add(DeviceList.get(i));
                }
                mAdapter = new AdapterSecondaryDevice(mSecondaryDevice);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetSecondaryDevice> call, Throwable t) {
                Toast.makeText(MainActivity.ma, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
