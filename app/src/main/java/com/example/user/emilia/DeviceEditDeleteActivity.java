package com.example.user.emilia;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

public class DeviceEditDeleteActivity extends AppCompatActivity {
    Button btnSubmit;
    EditText txtPassword;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_edit_delete);
        setTitle("Delete Device");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");
        txtPassword = findViewById(R.id.txtPassword_deviceeditdelete);
        btnSubmit = findViewById(R.id.btnSubmit_deviceeditdelete);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceEditDeleteActivity.this);
                builder.setMessage("Are you sure to delete this device?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (txtPassword.getText().toString().isEmpty()){
                                    Toast.makeText(DeviceEditDeleteActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                                }else{
                                    final String password = txtPassword.getText().toString();
                                    if (password.length()>=8 && password.length()<=12) {
                                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthPrimaryDevice(dvc_id, md5(password) ,"auth");
                                        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                            @Override
                                            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                                    Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postDeletePrimaryDevice(dvc_id, "delete");
                                                    postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                        @Override
                                                        public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                            finish();
                                                            DeviceEditActivity.dea.finish();
                                                            FragmentDevicePrimary.fdp.refresh();
                                                            Toast.makeText(MainActivity.ma, "Device has been deleted", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                            Toast.makeText(DeviceEditDeleteActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(DeviceEditDeleteActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                Toast.makeText(DeviceEditDeleteActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else {
                                        Toast.makeText(DeviceEditDeleteActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
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
