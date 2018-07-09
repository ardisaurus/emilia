package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DeviceUnlockActivity extends AppCompatActivity {
    EditText txtPassword;
    Button btnSubmit;
    TextView lblForget, lblDvc_id;
    public static DeviceUnlockActivity dua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_unlock);
        setTitle("Unlock : Primary Access");
        dua=this;
        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");
        lblDvc_id = findViewById(R.id.lblDeviceId_deviceunlock);
        lblDvc_id.setText(dvc_id);
        lblForget=findViewById(R.id.lblForgetPassword_deviceunlock);
        lblForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DeviceUnlockActivity.this, ForgetDevicePasswordActivity.class);
                i.putExtra("dvc_id",dvc_id);
                DeviceUnlockActivity.this.startActivity(i);
            }
        });


    }
}
