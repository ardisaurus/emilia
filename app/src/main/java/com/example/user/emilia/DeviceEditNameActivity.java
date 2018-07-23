package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.PostPrimaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceEditNameActivity extends AppCompatActivity {
    EditText txtName;
    Button btnSubmit;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit_name);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        setTitle("Edit Device : Name");Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");

        txtName = findViewById(R.id.txtName_deviceeditname);
        btnSubmit = findViewById(R.id.btnSubmit_deviceeditname);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtName.getText().toString().isEmpty()){
                    Toast.makeText(DeviceEditNameActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                }else{
                    final String name = txtName.getText().toString();
                    if (name.length()>=4 && name.length()<=50) {
                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postEditNamePrimaryDevice(dvc_id, name,"update", "name");
                        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                            @Override
                            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                finish();
                                Toast.makeText(DeviceEditActivity.dea, "Name has been changed", Toast.LENGTH_SHORT).show();
                                FragmentDevicePrimary.fdp.refresh();
                            }

                            @Override
                            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                Toast.makeText(DeviceEditNameActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(DeviceEditNameActivity.this, "Name have to be combination of 4 to 50 characters", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
