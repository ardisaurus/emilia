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
import com.example.user.emilia.model.PostPrimaryDevice;
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

public class DeviceUnlockActivity extends AppCompatActivity {
    EditText txtPassword;
    Switch swEncription;
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
        swEncription = findViewById(R.id.swEncription_deviceunlock);
        btnSubmit=findViewById(R.id.btnSubmit_deviceunlock);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swEncription.isChecked()){
                    if (txtPassword.getText().toString().isEmpty()){
                        Toast.makeText(DeviceUnlockActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                    }else{
                        final String dvc_id = lblDvc_id.getText().toString();
                        final String password =txtPassword.getText().toString().trim();
                        if(password.length()==16){
                            Call<PostCrypto> postCryptoCall = mApiInterface.postRequest(dvc_id, "create");
                            postCryptoCall.enqueue(new Callback<PostCrypto>() {
                                @Override
                                public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                    String public_key= response.body().getmCrypto().getPublic_key();
                                    String modulo = response.body().getmCrypto().getModulo();
                                    final String session_id = response.body().getmCrypto().getSession_id();
                                    String aes_key=Aes.getSaltString();
                                    final String cipheraes = Aes.encrypt(password, aes_key);
                                    Call<GetCrypto> postCryptoCall = mApiInterface.getCrypto(session_id, aes_key);
                                    postCryptoCall.enqueue(new Callback<GetCrypto>() {
                                        @Override
                                        public void onResponse(Call<GetCrypto> call, Response<GetCrypto> response) {
                                            final List<Crypto> CryptoList = response.body().getListDataCrypto();
                                            final String cipherrsa = CryptoList.get(0).getCipher();
                                            Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthPrimaryDevice( dvc_id, session_id, cipheraes, cipherrsa, "auth");
                                            postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                @Override
                                                public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                    if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                                        HashMap<String, String> user = session.getUserDetails();
                                                        String email = user.get(SessionManager.KEY_EMAIL);
                                                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postUnlockPrimaryDevice(email, dvc_id, "unlock");
                                                        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                            @Override
                                                            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                                Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                    @Override
                                                                    public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                        finish();
                                                                        FragmentDevicePrimary.fdp.refresh();
                                                                        Toast.makeText(MainActivity.ma, "Device Unlocked", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                        Toast.makeText(DeviceUnlockActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                                Toast.makeText(DeviceUnlockActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                        postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                            @Override
                                                            public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                Toast.makeText(DeviceUnlockActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                Toast.makeText(DeviceUnlockActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                    Toast.makeText(DeviceUnlockActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(Call<GetCrypto> call, Throwable t) {
                                            Toast.makeText(DeviceUnlockActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<PostCrypto> call, Throwable t) {
                                    Toast.makeText(DeviceUnlockActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(DeviceUnlockActivity.this, "Password have to be 16 character", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if (txtPassword.getText().toString().isEmpty()){
                        Toast.makeText(DeviceUnlockActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                    }else{
                        final String dvc_id = lblDvc_id.getText().toString();
                        final String password =txtPassword.getText().toString().trim();
                        if(password.length()==16){
                            Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthPrimaryDeviceUnencripted(dvc_id, password, "auth_unencripted");
                            postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                @Override
                                public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                    if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                        HashMap<String, String> user = session.getUserDetails();
                                        String email = user.get(SessionManager.KEY_EMAIL);
                                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postUnlockPrimaryDevice(email, dvc_id, "unlock");
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
                            Toast.makeText(DeviceUnlockActivity.this, "Password have to be 16 character", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
