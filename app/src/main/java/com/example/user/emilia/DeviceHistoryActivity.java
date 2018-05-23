package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.emilia.adapter.AdapterHistory;
import com.example.user.emilia.model.History;

import java.util.ArrayList;
import java.util.List;

public class DeviceHistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<History> mHistory = new ArrayList<>();
    private History history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);
        setTitle("Access History");

        mRecyclerView = findViewById(R.id.listhistory);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        history = new History("user", "user@example.com", "30-12-1970 : 08:00");
        mHistory.add(history);
        history = new History("user1", "user1@example.com", "30-12-1970 : 08:00");
        mHistory.add(history);
        history = new History("user2", "user2@example.com", "30-12-1970 : 08:00");
        mHistory.add(history);

        mAdapter = new AdapterHistory(mHistory);
        mRecyclerView.setAdapter(mAdapter);
    }
}
