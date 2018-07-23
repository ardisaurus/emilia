package com.example.user.emilia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.emilia.model.PostAdminDevice;
import com.example.user.emilia.rest.ApiClient;
import com.example.user.emilia.rest.ApiInterface;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminResetPasswordDeviceActivity extends AppCompatActivity {

    private TextView lblDvc_id;
    private String dvc_id;
    Button btnSubmit;
    EditText txtPassword1, txtPassword2;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reset_password_device);
        setTitle("Reset Password");
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        lblDvc_id = findViewById(R.id.lblDeviceid_adminresetpassword);
        txtPassword1 = findViewById(R.id.txtPassword1_adminresetpassword);
        txtPassword2 = findViewById(R.id.txtPassword2_adminresetpassword);
        btnSubmit = findViewById(R.id.btnSubmit_adminresetpassword);

        Intent i = getIntent();
        dvc_id = i.getStringExtra("dvc_id");
        lblDvc_id.setText(dvc_id);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dvc_id = lblDvc_id.getText().toString();
                final String password1 = txtPassword1.getText().toString();
                String password2 = txtPassword2.getText().toString();
                if (password1.length()>=8 && password1.length()<=12) {
                    if (password1.equals(password2)) {
                        Call<PostAdminDevice> postAdminDeviceCall = mApiInterface.postAddAdminDevice(dvc_id, md5(password1), "reset_password");
                        postAdminDeviceCall.enqueue(new Callback<PostAdminDevice>() {
                            @Override
                            public void onResponse(Call<PostAdminDevice> call, Response<PostAdminDevice> response) {
                                finish();
                                Toast.makeText(MainActivity.ma, "Password has been changed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<PostAdminDevice> call, Throwable t) {
                                Toast.makeText(AdminResetPasswordDeviceActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(AdminResetPasswordDeviceActivity.this, "New passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AdminResetPasswordDeviceActivity.this, "Password need to be between 8 to 12 character", Toast.LENGTH_SHORT).show();
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
