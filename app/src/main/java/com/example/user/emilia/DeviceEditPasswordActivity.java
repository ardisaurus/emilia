package com.example.user.emilia;

import android.content.Intent;
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
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
                    final String OldPassword = txtOldPassword.getText().toString();
                    final String NewPassword1 = txtNewPassword1.getText().toString();
                    String NewPassword2 = txtNewPassword2.getText().toString();
                    if (OldPassword.length()==16 && NewPassword1.length()==16){
                        if (NewPassword1.equals(NewPassword2)){
                            Call<PostCrypto> postCryptoCall = mApiInterface.postRequest(dvc_id, "create");
                            postCryptoCall.enqueue(new Callback<PostCrypto>() {
                                @Override
                                public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                    String public_key= response.body().getmCrypto().getPublic_key();
                                    String modulo = response.body().getmCrypto().getModulo();
                                    final String session_id = response.body().getmCrypto().getSession_id();
                                    String aes_key=Aes.getSaltString();;
                                    final String cipheraes = Aes.encrypt(OldPassword, aes_key);
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
                                                        Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postEditPasswordPrimaryDevice(dvc_id, NewPassword1 ,"update", "password");
                                                        postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                            @Override
                                                            public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                                Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                                postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                                    @Override
                                                                    public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                        finish();
                                                                        Toast.makeText(DeviceEditActivity.dea, "Password has been changed", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                        Toast.makeText(DeviceEditPasswordActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                                Toast.makeText(DeviceEditPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        Call<PostCrypto> postDeleteSessionCall = mApiInterface.postDeleteSession(session_id, "delete_session");
                                                        postDeleteSessionCall.enqueue(new Callback<PostCrypto>() {
                                                            @Override
                                                            public void onResponse(Call<PostCrypto> call, Response<PostCrypto> response) {
                                                                Toast.makeText(DeviceEditPasswordActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<PostCrypto> call, Throwable t) {
                                                                Toast.makeText(DeviceEditPasswordActivity.this, "Session not removed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                    Toast.makeText(DeviceEditPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure(Call<GetCrypto> call, Throwable t) {
                                            Toast.makeText(DeviceEditPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure(Call<PostCrypto> call, Throwable t) {
                                    Toast.makeText(DeviceEditPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(DeviceEditPasswordActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(DeviceEditPasswordActivity.this, "Password need to be 16 character", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
