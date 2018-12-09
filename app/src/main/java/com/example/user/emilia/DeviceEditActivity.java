package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeviceEditActivity extends AppCompatActivity {
    public DeviceEditActivity (){}

    public static DeviceEditActivity dea;
    private Button btnName, btnPassword, btnSecondary, btnDelete, btnEncryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit);
        setTitle("Edit Device");
        dea = this;

        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");

        btnName = findViewById(R.id.btnName_deviceedit);
        btnPassword = findViewById(R.id.btnPassword_deviceedit);
        btnSecondary = findViewById(R.id.btnSecondaryAccess_deviceedit);
        btnDelete = findViewById(R.id.btnDelete_deviceedit);
        btnEncryption = findViewById(R.id.btnEncryption_deviceedit);
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditNameActivity.class);
                i.putExtra("dvc_id",dvc_id);
                startActivity(i);
            }
        });
        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditPasswordActivity.class);
                i.putExtra("dvc_id",dvc_id);
                startActivity(i);
            }
        });
        btnSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditSecondaryAccessActivity.class);
                i.putExtra("dvc_id",dvc_id);
                startActivity(i);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditDeleteActivity.class);
                i.putExtra("dvc_id",dvc_id);
                startActivity(i);
            }
        });
        btnEncryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DeviceEditEncryptionActivity.class);
                i.putExtra("dvc_id",dvc_id);
                startActivity(i);
            }
        });
    }
}
