package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.model.Crypto;
import com.example.user.emilia.model.GetCrypto;
import com.example.user.emilia.model.PostCrypto;
import com.example.user.emilia.model.PostSecondaryDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceUnlockSecondaryActivity extends AppCompatActivity {
    EditText txtPassword;
    Switch swEncription;
    Button btnSubmit;
    TextView lblDvc_id;
    ApiInterface mApiInterface;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_unlock_secondary);
        setTitle("Unlock : Secondary Access");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        session = new SessionManager(getApplicationContext());
        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");
        lblDvc_id = findViewById(R.id.lblDeviceid_deviceunlocksecondary);
        lblDvc_id.setText(dvc_id);
        txtPassword = findViewById(R.id.txtPassword_deviceunlocksecondary);
        swEncription = findViewById(R.id.swEncription_deviceunlocksecondary);
        btnSubmit = findViewById(R.id.btnSubmit_deviceunlocksecondary);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swEncription.isChecked()){
                    if (txtPassword.getText().toString().isEmpty()){
                        Toast.makeText(DeviceUnlockSecondaryActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                    }else{
                        final String dvc_id = lblDvc_id.getText().toString();
                        final String password = txtPassword.getText().toString();
                        if (password.length()==16) {
                            Call<PostCrypto> postCryptoCall = mApiInterface.postRequest(dvc_id, "create");
                            postCryptoCall.enqueue(new Callback<PostCrypto>() {
                                @Override
                                public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                    String public_key= response.body().getmCrypto().getPublic_key();
                                    String modulo = response.body().getmCrypto().getModulo();
                                    final String session_id = response.body().getmCrypto().getSession_id();
                                    String aes_key=Aes.getSaltString();;
                                    final String cipheraes = Aes.encrypt(password, aes_key);
                                    Call<GetCrypto> postCryptoCall = mApiInterface.getCrypto(session_id, aes_key);
                                    postCryptoCall.enqueue(new Callback<GetCrypto>() {
                                        @Override
                                        public void onResponse(Call<GetCrypto> call, Response<GetCrypto> response) {
                                            final List<Crypto> CryptoList = response.body().getListDataCrypto();
                                            final String cipherrsa = CryptoList.get(0).getCipher();
                                            Call<PostSecondaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthSecondaryDevice(dvc_id, session_id, cipheraes, cipherrsa, "auth_sc");
                                            postPrimaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                                @Override
                                                public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                                    if(response.body().getmSecondaryDevice().getStatus().equals("success")){
                                                        HashMap<String, String> user = session.getUserDetails();
                                                        String email = user.get(SessionManager.KEY_EMAIL);
                                                        Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postUnlockSecondaryDevice(email, dvc_id ,"unlock_sc");
                                                        postSecondaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                                            @Override
                                                            public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                                                Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                    @Override
                                                                    public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                        finish();
                                                                        FragmentDeviceSecondary.fds.refresh();
                                                                        Toast.makeText(MainActivity.ma, "Device Unlocked", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                        Toast.makeText(DeviceUnlockSecondaryActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                                                Toast.makeText(DeviceUnlockSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                        postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                            @Override
                                                            public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                Toast.makeText(DeviceUnlockSecondaryActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                Toast.makeText(DeviceUnlockSecondaryActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                                    Toast.makeText(DeviceUnlockSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure(Call<GetCrypto> call, Throwable t) {
                                            Toast.makeText(DeviceUnlockSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure(Call<PostCrypto> call, Throwable t) {
                                    Toast.makeText(DeviceUnlockSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(DeviceUnlockSecondaryActivity.this, "Password need to be 16 character", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if (txtPassword.getText().toString().isEmpty()){
                        Toast.makeText(DeviceUnlockSecondaryActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                    }else{
                        final String dvc_id = lblDvc_id.getText().toString();
                        final String password = txtPassword.getText().toString();
                        if (password.length()==16) {
                            Call<PostSecondaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthSecondaryDeviceUnencripted(dvc_id, password, "auth_sc_unencripted");
                            postPrimaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                @Override
                                public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                    if(response.body().getmSecondaryDevice().getStatus().equals("success")){
                                        HashMap<String, String> user = session.getUserDetails();
                                        String email = user.get(SessionManager.KEY_EMAIL);
                                        Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postUnlockSecondaryDevice(email, dvc_id ,"unlock_sc");
                                        postSecondaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                            @Override
                                            public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                                finish();
                                                FragmentDeviceSecondary.fds.refresh();
                                                Toast.makeText(MainActivity.ma, "Device Unlocked", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                                Toast.makeText(DeviceUnlockSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(DeviceUnlockSecondaryActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                    Toast.makeText(DeviceUnlockSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(DeviceUnlockSecondaryActivity.this, "Password need to be 16 character", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }
}
