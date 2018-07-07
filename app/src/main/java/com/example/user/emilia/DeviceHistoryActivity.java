package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.user.emilia.adapter.AdapterHistory;
import com.example.user.emilia.model.GetHistory;
import com.example.user.emilia.model.History;
import com.example.user.emilia.model.SecondaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceHistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private List<History> mHistory = new ArrayList<>();
    ApiInterface mApiInterface;
    private History history;
    private String dvc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);
        setTitle("Access History");

        mRecyclerView = findViewById(R.id.listhistory);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        Intent i = getIntent();
        dvc_id = i.getStringExtra("dvc_id");

        final List<History> mHistory= new ArrayList<>();
        Call<GetHistory> history = mApiInterface.getHistory(dvc_id);
        history.enqueue(new Callback<GetHistory>() {
            @Override
            public void onResponse(Call<GetHistory> call, Response<GetHistory> response) {
                List<History> HistoryList = response.body().getListHitory();
                for (Integer i = 0; i<=HistoryList.size()-1; i++){
                    mHistory.add(HistoryList.get(i));
                }
                mAdapter = new AdapterHistory(mHistory);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetHistory> call, Throwable t) {
                Toast.makeText(DeviceHistoryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
