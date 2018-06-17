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

import com.example.user.emilia.R;
import com.example.user.emilia.adapter.AdapterAdmin;
import com.example.user.emilia.model.Admin;
import com.example.user.emilia.model.GetAdmin;
import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.User;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminAdminList extends Fragment {

    public FragmentAdminAdminList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        faal=this;
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    ApiInterface mApiInterface;
    private Admin admin;
    public static FragmentAdminAdminList faal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_admin_list,container,false);
        mRecyclerView = view.findViewById(R.id.listadmin);
        mLayoutManager = new LinearLayoutManager(MainActivity.ma);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        refresh();
        return view;
    }

    public void refresh(){
        final List<Admin> mAdmin = new ArrayList<>();
        Call<GetAdmin> adminCall = mApiInterface.getAdmin("");
        adminCall.enqueue(new Callback<GetAdmin>() {
            @Override
            public void onResponse(Call<GetAdmin> call, Response<GetAdmin> response) {
                List<Admin> UserList = response.body().getListDataAdmin();
                for (Integer i = 0; i<=UserList.size()-1; i++){
                    if (UserList.get(i).getLevel().equals("1")){
                        mAdmin.add(UserList.get(i));
                    }
                }
                mAdapter = new AdapterAdmin(mAdmin);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetAdmin> call, Throwable t) {
                Toast.makeText(MainActivity.ma, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
