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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceEditPasswordActivity extends AppCompatActivity {
    EditText txtOldPassword, txtNewPassword1, txtNewPassword2;
    ApiInterface mApiInterface;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit_password);
        setTitle("Edit Device : Password");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");
        txtOldPassword = findViewById(R.id.txtOldpassword_deviceeditpassword);
        txtNewPassword1 = findViewById(R.id.txtNewpassword1_deviceeditpassword);
        txtNewPassword2 = findViewById(R.id.txtNewpassword2_deviceeditpassword);
        btnSubmit = findViewById(R.id.btnSubmit_deviceeditpassword);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtOldPassword.getText().toString().isEmpty() || txtNewPassword1.getText().toString().isEmpty() || txtNewPassword2.getText().toString().isEmpty()){
                    Toast.makeText(DeviceEditPasswordActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                }else{
                    String OldPassword = txtOldPassword.getText().toString();
                    final String NewPassword1 = txtNewPassword1.getText().toString();
                    String NewPassword2 = txtNewPassword2.getText().toString();
                    if (OldPassword.length()>=8 && OldPassword.length()<=12 && NewPassword1.length()>=8 && NewPassword1.length()<=12){
                        if (NewPassword1.equals(NewPassword2)){
                            Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthPrimaryDevice(dvc_id, md5(OldPassword) ,"auth");
                            postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                @Override
                                public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                    if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postEditPasswordPrimaryDevice(dvc_id, md5(NewPassword1) ,"update", "password");
                                        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                            @Override
                                            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                finish();
                                                Toast.makeText(DeviceEditActivity.dea, "Password has been changed", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                Toast.makeText(DeviceEditPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(DeviceEditPasswordActivity.this, "Wrong old password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                    Toast.makeText(DeviceEditPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(DeviceEditPasswordActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(DeviceEditPasswordActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private static String md5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}
