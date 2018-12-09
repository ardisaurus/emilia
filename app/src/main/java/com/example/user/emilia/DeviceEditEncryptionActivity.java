package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.emilia.model.PostPrimaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceEditEncryptionActivity extends AppCompatActivity {
    Button btnSubmit;
    ApiInterface mApiInterface;
    RadioGroup radioEncryptionGroup;
    RadioButton radioEncryptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit_encryption);
        setTitle("Edit Device");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");
        radioEncryptionGroup = findViewById(R.id.rdgEncryption_deviceeditencryption);
        btnSubmit = findViewById(R.id.btnSubmit_deviceediencryption);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioEncryptionGroup.getCheckedRadioButtonId();
                radioEncryptionButton = (RadioButton) findViewById(selectedId);
                String selectedEncryption = radioEncryptionButton.getText().toString();
                if (selectedEncryption.equals("RSA + 3DES")){
                    selectedEncryption = "3des";
                }else if(selectedEncryption.equals("RSA + AES 256bit")){
                    selectedEncryption = "aes2";
                }else{
                    selectedEncryption = "aes1";
                }
                Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postEditEncrytionPrimaryDevice(dvc_id,selectedEncryption,"update","encryption");
                postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                    @Override
                    public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                        finish();
                        Toast.makeText(DeviceEditActivity.dea, "Encryption has been changed", Toast.LENGTH_SHORT).show();
                        FragmentDevicePrimary.fdp.refresh();
                    }

                    @Override
                    public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                        Toast.makeText(DeviceEditEncryptionActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
