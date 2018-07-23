package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.PostSecondaryDevice;
import com.example.user.emilia.model.SecondaryDevice;
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

public class DeviceAddSecondaryActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText txtPassword, txtDvc_id;
    private List<SecondaryDevice> mSecondaryDevice = new ArrayList<>();
    ApiInterface mApiInterface;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add_secondary);
        setTitle("Add Device : Secondary Access");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        session = new SessionManager(getApplicationContext());
        txtDvc_id = findViewById(R.id.txtdevice_deviceaddsecondary);
        txtPassword = findViewById(R.id.txtPassword_deviceaddsecondary);
        btnSubmit = findViewById(R.id.btnSubmit_deviceaddsecondary);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtDvc_id.getText().toString().isEmpty() ||txtPassword.getText().toString().isEmpty()){
                    Toast.makeText(DeviceAddSecondaryActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                }else{
                    final String dvc_id = txtDvc_id.getText().toString();
                    final String password = txtPassword.getText().toString();
                    if (password.length()>=8 && password.length()<=12) {
                        Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postIdcheckSecondaryDevice(dvc_id, "id_check_sc");
                        postSecondaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                            @Override
                            public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                if(response.body().getmSecondaryDevice().getStatus().equals("success")){
                                    Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postAuthSecondaryDevice(dvc_id, md5(password) ,"auth_sc");
                                    postSecondaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                        @Override
                                        public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                            if(response.body().getmSecondaryDevice().getStatus().equals("success")){
                                                HashMap<String, String> user = session.getUserDetails();
                                                String email = user.get(SessionManager.KEY_EMAIL);
                                                Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postAddSecondaryDevice(dvc_id, email ,"insert_sc");
                                                postSecondaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                                    @Override
                                                    public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                                        FragmentDeviceSecondary.fds.refresh();
                                                        finish();
                                                        Toast.makeText(MainActivity.ma, "Device has been added", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                                        Toast.makeText(DeviceAddSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }else{
                                                Toast.makeText(DeviceAddSecondaryActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                            Toast.makeText(DeviceAddSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(DeviceAddSecondaryActivity.this, "Device Id not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                Toast.makeText(DeviceAddSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(DeviceAddSecondaryActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
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
