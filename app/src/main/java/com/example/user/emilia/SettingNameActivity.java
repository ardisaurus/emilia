package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingNameActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SessionManager session;
    Button btnSubmit;
    EditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_name);
        setTitle("Setting : Name");
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        txtName = findViewById(R.id.txtName_settingname);
        btnSubmit = findViewById(R.id.btnSubmit_settingname);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString().isEmpty()){
                    Toast.makeText(SettingNameActivity.this, "Fill the new name", Toast.LENGTH_SHORT).show();
                }else{
                    String name = txtName.getText().toString();
                    if (name.length()<=4){
                        Toast.makeText(SettingNameActivity.this, "Name need to be 5 or more character", Toast.LENGTH_SHORT).show();
                    }else{
                        HashMap<String, String> user = session.getUserDetails();
                        final String email = user.get(SessionManager.KEY_EMAIL);
                        Call<PostUser> postSettingNameCall = mApiInterface.postSettingName(email, name, "update", "name");
                        postSettingNameCall.enqueue(new Callback<PostUser>() {
                            @Override
                            public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                finish();
                                SettingActivity.sa.recreate();
                                Toast.makeText(SettingActivity.sa, "Name has been changed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PostUser> call, Throwable t) {
                                Toast.makeText(SettingNameActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });

    }
}
