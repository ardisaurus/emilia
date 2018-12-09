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
                    if (password.length()==16) {
                        Call<PostCrypto> postCryptoCall = mApiInterface.postRequest(dvc_id, "create");
                        postCryptoCall.enqueue(new Callback<PostCrypto>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                String public_key= response.body().getmCrypto().getPublic_key();
                                String modulo = response.body().getmCrypto().getModulo();
                                final String session_id = response.body().getmCrypto().getSession_id();
                                final String aes_key=Aes.getSaltString();
                                Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postEncryptionCheckSecondary(dvc_id, "encryption_check");
                                postSecondaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                    @Override
                                    public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                        final String dvc_encryption = response.body().getmSecondaryDevice().getDvc_encryption();
                                        final String cipheraes = Aes.encrypt(password, aes_key, dvc_encryption);
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
                                                        Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postIdcheckSecondaryDevice(dvc_id, "id_check_sc");
                                                        postSecondaryDeviceCall.enqueue(new Callback<PostSecondaryDevice>() {
                                                            @Override
                                                            public void onResponse(Call<PostSecondaryDevice> call, Response<PostSecondaryDevice> response) {
                                                                if(response.body().getmSecondaryDevice().getStatus().equals("success")){
                                                                    Call<PostSecondaryDevice> postSecondaryDeviceCall = mApiInterface.postAuthSecondaryDevice(dvc_id, session_id, cipheraes, cipherrsa, "auth_sc");
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
                                                                                        Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                                        postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                                            @Override
                                                                                            public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                                                FragmentDeviceSecondary.fds.refresh();
                                                                                                finish();
                                                                                                Toast.makeText(MainActivity.ma, "Device has been added", Toast.LENGTH_SHORT).show();
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                                                Toast.makeText(DeviceAddSecondaryActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                                                                        Toast.makeText(DeviceAddSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                            }else{
                                                                                Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                                postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                                    @Override
                                                                                    public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                                        Toast.makeText(DeviceAddSecondaryActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                                        Toast.makeText(DeviceAddSecondaryActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
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

                                                    }

                                                    @Override
                                                    public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                                        Toast.makeText(DeviceAddSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailure(Call<GetCrypto> call, Throwable t) {
                                                Toast.makeText(DeviceAddSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<PostSecondaryDevice> call, Throwable t) {
                                        Toast.makeText(DeviceAddSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<PostCrypto> call, Throwable t) {
                                Toast.makeText(DeviceAddSecondaryActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(DeviceAddSecondaryActivity.this, "Password need to be 16 character", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
