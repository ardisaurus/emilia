package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AdminResetPasswordDeviceActivity extends AppCompatActivity {

    private TextView txtDvc_id;
    private String dvc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reset_password_device);
        setTitle("Reset Password");

        txtDvc_id = findViewById(R.id.lblDeviceid_adminresetpassword);

        Intent i = getIntent();
        dvc_id = i.getStringExtra("dvc_id");
        txtDvc_id.setText(dvc_id);
    }
}
