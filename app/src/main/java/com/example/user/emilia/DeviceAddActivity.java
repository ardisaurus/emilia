package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.PostPrimaryDevice;
import com.example.user.emilia.model.PrimaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceAddActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText txtPassword, txtDvc_id;
    private List<PrimaryDevice> mPrimaryDevice = new ArrayList<>();
    ApiInterface mApiInterface;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add);
        setTitle("Add Device : Primary Access");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        session = new SessionManager(getApplicationContext());
        txtDvc_id = findViewById(R.id.txtdevice_deviceadd);
        txtPassword = findViewById(R.id.txtPassword_deviceadd);
        btnSubmit = findViewById(R.id.btnSubmit_deviceadd);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtDvc_id.getText().toString().isEmpty() ||txtPassword.getText().toString().isEmpty()){
                    Toast.makeText(DeviceAddActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                }else{
                    final String dvc_id = txtDvc_id.getText().toString();
                    final String password = txtPassword.getText().toString();
                    if (password.length()>=8 && password.length()<=12) {
                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postIdcheckPrimaryDevice(dvc_id, "id_check");
                        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                            @Override
                            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                    Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthPrimaryDevice(dvc_id, md5(password) ,"auth");
                                    postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                        @Override
                                        public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                            if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                                HashMap<String, String> user = session.getUserDetails();
                                                String email = user.get(SessionManager.KEY_EMAIL);
                                                Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAddPrimaryDevice(dvc_id, email ,"insert");
                                                postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                    @Override
                                                    public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                        FragmentDevicePrimary.fdp.refresh();
                                                        finish();
                                                        Toast.makeText(MainActivity.ma, "Device has been added", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                        Toast.makeText(DeviceAddActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(DeviceAddActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                            Toast.makeText(DeviceAddActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(DeviceAddActivity.this, "Device Id not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                Toast.makeText(DeviceAddActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(DeviceAddActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
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
