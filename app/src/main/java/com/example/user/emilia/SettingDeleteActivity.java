package com.example.user.emilia;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.emilia.model.GetUser;
import com.example.user.emilia.model.PostUser;
import com.example.user.emilia.model.User;
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

public class SettingDeleteActivity extends AppCompatActivity {
    ApiInterface mApiInterface;
    SessionManager session;
    Button btnSubmit;
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_delete);
        setTitle("Delete Account");
        session = new SessionManager(getApplicationContext());
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        txtPassword = findViewById(R.id.txtPassword_settingdelete);
        btnSubmit = findViewById(R.id.btnSubmit_settingdelete);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingDeleteActivity.this);
                builder.setMessage("Are you sure to delete your account?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(txtPassword.getText().toString().isEmpty()){
                                    Toast.makeText(SettingDeleteActivity.this, "Insert your password", Toast.LENGTH_SHORT).show();
                                }else{
                                    if (txtPassword.getText().toString().length()<8 || txtPassword.getText().toString().length()>12){
                                        Toast.makeText(SettingDeleteActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Call<GetUser> userCall = mApiInterface.getUser("");
                                        userCall.enqueue(new Callback<GetUser>() {
                                            @Override
                                            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                                                List<User> listUser = response.body().getListDataUser();
                                                Integer userCount = listUser.size();
                                                Integer adminCount = 0;
                                                for(Integer i =0; i<=userCount-1; i++){
                                                    if (listUser.get(i).getLevel().equals("1") && listUser.get(i).getActive().equals("1")){
                                                        adminCount++;
                                                    }
                                                }
                                                if (adminCount>1){
                                                    HashMap<String, String> user = session.getUserDetails();
                                                    final String email = user.get(SessionManager.KEY_EMAIL);
                                                    String password = txtPassword.getText().toString();
                                                    Call<PostUser> postLoginCall = mApiInterface.postLogin(email, md5(password), "auth");
                                                    postLoginCall.enqueue(new Callback<PostUser>() {
                                                        @Override
                                                        public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                                            if (response.body().getmUser().getStatus().equals("success")){
                                                                Call<PostUser> postSettingDeleteCall = mApiInterface.postSettingDelete(email, "delete");
                                                                postSettingDeleteCall.enqueue(new Callback<PostUser>() {
                                                                    @Override
                                                                    public void onResponse(Call<PostUser> call, Response<PostUser> response) {
                                                                        SettingActivity.sa.finish();
                                                                        session.logoutUser();
                                                                        MainActivity.ma.recreate();
                                                                        finish();
                                                                        Toast.makeText(MainActivity.ma, "Account has been deleted", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<PostUser> call, Throwable t) {
                                                                        Toast.makeText(SettingDeleteActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }else{
                                                                Toast.makeText(SettingDeleteActivity.this, "Email And Password don't match ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<PostUser> call, Throwable t) {
                                                            Toast.makeText(SettingDeleteActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(SettingDeleteActivity.this, "Delete fail : one admin left", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<GetUser> call, Throwable t) {
                                                Toast.makeText(SettingDeleteActivity.this, "Connection fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
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
