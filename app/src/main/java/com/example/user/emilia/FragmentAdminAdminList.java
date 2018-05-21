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

import com.example.user.emilia.R;
import com.example.user.emilia.adapter.AdapterAdmin;
import com.example.user.emilia.model.Admin;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdminAdminList extends Fragment {
    private static final String TAG = "FragmentAdminAdminList";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<Admin> mAdmin = new ArrayList<>();
    Admin admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_admin_list,container,false);

        mRecyclerView = view.findViewById(R.id.listadmin);
        mLayoutManager = new LinearLayoutManager(MainActivity.ma);
        mRecyclerView.setLayoutManager(mLayoutManager);

        admin = new Admin("user@example.com", "user", "30-12-1970", "confirmed");
        mAdmin.add(admin);
        admin = new Admin("user@example.com", "user", "30-12-1970", "confirmed");
        mAdmin.add(admin);
        admin = new Admin("user@example.com", "user", "30-12-1970", "confirmed");
        mAdmin.add(admin);

        mAdapter = new AdapterAdmin(mAdmin);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}
