package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.model.PostPrimaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceUnlockActivity extends AppCompatActivity {
    EditText txtPassword;
    Button btnSubmit;
    TextView lblForget, lblDvc_id;
    public static DeviceUnlockActivity dua;
    ApiInterface mApiInterface;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_unlock);
        setTitle("Unlock : Primary Access");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        session = new SessionManager(getApplicationContext());
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
        txtPassword = findViewById(R.id.txtPassword_deviceunlock);
        btnSubmit=findViewById(R.id.btnSubmit_deviceunlock);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPassword.getText().toString().isEmpty()){
                    Toast.makeText(DeviceUnlockActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                }else{
                    final String dvc_id = lblDvc_id.getText().toString();
                    final String password = txtPassword.getText().toString();
                    if (password.length()>=8 && password.length()<=12) {
                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthPrimaryDevice(dvc_id, md5(password) ,"auth");
                        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                            @Override
                            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                    HashMap<String, String> user = session.getUserDetails();
                                    String email = user.get(SessionManager.KEY_EMAIL);
                                    Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postUnlockPrimaryDevice(email, dvc_id, md5(password) ,"unlock");
                                    postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                        @Override
                                        public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                            finish();
                                            FragmentDevicePrimary.fdp.refresh();
                                            Toast.makeText(MainActivity.ma, "Device Unlocked", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                            Toast.makeText(DeviceUnlockActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(DeviceUnlockActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                Toast.makeText(DeviceUnlockActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(DeviceUnlockActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
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
