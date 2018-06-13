package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

public class SettingPasswordActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SessionManager session;
    EditText txtPassword1, txtPassword2;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        setTitle("Setting : Password");
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

    }
}
