package com.example.user.emilia;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.Crypto;
import com.example.user.emilia.model.GetCrypto;
import com.example.user.emilia.model.PostCrypto;
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
                    if (password.length()==16) {
                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postIdcheckPrimaryDevice(dvc_id, "id_check");
                        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                            @Override
                            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                if(response.body().getmPrimaryDevice().getStatus().equals("success")){
                                    Call<PostCrypto> postCryptoCall = mApiInterface.postRequest(dvc_id, "create");
                                    postCryptoCall.enqueue(new Callback<PostCrypto>() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                            String public_key= response.body().getmCrypto().getPublic_key();
                                            String modulo = response.body().getmCrypto().getModulo();
                                            final String session_id = response.body().getmCrypto().getSession_id();
                                            final String aes_key=Aes.getSaltString();
                                            Call<PostPrimaryDevice> postSecondaryDeviceCall = mApiInterface.postEncryptionCheck(dvc_id, "encryption_check");
                                            postSecondaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                @Override
                                                public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                    final String dvc_encryption = response.body().getmPrimaryDevice().getDvc_encryption();
                                                    final String cipheraes = Aes.encrypt(password, aes_key, dvc_encryption);
                                                    Call<GetCrypto> postCryptoCall = mApiInterface.getCrypto(session_id, aes_key);
                                                    postCryptoCall.enqueue(new Callback<GetCrypto>() {
                                                        @Override
                                                        public void onResponse(Call<GetCrypto> call, Response<GetCrypto> response) {
                                                            final List<Crypto> CryptoList = response.body().getListDataCrypto();
                                                            final String cipherrsa = CryptoList.get(0).getCipher();
                                                            Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postAuthPrimaryDevice(dvc_id, session_id, cipheraes, cipherrsa, "auth");
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
                                                                                Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                                postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                                    @Override
                                                                                    public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                                        FragmentDevicePrimary.fdp.refresh();
                                                                                        finish();
                                                                                        Toast.makeText(MainActivity.ma, "Device has been added", Toast.LENGTH_SHORT).show();
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                                        Toast.makeText(DeviceAddActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                                                Toast.makeText(DeviceAddActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }else{
                                                                        Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                        postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                            @Override
                                                                            public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                                Toast.makeText(DeviceAddActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                                Toast.makeText(DeviceAddActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                                    Toast.makeText(DeviceAddActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onFailure(Call<GetCrypto> call, Throwable t) {
                                                            Toast.makeText(DeviceAddActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                    Toast.makeText(DeviceAddActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure(Call<PostCrypto> call, Throwable t) {
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
                        Toast.makeText(DeviceAddActivity.this, "Password need to be 16 character", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
