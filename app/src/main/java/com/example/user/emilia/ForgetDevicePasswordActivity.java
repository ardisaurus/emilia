package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.PostPrimaryDevice;
import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetDevicePasswordActivity extends AppCompatActivity {
    Button btnSubmit;
    EditText txtOldPassword, txtNewPassword1, txtNewPassword2;
    ApiInterface mApiInterface;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_device_password);
        setTitle("Forget Device Password");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        session = new SessionManager(getApplicationContext());
        Intent i = getIntent();
        final String dvc_id = i.getStringExtra("dvc_id");
        txtOldPassword = findViewById(R.id.txtAccountPassword_forgetdevicepassword);
        txtNewPassword1 = findViewById(R.id.txtNewDevicePassword1_forgetdevicepassword);
        txtNewPassword2 = findViewById(R.id.txtNewDevicePassword2_forgetdevicepassword);
        btnSubmit = findViewById(R.id.btnSubmit_forgetdevicepassword);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtOldPassword.getText().toString().isEmpty() || txtNewPassword1.getText().toString().isEmpty() || txtNewPassword2.getText().toString().isEmpty()){
                    Toast.makeText(ForgetDevicePasswordActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, String> user = session.getUserDetails();
                    final String email = user.get(SessionManager.KEY_EMAIL);
                    final String OldPassword = txtOldPassword.getText().toString();
                    final String NewPassword1 = txtNewPassword1.getText().toString();
                    String NewPassword2 = txtNewPassword2.getText().toString();
                    if (OldPassword.length()>=8 && OldPassword.length()<=12){
                        if (NewPassword1.length()==16){
                            if (NewPassword1.equals(NewPassword2)){
                                Call<PostUser> postLoginCall = mApiInterface.postLogin(email, md5(OldPassword), "auth");
                                postLoginCall.enqueue(new Callback<PostUser>() {
                                    @Override
                                    public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                        if(response.body().getmUser().getStatus().equals("success")){
                                            Call<PostPrimaryDevice> postPrimaryDeviceCall = mApiInterface.postForgetPasswordPrimaryDevice(email, md5(OldPassword),dvc_id, md5(NewPassword1) ,"forgot_password");
                                            postPrimaryDeviceCall.enqueue(new Callback<PostPrimaryDevice>() {
                                                @Override
                                                public void onResponse(Call<PostPrimaryDevice> call, Response<PostPrimaryDevice> response) {
                                                    finish();
                                                    Toast.makeText(DeviceUnlockActivity.dua, "Password Has been changed", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<PostPrimaryDevice> call, Throwable t) {
                                                    Toast.makeText(ForgetDevicePasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            Toast.makeText(ForgetDevicePasswordActivity.this, "Wrong account password", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PostUser> call, Throwable t) {
                                        Toast.makeText(ForgetDevicePasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(ForgetDevicePasswordActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ForgetDevicePasswordActivity.this, "New Password need to be 16 characters", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ForgetDevicePasswordActivity.this, "Account Password need to be between 8 to 12 characters", Toast.LENGTH_SHORT).show();
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
