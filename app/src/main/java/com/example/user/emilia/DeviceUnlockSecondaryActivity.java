package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeviceUnlockSecondaryActivity extends AppCompatActivity {
    EditText txtPassword;
    Button btnSubmit;
    TextView lblDvc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_unlock_secondary);
        setTitle("Unlock : Secondary Access");

        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");
        lblDvc_id = findViewById(R.id.lblDeviceid_deviceunlocksecondary);
        lblDvc_id.setText(dvc_id);

    }
}
