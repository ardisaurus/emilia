package com.example.user.emilia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SettingPasswordActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SessionManager session;
    EditText txtPasswordOld, txtPassword1, txtPassword2;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        setTitle("Setting : Password");
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        txtPasswordOld = findViewById(R.id.txtOldpassword_settingpassword);
        txtPassword1 = findViewById(R.id.txtNewpassword1_settingpassword);
        txtPassword2 =  findViewById(R.id.txtNewpassword2_settingpassword);

        btnSubmit = findViewById(R.id.btnSubmit_settingpassword);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtPasswordOld.getText().toString().isEmpty() || txtPassword1.getText().toString().isEmpty() || txtPassword2.getText().toString().isEmpty()){
                    Toast.makeText(SettingPasswordActivity.this, "Fill every available form", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, String> user = session.getUserDetails();
                    final String email = user.get(SessionManager.KEY_EMAIL);
                    String passwordOld = txtPasswordOld.getText().toString();
                    final String password1 = txtPassword1.getText().toString();
                    String password2 = txtPassword2.getText().toString();
                    if (password1.length()>=8 && password1.length()<=12){
                        if (password1.equals(password2)){
                            Call<PostUser> postLoginCall = mApiInterface.postLogin(email, md5(passwordOld), "auth");
                            postLoginCall.enqueue(new Callback<PostUser>() {
                                @Override
                                public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                    if (response.body().getmUser().getStatus().equals("success")){
                                        Call<PostUser> postSettingPasswordCall = mApiInterface.postSettingPassword(email, md5(password1), "update", "password");
                                        postSettingPasswordCall.enqueue(new Callback<PostUser>() {
                                            @Override
                                            public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                                finish();
                                                Toast.makeText(SettingActivity.sa, "Password has been changed", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<PostUser> call, Throwable t) {
                                                Toast.makeText(SettingPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(SettingPasswordActivity.this, "Old password is wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<PostUser> call, Throwable t) {
                                    Toast.makeText(SettingPasswordActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(SettingPasswordActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SettingPasswordActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
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
