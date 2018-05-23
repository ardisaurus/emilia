package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {
    private Button btnLogout, btnEmail, btnName, btnDob, btnPassword, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Account Settings");

        btnEmail = findViewById(R.id.btnEmail_setting);
        btnName = findViewById(R.id.btnName_setting);
        btnDob = findViewById(R.id.btnDob_setting);
        btnPassword = findViewById(R.id.btnPassword_setting);
        btnDelete = findViewById(R.id.btnDelete_setting);
        btnLogout = findViewById(R.id.btnLogout_Setting);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingEmailActivity.class);
                startActivity(i);
            }
        });

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingNameActivity.class);
                startActivity(i);
            }
        });

        btnDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingDobActivity.class);
                startActivity(i);
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingPasswordActivity.class);
                startActivity(i);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingDeleteActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
