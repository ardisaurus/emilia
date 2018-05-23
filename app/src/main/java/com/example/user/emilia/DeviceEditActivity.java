package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeviceEditActivity extends AppCompatActivity {
    private Button btnName, btnPassword, btnSecondary, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit);
        setTitle("Edit Device");

        btnName = findViewById(R.id.btnName_deviceedit);
        btnPassword = findViewById(R.id.btnPassword_deviceedit);
        btnSecondary = findViewById(R.id.btnSecondaryAccess_deviceedit);
        btnDelete = findViewById(R.id.btnDelete_deviceedit);

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditNameActivity.class);
                startActivity(i);
            }
        });
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditPasswordActivity.class);
                startActivity(i);
            }
        });
        btnSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditSecondaryAccessActivity.class);
                startActivity(i);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditDeleteActivity.class);
                startActivity(i);
            }
        });
    }
}
