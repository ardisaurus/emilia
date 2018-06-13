package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

public class SettingNameActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SessionManager session;
    Button btnSubmit;
    EditText txtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_name);
        setTitle("Setting : Name");
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        txtname = findViewById(R.id.txtName_settingname);
        btnSubmit = findViewById(R.id.btnSubmit_settingname);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
